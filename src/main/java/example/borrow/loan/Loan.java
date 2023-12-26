package example.borrow.loan;

import java.time.LocalDate;

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
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private Long patronId;

    private LocalDate dateOfCheckout;

    private LocalDate dateOfHold;

    private int holdDurationInDays;

    private int loanDurationInDays;

    private LocalDate dateOfCheckin;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Version
    private Long version;

    Loan(Long bookId, LocalDate dateOfHold) {
        this.bookId = bookId;
        this.dateOfHold = dateOfHold;
        this.holdDurationInDays = 3;
        this.status = LoanStatus.HOLDING;
    }

    public static Loan of(Long bookId, LocalDate dateOfHold) {
        return new Loan(bookId, dateOfHold);
    }

    public boolean isActive() {
        return LoanStatus.ACTIVE.equals(this.status);
    }

    public boolean isHolding() {
        return LoanStatus.HOLDING.equals(this.status);
    }

    public boolean isOverdue() {
        return LoanStatus.OVERDUE.equals(this.status);
    }

    public boolean isCompleted() {
        return LoanStatus.COMPLETED.equals(this.status);
    }

    public void complete(LocalDate dateOfCheckin) {
        if (isCompleted()) {
            throw new IllegalStateException("Loan is not active!");
        }
        this.status = LoanStatus.COMPLETED;
        this.dateOfCheckin = dateOfCheckin;
    }

    public void activate(LocalDate dateOfCheckout) {
        if (!isHolding()) {
            throw new IllegalStateException("Loan is not holding!");
        }
        this.loanDurationInDays = 14;
        this.dateOfCheckout = dateOfCheckout;
        this.status = LoanStatus.ACTIVE;
    }

    public enum LoanStatus {
        HOLDING, ACTIVE, OVERDUE, COMPLETED
    }
}
