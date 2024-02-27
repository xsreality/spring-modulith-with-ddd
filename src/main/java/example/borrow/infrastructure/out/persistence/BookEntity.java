package example.borrow.infrastructure.out.persistence;

import org.jmolecules.ddd.annotation.Identity;

import java.util.UUID;

import example.borrow.domain.Book;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "borrow_books", uniqueConstraints = @UniqueConstraint(columnNames = "barcode"))
public class BookEntity {

    @Identity
    @Id
    private UUID id;

    @Embedded
    private Book.Barcode inventoryNumber;

    private String title;

    private String isbn;

    @Enumerated(EnumType.STRING)
    private Book.BookStatus status;

    @Version
    private Long version;

    public Book toDomain() {
        return Book.toBook(new Book.BookId(id), inventoryNumber, title, isbn, status);
    }

    public static BookEntity fromDomain(Book book) {
        var entity = new BookEntity();
        entity.id = book.getId().id();
        entity.inventoryNumber = book.getInventoryNumber();
        entity.title = book.getTitle();
        entity.isbn = book.getIsbn();
        entity.status = book.getStatus();
        entity.version = 0L;
        return entity;
    }
}
