package example.borrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import example.borrow.book.domain.Book;
import example.borrow.book.domain.BookCollected;
import example.borrow.book.domain.BookPlacedOnHold;
import example.borrow.book.domain.BookRepository;
import example.borrow.book.domain.BookReturned;
import example.borrow.loan.domain.Loan.LoanStatus;
import example.borrow.loan.application.LoanManagement;
import example.catalog.BookAddedToCatalog;

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

    @Autowired
    BookRepository books;

    @Test
    void shouldCreateBookOnNewBookInCatalog(Scenario scenario) {
        scenario.publish(() -> new BookAddedToCatalog("A title", "9999", "73294", "An author"))
                .customize(it -> it.atMost(Duration.ofMillis(200)))
                .andWaitForStateChange(() -> books.findByInventoryNumber(new Book.Barcode("9999")))
                .andVerify(book -> {
                    //noinspection OptionalGetWithoutIsPresent
                    assertThat(book.get().getInventoryNumber().barcode()).isEqualTo("9999");
                    assertThat(book.get().getIsbn()).isEqualTo("73294");
                });
    }

    @Test
    void shouldCreateLoanOnPlacingHold(Scenario scenario) {
        scenario.stimulate(() -> loans.hold("13268510", 1L))
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

    @Test
    void shouldListActiveLoans() {
        var loans = this.loans.activeLoans();
        assertThat(loans).isNotEmpty();
    }

    @Test
    void shouldListOnHoldLoans() {
        var loans = this.loans.onHoldLoans();
        assertThat(loans).isNotEmpty();
    }
}
