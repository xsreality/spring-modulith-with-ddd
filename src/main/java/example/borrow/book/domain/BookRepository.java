package example.borrow.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import example.borrow.book.domain.Book.Barcode;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByInventoryNumber(Barcode inventoryNumber);
}
