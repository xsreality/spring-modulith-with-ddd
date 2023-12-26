package example.catalog.internal;

public record BookDto(Long id, String title, CatalogBook.Barcode catalogNumber,
                      String isbn, CatalogBook.Author author) {
}
