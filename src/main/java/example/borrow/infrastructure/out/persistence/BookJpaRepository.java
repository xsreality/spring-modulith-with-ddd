package example.borrow.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import example.borrow.domain.Book;

public interface BookJpaRepository extends JpaRepository<BookEntity, UUID> {

    Optional<BookEntity> findByInventoryNumber(Book.Barcode inventoryNumber);

    Optional<BookEntity> findByInventoryNumberAndStatus(Book.Barcode inventoryNumber, Book.BookStatus status);
}
