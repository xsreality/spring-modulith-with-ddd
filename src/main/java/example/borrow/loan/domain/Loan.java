package example.borrow.loan.domain;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.annotation.ValueObject;

import java.time.LocalDate;

import example.borrow.book.domain.Book.Barcode;
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

@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
public class Loan {

    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Barcode bookBarcode;

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

    Loan(String barcode, LocalDate dateOfHold, Long patronId) {
        this.bookBarcode = new Barcode(barcode);
        this.dateOfHold = dateOfHold;
        this.patronId = patronId;
        this.holdDurationInDays = 3;
        this.status = LoanStatus.HOLDING;
    }

    public static Loan of(String barcode, LocalDate dateOfHold, Long patronId) {
        return new Loan(barcode, dateOfHold, patronId);
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

    @ValueObject
    public enum LoanStatus {
        HOLDING, ACTIVE, OVERDUE, COMPLETED
    }
}
