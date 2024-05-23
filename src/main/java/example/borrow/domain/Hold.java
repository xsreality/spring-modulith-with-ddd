package example.borrow.domain;

import org.jmolecules.ddd.types.Identifier;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.UnaryOperator;

import example.borrow.domain.Patron.PatronId;
import lombok.Getter;

@Getter
public class Hold {

    private final HoldId id;

    private final Book.Barcode onBook;

    private final PatronId heldBy;

    private final LocalDate dateOfHold;

    private LocalDate dateOfCheckout;

    private Hold(PlaceHold placeHold) {
        this.id = new HoldId(UUID.randomUUID());
        this.onBook = placeHold.inventoryNumber();
        this.dateOfHold = placeHold.dateOfHold();
        this.heldBy = placeHold.patronId();
    }

    public Hold(HoldId id, Book.Barcode onBook, PatronId heldBy, LocalDate dateOfHold, LocalDate dateOfCheckout) {
        this.id = id;
        this.onBook = onBook;
        this.heldBy = heldBy;
        this.dateOfHold = dateOfHold;
        this.dateOfCheckout = dateOfCheckout;
    }

    public static Hold placeHold(PlaceHold command) {
        return new Hold(command);
    }

    public Hold checkout(Checkout command) {
        this.dateOfCheckout = command.dateOfCheckout();
        return this;
    }

    public Hold then(UnaryOperator<Hold> function) {
        return function.apply(this);
    }

    public boolean isHeldBy(PatronId patronId) {
        return this.heldBy.equals(patronId);
    }

    public record HoldId(UUID id) implements Identifier {
    }

    public record PlaceHold(Book.Barcode inventoryNumber, LocalDate dateOfHold, PatronId patronId) {
    }

    public record Checkout(HoldId holdId, LocalDate dateOfCheckout, PatronId patronId) {

    }
}
