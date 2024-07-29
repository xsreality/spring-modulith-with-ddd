package example.borrow.domain;

import org.jmolecules.architecture.hexagonal.SecondaryPort;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Repository
@SecondaryPort
public interface BookRepository extends CrudRepository<Book, Book.BookId> {

    @Query("""
            SELECT b FROM Book b WHERE b.inventoryNumber = :inventoryNumber AND b.status = 'AVAILABLE'
            """)
    Optional<Book> findAvailableBook(Book.Barcode inventoryNumber);

    @Query("""
            SELECT b FROM Book b WHERE b.inventoryNumber = :inventoryNumber AND b.status = 'ON_HOLD'
            """)
    Optional<Book> findOnHoldBook(Book.Barcode inventoryNumber);

    Optional<Book> findByInventoryNumber(Book.Barcode inventoryNumber);

}
