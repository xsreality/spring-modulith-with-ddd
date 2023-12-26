package example.borrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import example.borrow.book.BookCollected;
import example.borrow.book.BookPlacedOnHold;
import example.borrow.book.BookReturned;
import example.borrow.loan.Loan.LoanStatus;
import example.borrow.loan.LoanManagement;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ApplicationModuleTest
class LoanIntegrationTests {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:borrow.sql");
    }

    @Autowired
    LoanManagement loans;

    @Test
    void shouldCreateLoanOnPlacingHold(Scenario scenario) {
        scenario.stimulate(() -> loans.hold("13268510"))
                .andWaitForEventOfType(BookPlacedOnHold.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.inventoryNumber()).isEqualTo("13268510");
                    assertThat(dto.status()).isEqualTo(LoanStatus.HOLDING);
                });
    }

    @Test
    void shouldActivateLoanOnCheckout(Scenario scenario) {
        scenario.stimulate(() -> loans.checkout(10L))
                .andWaitForEventOfType(BookCollected.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.inventoryNumber()).isEqualTo("49031878");
                    assertThat(dto.status()).isEqualTo(LoanStatus.ACTIVE);
                });
    }

    @Test
    void shouldCompleteLoanOnCheckin(Scenario scenario) {
        scenario.stimulate(() -> loans.checkin(11L))
                .andWaitForEventOfType(BookReturned.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.inventoryNumber()).isEqualTo("37040952");
                    assertThat(dto.status()).isEqualTo(LoanStatus.COMPLETED);
                });
    }

//    @Test
//    void shouldListActiveLoans() {
//        var loans = this.loans.activeLoans();
//        assertThat(loans).hasSize(1);
//    }
}
