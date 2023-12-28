package example.borrow.loan;

import java.time.LocalDate;

public record LoanDto(Long id, Long bookId, Long patronId, LocalDate dateOfHold,
                      LocalDate dateOfCheckout, int holdDurationInDays, int loanDurationInDays,
                      LocalDate dateOfCheckin, Loan.LoanStatus status) {
}
