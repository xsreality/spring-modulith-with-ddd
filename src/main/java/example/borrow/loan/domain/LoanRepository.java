package example.borrow.loan.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import example.borrow.loan.domain.Loan.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findLoanByStatus(LoanStatus status);
}
