package com.abhinav.app.borrow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("""
            SELECT new com.abhinav.app.borrow.LoanWithBookDto(l.id, l.dateOfIssue, l.bookBarcode, b.title, b.author.name)
            FROM Loan l
            INNER JOIN Book b ON l.bookBarcode = b.inventoryNumber.barcode
            WHERE l.status = :status
            """)
    List<LoanWithBookDto> findLoansWithStatus(Loan.LoanStatus status);
}
