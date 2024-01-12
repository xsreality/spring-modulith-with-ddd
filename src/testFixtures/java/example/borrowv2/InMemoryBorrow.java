package example.borrowv2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import example.borrowv2.domain.model.Book;
import example.borrowv2.domain.model.Book.Barcode;
import example.borrowv2.domain.model.Book.BookId;
import example.borrowv2.domain.model.Hold;
import example.borrowv2.domain.service.BorrowRepository;

public class InMemoryBorrow implements BorrowRepository {

    private final Map<UUID, Book> books = new HashMap<>();
    private final Map<UUID, Hold> holds = new HashMap<>();

    public InMemoryBorrow() {
        var sapiens = Book.createAvailableBook("Sapiens", new Barcode("12345"), "9780062316097");
        books.put(sapiens.getId().id(), sapiens);
    }

    @Override
    public void saveBook(Book book) {
        books.put(book.getId().id(), book);
    }

    @Override
    public Optional<Book> findAvailableBook(Barcode inventoryNumber) {
        return books.values()
                .stream()
                .filter(it -> it.getInventoryNumber().equals(inventoryNumber))
                .findFirst();
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return Optional.of(books.get(id.id()));
    }
}
