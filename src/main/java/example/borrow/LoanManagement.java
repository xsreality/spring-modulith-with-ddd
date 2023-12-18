package example.borrow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import example.borrow.Loan.LoanStatus;
import example.inventory.BookManagement;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class LoanManagement {

    private final LoanRepository loanRepository;
    private final BookManagement books;
    private final LoanMapper mapper;

    /**
     * Borrow a book.
     *
     * @param barcode Unique identifier of the book
     */
    public LoanDto checkout(String barcode) {
        books.issue(barcode);
        var loan = Loan.of(barcode);
        var savedLoan = loanRepository.save(loan);
        return mapper.toDto(savedLoan);
    }

    /**
     * Return a borrowed book.
     *
     * @param loanId Unique identifier of the book loan
     */
    public LoanDto checkin(Long loanId) {
        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("No loan found"));
        books.release(loan.getBook().barcode());
        loan.complete();
        return mapper.toDto(loanRepository.save(loan));
    }

    @Transactional(readOnly = true)
    public List<LoanWithBookDto> activeLoans() {
        return loanRepository.findLoansWithStatus(LoanStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Optional<LoanDto> locate(Long loanId) {
        return loanRepository.findById(loanId)
                .map(mapper::toDto);
    }
}
