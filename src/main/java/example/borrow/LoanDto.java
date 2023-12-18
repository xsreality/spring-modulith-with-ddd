package example.borrow;

import java.time.LocalDate;

import example.borrow.Loan.Book;

public record LoanDto(Long id, Book book, Long patronId,
                      LocalDate dateOfIssue, int loanDurationInDays,
                      LocalDate dateOfReturn, Loan.LoanStatus status) {
}
