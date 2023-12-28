package example.catalog.internal;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
@Table(name = "catalog_books", uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class CatalogBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Embedded
    private Barcode catalogNumber;

    private String isbn;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "author"))
    private Author author;

    @Version
    private Long version;

    public CatalogBook(String title, Barcode catalogNumber, String isbn, Author author) {
        this.title = title;
        this.catalogNumber = catalogNumber;
        this.isbn = isbn;
        this.author = author;
    }

    public record Barcode(String barcode) {
    }

    public record Author(String name) {
    }
}
