package example.borrow;

import java.time.LocalDate;

public record LoanDto(Long id, String bookBarcode, Long patronId,
                      LocalDate dateOfIssue, int loanDurationInDays,
                      LocalDate dateOfReturn, Loan.LoanStatus status) {
}
