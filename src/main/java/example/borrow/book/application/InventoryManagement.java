package example.borrow.book.application;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.borrow.book.domain.Book;
import example.borrow.book.domain.Book.Barcode;
import example.borrow.book.domain.BookCollected;
import example.borrow.book.domain.BookPlacedOnHold;
import example.borrow.book.domain.BookReturned;
import example.borrow.book.domain.BookRepository;
import example.catalog.BookAddedToCatalog;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class InventoryManagement {

    private final BookRepository books;

    @ApplicationModuleListener
    public void on(BookAddedToCatalog event) {
        books.save(new Book(event.title(), new Barcode(event.inventoryNumber()), event.isbn()));
    }

    @ApplicationModuleListener
    public void on(BookPlacedOnHold event) {
        var book = books.findById(event.bookId())
                .map(Book::markOnHold)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));
        books.save(book);
    }

    @ApplicationModuleListener
    public void on(BookCollected event) {
        var book = books.findById(event.bookId())
                .map(Book::markIssued)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));
        books.save(book);
    }

    @ApplicationModuleListener
    public void on(BookReturned event) {
        var book = books.findById(event.bookId())
                .map(Book::markAvailable)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));
        books.save(book);
    }
}
