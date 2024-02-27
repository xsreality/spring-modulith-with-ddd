package example.borrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import example.borrow.application.CirculationDesk;
import example.borrow.domain.Book;
import example.borrow.domain.BookPlacedOnHold;
import example.borrow.domain.BookRepository;
import example.borrow.domain.Hold;
import example.borrow.domain.HoldEventPublisher;
import example.borrow.domain.HoldRepository;
import example.borrow.domain.Patron.PatronId;

import static example.borrow.domain.Book.BookStatus.AVAILABLE;
import static example.borrow.domain.Book.BookStatus.ON_HOLD;
import static org.assertj.core.api.Assertions.assertThat;

class CirculationDeskTest {

    CirculationDesk circulationDesk;

    BookRepository bookRepository;

    HoldRepository holdRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new InMemoryBooks();
        holdRepository = new InMemoryHolds();
        circulationDesk = new CirculationDesk(bookRepository, holdRepository, new InMemoryHoldsEventPublisher());
    }

    @Test
    void patronCanPlaceHold() {
        var command = new Hold.PlaceHold(new Book.Barcode("12345"), LocalDate.now(), new PatronId(UUID.randomUUID()));
        var holdDto = circulationDesk.placeHold(command);
        assertThat(holdDto.getBookBarcode()).isEqualTo("12345");
        assertThat(holdDto.getDateOfHold()).isNotNull();
    }

    @Test
    void bookStatusUpdatedWhenPlacedOnHold() {
        var command = new Hold.PlaceHold(new Book.Barcode("12345"), LocalDate.now(), new PatronId(UUID.randomUUID()));
        var hold = Hold.placeHold(command);
        circulationDesk.handle(new BookPlacedOnHold(hold.getId().id(), hold.getOnBook().barcode(), hold.getDateOfHold()));
        //noinspection OptionalGetWithoutIsPresent
        var book = bookRepository.findByBarcode("12345").get();
        assertThat(book.getStatus()).isEqualTo(ON_HOLD);
    }

//    @Test
//    void patronCanCheckoutBook() {
//        var command = new Hold.PlaceHold("12345", LocalDate.now());
//        var hold = circulationDesk.placeHold(command);
//        var checkout = circulationDesk.checkout(new Checkout.CheckoutBook(hold.getId(), hold.getBarcode(), LocalDate.now()));
//        assertThat(checkout.getBarcode()).isEqualTo("12345");
//        assertThat(checkout.getHoldId()).isEqualTo(hold.getId());
//        assertThat(checkout.getDateOfCheckout()).isNotNull();
//    }

//    @Test
//    void bookStatusUpdatedWhenCheckedOut() {
//        // place on hold
//        var command = new Hold.PlaceHold("12345", LocalDate.now());
//        var hold = circulationDesk.placeHold(command);
//        // publish event
//        circulationDesk.handle(new Hold.HoldPlaced(hold));
//
//        // checkout book
//        var checkout = circulationDesk.checkout(new Checkout.CheckoutBook(hold.getId(), hold.getBarcode(), LocalDate.now()));
//        // publish event
//        circulationDesk.handle(new Checkout.BookCheckedOut(checkout));
//
//        var book = bookRepository.findByBarcode("12345").get();
//        assertThat(book.getStatus()).isEqualTo(ISSUED);
//    }
}

class InMemoryBooks implements BookRepository {

    private final Map<String, Book> books = new HashMap<>();

    public InMemoryBooks() {
        var booksToAdd = List.of(
                Book.addBook(new Book.AddBook(new Book.Barcode("12345"), "A famous book", "92972947199")),
                Book.addBook(new Book.AddBook(new Book.Barcode("98765"), "Another famous book", "98137674132"))
        );
        booksToAdd.forEach(book -> books.put(book.getInventoryNumber().barcode(), book));
    }

    @Override
    public Optional<Book> findAvailableBook(Book.Barcode barcode) {
        return books.values()
                .stream()
                .filter(it -> it.getInventoryNumber().equals(barcode))
                .filter(it -> it.getStatus().equals(AVAILABLE))
                .findFirst();
    }

    @Override
    public Optional<Book> findOnHoldBook(Book.Barcode barcode) {
        return books.values()
                .stream()
                .filter(it -> it.getInventoryNumber().equals(barcode))
                .filter(it -> it.getStatus().equals(ON_HOLD))
                .findFirst();
    }

    @Override
    public Book save(Book book) {
        books.put(book.getInventoryNumber().barcode(), book);
        return book;
    }

    @Override
    public Optional<Book> findByBarcode(String barcode) {
        return books.values()
                .stream()
                .filter(it -> it.getInventoryNumber().barcode().equals(barcode))
                .findFirst();
    }
}

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class InMemoryHolds implements HoldRepository {

    private final Map<UUID, Hold> holds = new HashMap<>();
//    private final Map<UUID, Checkout> checkouts = new HashMap<>();

    public InMemoryHolds() {
        var hold = Hold.placeHold(new Hold.PlaceHold(new Book.Barcode("98765"), LocalDate.now(), new PatronId(UUID.randomUUID())));
        holds.put(hold.getId().id(), hold);
    }

    @Override
    public Hold save(Hold hold) {
        holds.put(hold.getId().id(), hold);
        return hold;
    }

//    @Override
//    public Checkout save(Checkout checkout) {
//        checkouts.put(checkout.getHoldId().id(), checkout);
//        return checkout;
//    }

    @Override
    public Optional<Hold> findById(Hold.HoldId id) {
        return Optional.ofNullable(holds.get(id.id()));
    }

    @Override
    public List<Hold> activeHolds() {
        return holds.values().stream().toList();
    }

//    @Override
//    public List<Checkout> checkouts() {
//        return checkouts.values().stream().toList();
//    }
}

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
class InMemoryHoldsEventPublisher implements HoldEventPublisher {

    private final List<BookPlacedOnHold> events = new LinkedList<>();

    @Override
    public void holdPlaced(BookPlacedOnHold event) {
        events.add(event);
    }

//    @Override
//    public void bookCheckedOut(Checkout.BookCheckedOut event) {
//        // NOOP
//    }
}
