package example.borrowv2.infrastructure.out.persistence;

import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import example.borrowv2.domain.model.Book;
import example.borrowv2.domain.model.Book.Barcode;
import example.borrowv2.domain.model.Book.BookId;
import example.borrowv2.domain.model.Book.BookStatus;
import example.borrowv2.domain.service.BorrowRepository;
import example.borrowv2.infrastructure.out.persistence.entity.BookEntity;
import lombok.RequiredArgsConstructor;

@SecondaryAdapter
@Transactional
@Component
@RequiredArgsConstructor
public class BorrowRepositoryAdapter implements BorrowRepository {

    private final BookJpaRepository books;

    @Override
    public void saveBook(Book book) {
        books.save(BookEntity.fromDomain(book));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findAvailableBook(Barcode inventoryNumber) {
        return books.findByInventoryNumberAndStatus(inventoryNumber, BookStatus.AVAILABLE)
                .map(BookEntity::toDomain);
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return books.findById(id.id()).map(BookEntity::toDomain);
    }
}
