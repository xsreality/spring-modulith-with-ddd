package example.borrowv2.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import example.borrowv2.domain.model.Book;
import example.borrowv2.infrastructure.out.persistence.entity.BookEntity;

public interface BookJpaRepository extends JpaRepository<BookEntity, UUID> {

    Optional<BookEntity> findByInventoryNumberAndStatus(Book.Barcode inventoryNumber, Book.BookStatus status);
}
