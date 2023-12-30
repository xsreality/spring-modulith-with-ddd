package example.borrow.loan;

import java.time.LocalDate;

import example.borrow.book.Book.Barcode;

public record LoanDto(Long id, String bookBarcode, Long patronId, LocalDate dateOfHold,
                      LocalDate dateOfCheckout, int holdDurationInDays, int loanDurationInDays,
                      LocalDate dateOfCheckin, Loan.LoanStatus status) {
}
