package example.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    Optional<Book> findByInventoryNumber(Book.Barcode inventoryNumber);

    List<Book> findByStatus(Book.BookStatus status);
}
