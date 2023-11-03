package com.abhinav.app.inventory;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"barcode"}))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Embedded
    private Barcode inventoryNumber;

    private String isbn;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "author"))
    private Author author;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public boolean isAvailable() {
        return BookStatus.AVAILABLE.equals(this.status);
    }

    public boolean isIssued() {
        return BookStatus.ISSUED.equals(this.status);
    }

    public Book(String title, Barcode inventoryNumber, String isbn, Author author) {
        this.title = title;
        this.inventoryNumber = inventoryNumber;
        this.isbn = isbn;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    public Book markIssued() {
        if (this.status.equals(BookStatus.ISSUED)) {
            throw new IllegalStateException("Book is already issued!");
        }
        this.status = BookStatus.ISSUED;
        return this;
    }

    public Book markAvailable() {
        this.status = BookStatus.AVAILABLE;
        return this;
    }

    public record Barcode(String barcode) {
    }

    public record Author(String name) {
    }

    public enum BookStatus {
        AVAILABLE, ISSUED
    }
}
