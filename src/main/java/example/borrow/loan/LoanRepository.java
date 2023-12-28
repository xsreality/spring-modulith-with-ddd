package example.borrow.loan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import example.borrow.loan.Loan.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findLoanByStatus(LoanStatus status);
}
