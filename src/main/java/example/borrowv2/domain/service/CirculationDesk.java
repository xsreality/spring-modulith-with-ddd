package example.borrowv2.domain.service;

import java.time.LocalDate;
import java.util.Optional;

import example.borrowv2.config.TransactionalPort;
import example.borrowv2.domain.model.Book;
import example.borrowv2.domain.model.Book.Barcode;
import example.borrowv2.domain.model.Book.BookId;
import example.borrowv2.domain.model.BookPlacedOnHold;
import example.borrowv2.domain.model.Hold;
import example.borrowv2.domain.model.Hold.HoldId;
import example.borrowv2.domain.model.Patron.PatronId;
import example.catalog.BookAddedToCatalog;
import lombok.RequiredArgsConstructor;

@TransactionalPort
@RequiredArgsConstructor
public class CirculationDesk implements BorrowServices {

    private final BorrowRepository repository;
    private final HoldRepository holds;

    @Override
    public Hold placeHold(PatronId patronId, Barcode inventoryNumber) {
        var book = repository.findAvailableBook(inventoryNumber)
                .orElseThrow(() -> new IllegalArgumentException("Book is not available!"));

        var dateOfHold = LocalDate.now();
        var hold = new Hold(book.getId(), patronId, dateOfHold);
        holds.saveHold(hold);
        holds.publish(
                new BookPlacedOnHold(
                        book.getId().id(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getInventoryNumber().barcode(),
                        patronId.id(),
                        dateOfHold));
        return hold;
    }

    @Override
    public void checkout(PatronId patronId, Barcode inventoryNumber) {

    }

    @Override
    public void checkin(PatronId patronId, Barcode inventoryNumber) {

    }

    @Override
    public Optional<Hold> fetchHold(HoldId holdId) {
        return holds.findById(holdId);
    }

    @Override
    public void handle(BookAddedToCatalog event) {
        repository.saveBook(
                Book.createAvailableBook(event.title(), new Barcode(event.inventoryNumber()), event.isbn())
        );
    }

    @Override
    public void handle(BookPlacedOnHold event) {
        var book = repository.findById(new BookId(event.bookId()))
                .map(Book::markOnHold)
                .orElseThrow(() -> new IllegalArgumentException("Book is unavailable!"));
        repository.saveBook(book);
    }
}
