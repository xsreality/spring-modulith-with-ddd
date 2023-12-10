package example.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BookManagementIT {

    @Autowired
    BookManagement bookManagement;

    @Test
    void shouldListIssuedBooks() {
        var books = bookManagement.issuedBooks();
        assertThat(books).hasSize(1);
        assertThat(books.get(0).status()).isEqualTo(Book.BookStatus.ISSUED);
    }
}
