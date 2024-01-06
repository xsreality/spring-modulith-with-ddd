package example.borrowv2.domain;

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

    public Book(String title, Barcode inventoryNumber, String isbn, BookStatus status) {
        this.title = title;
        this.inventoryNumber = inventoryNumber;
        this.isbn = isbn;
        this.status = status;
    }

    public record Barcode(String barcode) implements ValueObject {
    }

    public enum BookStatus implements ValueObject {
        AVAILABLE, ON_HOLD, ISSUED
    }

    public record BookId(UUID id) implements Identifier {
    }
}
