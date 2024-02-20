package example.borrow.infrastructure.out.persistence;

import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import example.borrow.domain.Book;
import example.borrow.domain.BookRepository;
import lombok.RequiredArgsConstructor;

@SecondaryAdapter
@Component
@Transactional
@RequiredArgsConstructor
public class BookRepositoryAdapter implements BookRepository {

    private final BookJpaRepository books;

    @Override
    public Optional<Book> findAvailableBook(Book.Barcode inventoryNumber) {
        return books.findByInventoryNumberAndStatus(inventoryNumber, Book.BookStatus.AVAILABLE)
                .map(BookEntity::toDomain);
    }

    @Override
    public Optional<Book> findOnHoldBook(Book.Barcode inventoryNumber) {
        return books.findByInventoryNumberAndStatus(inventoryNumber, Book.BookStatus.ON_HOLD)
                .map(BookEntity::toDomain);
    }

    @Override
    public Book save(Book book) {
        books.save(BookEntity.fromDomain(book));
        return book;
    }

    @Override
    public Optional<Book> findByBarcode(String barcode) {
        return books.findByInventoryNumber(new Book.Barcode(barcode))
                .map(BookEntity::toDomain);
    }
}
