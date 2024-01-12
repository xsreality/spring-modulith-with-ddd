package example.borrowv2.domain.service;

import org.jmolecules.architecture.hexagonal.SecondaryPort;

import java.util.Optional;

import example.borrowv2.domain.model.Book;
import example.borrowv2.domain.model.Book.Barcode;
import example.borrowv2.domain.model.Book.BookId;

@SecondaryPort
public interface BorrowRepository {

    void saveBook(Book book);

    Optional<Book> findAvailableBook(Barcode inventoryNumber);

    Optional<Book> findById(BookId id);
}
