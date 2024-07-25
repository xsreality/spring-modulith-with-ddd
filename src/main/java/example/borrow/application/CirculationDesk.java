package example.borrow.application;

import org.jmolecules.architecture.hexagonal.Port;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import example.borrow.domain.Book;
import example.borrow.domain.Book.BookCheckedOut;
import example.borrow.domain.Book.BookPlacedOnHold;
import example.borrow.domain.BookRepository;
import example.borrow.domain.Hold;
import example.borrow.domain.HoldEventPublisher;
import example.borrow.domain.HoldRepository;
import example.catalog.BookAddedToCatalog;

@Port
@Service
@Transactional
public class CirculationDesk {

    private final BookRepository books;
    private final HoldRepository holds;
    private final HoldEventPublisher eventPublisher;

    public CirculationDesk(BookRepository books, HoldRepository holds, HoldEventPublisher eventPublisher) {
        this.books = books;
        this.holds = holds;
        this.eventPublisher = eventPublisher;
    }

    public HoldInformation placeHold(Hold.PlaceHold command) {
        books.findAvailableBook(command.inventoryNumber())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        var hold = Hold.placeHold(command)
                .then(holds::save)
                .then(eventPublisher::holdPlaced);

        return HoldInformation.from(hold);
    }

    public Optional<HoldInformation> locate(UUID holdId) {
        return holds.findById(new Hold.HoldId(holdId))
                .map(HoldInformation::from);
    }

    public CheckoutDto checkout(Hold.Checkout command) {
        var hold = holds.findById(command.holdId())
                .orElseThrow(() -> new IllegalArgumentException("Hold not found!"));

        if (!hold.isHeldBy(command.patronId())) {
            throw new IllegalArgumentException("Hold belongs to a different patron");
        }

        return CheckoutDto.from(
                hold.checkout(command)
                        .then(holds::save)
                        .then(eventPublisher::bookCheckedOut)
        );
    }

    @ApplicationModuleListener
    public void handle(BookPlacedOnHold event) {
        books.findAvailableBook(new Book.Barcode(event.inventoryNumber()))
                .map(Book::markOnHold)
                .map(books::save)
                .orElseThrow(() -> new IllegalArgumentException("Duplicate hold?"));
    }

    @ApplicationModuleListener
    public void handle(BookCheckedOut event) {
        books.findOnHoldBook(new Book.Barcode(event.inventoryNumber()))
                .map(Book::markCheckedOut)
                .map(books::save)
                .orElseThrow(() -> new IllegalArgumentException("Book not on hold?"));
    }

    @ApplicationModuleListener
    public void handle(BookAddedToCatalog event) {
        var command = new Book.AddBook(new Book.Barcode(event.inventoryNumber()), event.title(), event.isbn());
        books.save(Book.addBook(command));
    }
}
