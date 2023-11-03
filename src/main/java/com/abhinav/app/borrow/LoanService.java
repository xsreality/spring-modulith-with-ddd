package com.abhinav.app.borrow;

import com.abhinav.app.inventory.BookService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService books;

    /**
     * Borrow a book.
     *
     * @param barcode Unique identifier of the book
     */
    public Loan checkout(String barcode) {
        books.issue(barcode);
        var loan = Loan.of(barcode);
        return loanRepository.save(loan);
    }

    /**
     * Return a borrowed book.
     *
     * @param loanId Unique identifier of the book loan
     */
    public Loan checkin(Long loanId) {
        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("No loan found"));
        loan.complete();
        return loanRepository.save(loan);
    }

    public List<LoanWithBookDto> activeLoans() {
        return loanRepository.findLoansWithStatus(Loan.LoanStatus.ACTIVE);
    }
}
