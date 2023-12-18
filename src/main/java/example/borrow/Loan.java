package example.borrow;

import java.time.LocalDate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "barcode", column = @Column(name = "book_barcode"))
    private Book book;

    private Long patronId;

    private LocalDate dateOfIssue;

    private int loanDurationInDays;

    private LocalDate dateOfReturn;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Version
    private Long version;

    Loan(String bookBarcode) {
        this.book = new Book(bookBarcode);
        this.dateOfIssue = LocalDate.now();
        this.loanDurationInDays = 14;
        this.status = LoanStatus.ACTIVE;
    }

    public static Loan of(String bookBarcode) {
        return new Loan(bookBarcode);
    }

    public boolean isActive() {
        return LoanStatus.ACTIVE.equals(this.status);
    }

    public boolean isOverdue() {
        return LoanStatus.OVERDUE.equals(this.status);
    }

    public boolean isCompleted() {
        return LoanStatus.COMPLETED.equals(this.status);
    }

    public void complete() {
        if (isCompleted()) {
            throw new IllegalStateException("Loan is not active!");
        }
        this.status = LoanStatus.COMPLETED;
        this.dateOfReturn = LocalDate.now();
    }

    public enum LoanStatus {
        ACTIVE, OVERDUE, COMPLETED
    }

    /**
     * Book modeled as a value object in the Loan module. It only includes one property.
     *
     * @param barcode The barcode of the book.
     */
    public record Book(String barcode) {
    }
}
