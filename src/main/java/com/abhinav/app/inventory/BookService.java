package com.abhinav.app.inventory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void addToInventory(String title, Book.Barcode inventoryNumber, String isbn, String authorName) {
        var book = new Book(title, inventoryNumber, isbn, new Book.Author(authorName));
        bookRepository.save(book);
    }

    @Transactional
    public void removeFromInventory(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    public Optional<Book> locate(String barcode) {
        var inventoryNumber = new Book.Barcode(barcode);
        return bookRepository.findByInventoryNumber(inventoryNumber);
    }

    @Transactional
    public void issue(String barcode) {
        var inventoryNumber = new Book.Barcode(barcode);
        var book = bookRepository.findByInventoryNumber(inventoryNumber)
                .map(Book::markIssued)
                .orElseThrow(() -> new IllegalStateException("Book not found!"));
        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> issuedBooks() {
        return bookRepository.findByStatus(Book.BookStatus.ISSUED);
    }
}
