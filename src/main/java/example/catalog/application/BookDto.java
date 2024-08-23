package example.catalog.application;

import example.catalog.domain.CatalogBook;

public record BookDto(Long id, String title, CatalogBook.Barcode catalogNumber,
                      String isbn, CatalogBook.Author author) {

    public static BookDto from(CatalogBook book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getCatalogNumber(),
                book.getIsbn(),
                book.getAuthor()
        );
    }
}
