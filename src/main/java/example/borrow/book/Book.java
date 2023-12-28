package example.borrow.book;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "borrow_books", uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Embedded
    private Barcode inventoryNumber;

    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Version
    private Long version;

    public record Barcode(String barcode) {
    }

    public boolean available() {
        return BookStatus.AVAILABLE.equals(this.status);
    }

    public boolean onHold() {
        return BookStatus.ON_HOLD.equals(this.status);
    }

    public boolean issued() {
        return BookStatus.ISSUED.equals(this.status);
    }

    public Book(String title, Barcode inventoryNumber, String isbn) {
        this.title = title;
        this.inventoryNumber = inventoryNumber;
        this.isbn = isbn;
        this.status = BookStatus.AVAILABLE;
    }

    public Book markIssued() {
        if (issued()) {
            throw new IllegalStateException("Book is already issued!");
        }
        this.status = BookStatus.ISSUED;
        return this;
    }

    public Book markOnHold() {
        if (onHold()) {
            throw new IllegalStateException("Book is already available!");
        }
        this.status = BookStatus.ON_HOLD;
        return this;
    }

    public Book markAvailable() {
        if (available()) {
            throw new IllegalStateException("Book is already available!");
        }
        this.status = BookStatus.AVAILABLE;
        return this;
    }

    public enum BookStatus {
        AVAILABLE, ON_HOLD, ISSUED
    }
}
