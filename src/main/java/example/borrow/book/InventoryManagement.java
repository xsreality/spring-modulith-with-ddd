package example.borrow.book;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.borrow.book.Book.Barcode;
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
