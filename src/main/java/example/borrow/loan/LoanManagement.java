package example.borrow.loan;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import example.borrow.book.Book.Barcode;
import example.borrow.book.BookCollected;
import example.borrow.book.BookPlacedOnHold;
import example.borrow.book.BookRepository;
import example.borrow.book.BookReturned;
import example.borrow.loan.Loan.LoanStatus;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class LoanManagement {

    private final LoanRepository loans;
    private final BookRepository books;
    private final ApplicationEventPublisher events;
    private final LoanMapper mapper;

    /**
     * Place a book on hold.
     *
     * @param barcode Unique identifier of the book
     */
    public LoanDto hold(String barcode, Long patronId) {
        var book = books.findByInventoryNumber(new Barcode(barcode))
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));

        if (!book.available()) {
            throw new IllegalStateException("Book not available!");
        }

        var dateOfHold = LocalDate.now();
        var loan = Loan.of(barcode, dateOfHold, patronId);
        var dto = mapper.toDto(loans.save(loan));
        events.publishEvent(
                new BookPlacedOnHold(
                        book.getId(),
                        book.getIsbn(),
                        book.getInventoryNumber().barcode(),
                        loan.getPatronId(),
                        dateOfHold));
        return dto;
    }

    /**
     * Collect a book previously placed on hold.
     *
     * @param loanId Unique identifier of the book loan
     */
    public LoanDto checkout(Long loanId) {
        var loan = loans.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("No loan found"));

        var book = books.findByInventoryNumber(loan.getBookBarcode())
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));

        if (!book.onHold()) {
            throw new IllegalStateException("Book is not on hold!");
        }

        var dateOfCheckout = LocalDate.now();
        loan.activate(dateOfCheckout);
        var dto = mapper.toDto(loans.save(loan));
        events.publishEvent(new BookCollected(book.getId(), book.getIsbn(), book.getInventoryNumber().barcode(), loan.getPatronId(), dateOfCheckout));
        return dto;
    }

    /**
     * Return a borrowed book.
     *
     * @param loanId Unique identifier of the book loan
     */
    public LoanDto checkin(Long loanId) {
        var dateOfCheckin = LocalDate.now();

        var loan = loans.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("No loan found"));
        loan.complete(dateOfCheckin);
        var dto = mapper.toDto(loans.save(loan));

        var book = books.findByInventoryNumber(loan.getBookBarcode())
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));
        events.publishEvent(new BookReturned(book.getId(), book.getIsbn(), book.getInventoryNumber().barcode(), loan.getPatronId(), dateOfCheckin));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<LoanDto> activeLoans() {
        return loans.findLoanByStatus(LoanStatus.ACTIVE)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LoanDto> onHoldLoans() {
        return loans.findLoanByStatus(LoanStatus.HOLDING)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<LoanDto> locate(Long loanId) {
        return loans.findById(loanId)
                .map(mapper::toDto);
    }
}
