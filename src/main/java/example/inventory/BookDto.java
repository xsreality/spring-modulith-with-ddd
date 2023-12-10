package example.inventory;

public record BookDto(Long id, String title, Book.Barcode inventoryNumber,
                      String isbn, Book.Author author, Book.BookStatus status) {
}
