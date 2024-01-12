package example.borrowv2.infrastructure.out.persistence.entity;

import java.util.UUID;

import example.borrowv2.domain.model.Book;
import example.borrowv2.domain.model.Book.Barcode;
import example.borrowv2.domain.model.Book.BookId;
import example.borrowv2.domain.model.Book.BookStatus;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class BookEntity {

    @Id
    private UUID id;

    private String title;

    @Embedded
    private Barcode inventoryNumber;

    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Version
    private Long version;

    public BookEntity markOnHold() {
        this.status = BookStatus.ON_HOLD;
        return this;
    }

    public Book toDomain() {
        return new Book(new BookId(id), title, inventoryNumber, isbn, status);
    }

    public static BookEntity fromDomain(Book book) {
        var entity = new BookEntity();
        entity.id = book.getId().id();
        entity.title = book.getTitle();
        entity.inventoryNumber = book.getInventoryNumber();
        entity.isbn = book.getIsbn();
        entity.status = book.getStatus();
        entity.version = 0L;
        return entity;
    }
}
