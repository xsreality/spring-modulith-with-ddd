package example.borrowv2.infrastructure.out.persistence;

import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Optional;

import example.borrowv2.application.BorrowRepository;
import example.borrowv2.domain.Book;
import example.borrowv2.domain.Book.Barcode;
import example.borrowv2.domain.BookPlacedOnHold;
import example.borrowv2.domain.Hold;
import example.borrowv2.infrastructure.out.persistence.entity.BookEntity;
import example.borrowv2.infrastructure.out.persistence.entity.HoldEntity;
import lombok.RequiredArgsConstructor;

@SecondaryAdapter
@Component
@RequiredArgsConstructor
public class BorrowRepositoryAdapter implements BorrowRepository {

    private final BookJpaRepository books;
    private final HoldJpaRepository holds;
    private final ApplicationEventPublisher events;

    @Override
    public Book saveBook(Book book) {
        return books.save(BookEntity.fromDomain(book))
                .toDomain();
    }

    @Override
    public Hold saveHold(Hold hold) {
        return holds.save(HoldEntity.fromDomain(hold))
                .toDomain();
    }

    @Override
    public Optional<Book> findAvailableBook(Barcode inventoryNumber) {
        return books.findByInventoryNumberAndStatus(inventoryNumber, Book.BookStatus.AVAILABLE)
                .map(BookEntity::toDomain);
    }

    @Override
    public void publish(BookPlacedOnHold event) {
        events.publishEvent(event);
    }
}
