package com.abhinav.app.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BookServiceIT {

    @Autowired
    BookService bookService;

    @Test
    void testAddBookToInventory() {

    }

    @Test
    void shouldListIssuedBooks() {
        var books = bookService.issuedBooks();
        assertThat(books).hasSize(1);
        assertThat(books.get(0).isIssued()).isTrue();
    }
}
