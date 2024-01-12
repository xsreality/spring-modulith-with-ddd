package example.borrowv2.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import example.borrowv2.InMemoryBorrow;
import example.borrowv2.InMemoryHolds;
import example.borrowv2.domain.model.Book;
import example.borrowv2.domain.model.Book.Barcode;
import example.borrowv2.domain.model.Book.BookStatus;
import example.borrowv2.domain.model.BookPlacedOnHold;
import example.borrowv2.domain.model.Hold;
import example.borrowv2.domain.model.Patron;
import example.borrowv2.domain.model.Patron.PatronId;
import example.borrowv2.domain.service.BorrowRepository;
import example.borrowv2.domain.service.BorrowServices;
import example.borrowv2.domain.service.CirculationDesk;
import example.borrowv2.domain.service.HoldRepository;
import example.catalog.BookAddedToCatalog;

import static org.assertj.core.api.Assertions.assertThat;

public class CirculationDeskTest {

    private BorrowRepository repository;
    private HoldRepository holds;
    private BorrowServices services;

    @BeforeEach
    void setup() {
        repository = new InMemoryBorrow();
        holds = new InMemoryHolds();
        services = new CirculationDesk(repository, holds);
    }

    @Test
    void patronCanPlaceHold() {
        var patronId = new PatronId(UUID.randomUUID());
        var hold = services.placeHold(patronId, new Barcode("12345"));
        assertThat(hold.getStatus()).isEqualTo(Hold.HoldStatus.HOLDING);
        assertThat(hold.getHeldBy().pointsTo(patronId)).isTrue();
//        assertThat(hold.getOnBook().pointsTo(repository.findAvailableBook(new Barcode("12345")).get())).isTrue();
    }

    @Test
    void shouldCreateAvailableBookWhenBookAddedToCatalog() {
        services.handle(new BookAddedToCatalog("A title", "98765", "12173219", "An author"));
        Optional<Book> book = repository.findAvailableBook(new Barcode("98765"));
        assertThat(book).isNotEmpty();
        assertThat(book.get().getTitle()).isEqualTo("A title");
        assertThat(book.get().getIsbn()).isEqualTo("12173219");
    }

    @Test
    void shouldMarkBookOnHoldWhenPlacedOnHold() {
        UUID bookId = UUID.randomUUID();
        services.handle(new BookPlacedOnHold(bookId, "A title", "12173219", "12345", UUID.randomUUID(), LocalDate.now()));
        Optional<Book> book = repository.findById(new Book.BookId(bookId));
        assertThat(book).isNotEmpty();
        assertThat(book.get().getStatus()).isEqualTo(BookStatus.ON_HOLD);
    }
}
