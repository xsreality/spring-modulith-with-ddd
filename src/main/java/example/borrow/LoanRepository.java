package example.borrow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("""
            SELECT new example.borrow.LoanWithBookDto(l.id, l.dateOfIssue, l.book.barcode, b.title, b.author.name)
            FROM Loan l
            INNER JOIN Book b ON l.book.barcode = b.inventoryNumber.barcode
            WHERE l.status = :status
            """)
    List<LoanWithBookDto> findLoansWithStatus(Loan.LoanStatus status);
}
