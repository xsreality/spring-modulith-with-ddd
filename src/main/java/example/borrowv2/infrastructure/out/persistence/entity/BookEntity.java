package example.borrowv2.infrastructure.out.persistence.entity;

import java.util.UUID;

import example.borrowv2.domain.Book;
import example.borrowv2.domain.Book.Barcode;
import example.borrowv2.domain.Book.BookStatus;
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

    public Book toDomain() {
        return new Book(title, inventoryNumber, isbn, status);
    }

    public static BookEntity fromDomain(Book book) {
        var entity = new BookEntity();
        entity.id = book.getId().id();
        entity.title = book.getTitle();
        entity.inventoryNumber = book.getInventoryNumber();
        entity.isbn = book.getIsbn();
        entity.status = book.getStatus();
        return entity;
    }
}
