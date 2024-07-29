package example.borrow.domain;

import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.ddd.types.ValueObject;

import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@NoArgsConstructor
@Table(name = "borrow_books", uniqueConstraints = @UniqueConstraint(columnNames = "barcode"))
@Getter
public class Book {

    @EmbeddedId
    private BookId id;

    @Embedded
    private Barcode inventoryNumber;

    private String title;

    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @SuppressWarnings("unused")
    @Version
    private Long version;

    private Book(AddBook addBook) {
        this.id = new BookId(UUID.randomUUID());
        this.inventoryNumber = addBook.barcode();
        this.title = addBook.title();
        this.isbn = addBook.isbn();
        this.status = BookStatus.AVAILABLE;
    }

    public static Book addBook(AddBook command) {
        return new Book(command);
    }

    public Book markOnHold() {
        this.status = BookStatus.ON_HOLD;
        return this;
    }

    public Book markCheckedOut() {
        this.status = BookStatus.ISSUED;
        return this;
    }

    public record BookId(UUID id) implements Identifier {
    }

    public record Barcode(String barcode) implements ValueObject {

        public static Barcode of(String barcode) {
            return new Barcode(barcode);
        }
    }

    public enum BookStatus implements ValueObject {
        AVAILABLE, ON_HOLD, ISSUED
    }

    /**
     * Command to add a new book
     */
    public record AddBook(Barcode barcode, String title, String isbn) {
    }

}
