package example.borrowv2.application;

import org.jmolecules.architecture.hexagonal.SecondaryPort;

import java.util.Optional;

import example.borrowv2.domain.Book;
import example.borrowv2.domain.Book.Barcode;
import example.borrowv2.domain.BookPlacedOnHold;
import example.borrowv2.domain.Hold;

@SecondaryPort
public interface BorrowRepository {

    Book saveBook(Book book);

    Hold saveHold(Hold hold);

    Optional<Book> findAvailableBook(Barcode inventoryNumber);

    void publish(BookPlacedOnHold event);
}
