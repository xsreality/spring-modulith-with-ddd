package example.catalog.domain;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.data.domain.AbstractAggregateRoot;

import example.catalog.BookAddedToCatalog;
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

@SuppressWarnings("JpaDataSourceORMInspection")
@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
@Table(name = "catalog_books", uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class CatalogBook extends AbstractAggregateRoot<CatalogBook> {

    @Identity
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

    @SuppressWarnings("unused")
    @Version
    private Long version;

    public CatalogBook(String title, Barcode catalogNumber, String isbn, Author author) {
        this.title = title;
        this.catalogNumber = catalogNumber;
        this.isbn = isbn;
        this.author = author;
        this.registerEvent(new BookAddedToCatalog(title, catalogNumber.barcode(), isbn, author.name()));
    }

    public record Barcode(String barcode) {
    }

    public record Author(String name) {
    }
}
