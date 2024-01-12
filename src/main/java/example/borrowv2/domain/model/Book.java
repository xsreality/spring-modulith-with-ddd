package example.borrowv2.domain.model;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.ddd.types.ValueObject;

import java.util.UUID;

import lombok.Getter;

@Getter
public class Book implements AggregateRoot<Book, Book.BookId> {

    private BookId id;

    private String title;

    private Barcode inventoryNumber;

    private String isbn;

    private BookStatus status;

    public Book(BookId id, String title, Barcode inventoryNumber, String isbn, BookStatus status) {
        this.id = id;
        this.title = title;
        this.inventoryNumber = inventoryNumber;
        this.isbn = isbn;
        this.status = status;
    }

    public static Book createAvailableBook(String title, Barcode inventoryNumber, String isbn) {
        return new Book(new BookId(UUID.randomUUID()), title, inventoryNumber, isbn, BookStatus.AVAILABLE);
    }

    public static Book createOnHoldBook(UUID id, String title, Barcode inventoryNumber, String isbn) {
        return new Book(new BookId(id), title, inventoryNumber, isbn, BookStatus.ON_HOLD);
    }

    public Book markOnHold() {
        this.status = BookStatus.ON_HOLD;
        return this;
    }

    public record Barcode(String barcode) implements ValueObject {
    }

    public enum BookStatus implements ValueObject {
        AVAILABLE, ON_HOLD, ISSUED
    }

    public record BookId(UUID id) implements Identifier {
    }
}
