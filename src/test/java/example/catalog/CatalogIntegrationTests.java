package example.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import example.catalog.internal.CatalogBook.Barcode;
import example.catalog.internal.CatalogManagement;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@Transactional
@ApplicationModuleTest
class CatalogIntegrationTests {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:catalog_books.sql");
    }

    @Autowired
    CatalogManagement books;

    @Test
    void shouldAddBookToInventory(Scenario scenario) {
        scenario.stimulate(() -> books.addToCatalog("A title", new Barcode("999"), "654", "An author"))
                .andWaitForEventOfType(BookAddedToCatalog.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.title()).isEqualTo("A title");
                    assertThat(event.inventoryNumber()).isEqualTo("999");
                    assertThat(event.isbn()).isEqualTo("654");
                    assertThat(event.author()).isEqualTo("An author");
                    assertThat(dto.id()).isNotNull();
                });
    }

    @Test
    void shouldListBooks() {
        var issuedBooks = books.fetchBooks();
        assertThat(issuedBooks).hasSizeGreaterThanOrEqualTo(2);
    }
}
