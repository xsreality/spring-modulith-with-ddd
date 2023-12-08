package example.borrow;

import java.time.LocalDate;

public record LoanWithBookDto(Long loanId, LocalDate dateOfIssue, String bookBarcode,
                              String bookTitle, String author) {
}
