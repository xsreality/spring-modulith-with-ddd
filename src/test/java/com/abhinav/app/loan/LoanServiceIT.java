package com.abhinav.app.loan;

import com.abhinav.app.borrow.LoanService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class LoanServiceIT {

    @Autowired
    LoanService loanService;

    @Test
    void shouldIssueBookOnCheckout() {
        var loan = loanService.checkout("13268510");
        assertThat(loan.isActive()).isTrue();
        assertThat(loan.getBookBarcode()).isEqualTo("13268510");
    }

    @Test
    void shouldCompleteLoanOnCheckin() {
        var loan = loanService.checkout("13268510");
        var completedLoan = loanService.checkin(loan.getId());
        assertThat(completedLoan.isCompleted()).isTrue();
    }

    @Test
    void shouldListActiveLoans() {
        var loans = loanService.activeLoans();
        assertThat(loans).hasSize(1);
    }
}
