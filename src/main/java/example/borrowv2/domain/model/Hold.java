package example.borrowv2.domain.model;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.ddd.types.ValueObject;

import java.time.LocalDate;
import java.util.UUID;

import example.borrowv2.domain.model.Book.BookId;
import example.borrowv2.domain.model.Patron.PatronId;
import lombok.Getter;

@Getter
public class Hold implements AggregateRoot<Hold, Hold.HoldId> {

    private HoldId id;

    private Association<Book, BookId> onBook;

    private Association<Patron, PatronId> heldBy;

    private LocalDate dateOfCheckout;

    private LocalDate dateOfHold;

    private int holdDurationInDays;

    private int loanDurationInDays;

    private LocalDate dateOfCheckin;

    private HoldStatus status;

    public Hold(BookId bookId, PatronId patronId, LocalDate dateOfHold, LocalDate dateOfCheckout, int holdDurationInDays, int loanDurationInDays, LocalDate dateOfCheckin, HoldStatus status) {
        this.id = new HoldId(UUID.randomUUID());
        this.onBook = Association.forId(bookId);
        this.heldBy = Association.forId(patronId);
        this.dateOfCheckout = dateOfCheckout;
        this.dateOfHold = dateOfHold;
        this.holdDurationInDays = holdDurationInDays;
        this.loanDurationInDays = loanDurationInDays;
        this.dateOfCheckin = dateOfCheckin;
        this.status = status;
    }

    public Hold(BookId bookId, PatronId patronId, LocalDate dateOfHold) {
        this(bookId, patronId, dateOfHold, null, 3, 0, null, HoldStatus.HOLDING);
    }

    public enum HoldStatus implements ValueObject {
        HOLDING, ACTIVE, OVERDUE, COMPLETED
    }

    public record HoldId(UUID id) implements Identifier {
    }
}
