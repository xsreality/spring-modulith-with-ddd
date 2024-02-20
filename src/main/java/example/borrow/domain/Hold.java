package example.borrow.domain;

import org.jmolecules.ddd.types.Identifier;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.UnaryOperator;

import lombok.Getter;

@Getter
public class Hold {

    private final HoldId id;

    private final Book.Barcode onBook;

//    private final PatronId heldBy;

    private final LocalDate dateOfHold;

//    private int holdDurationInDays;

    private Hold(PlaceHold placeHold) {
        this.id = new HoldId(UUID.randomUUID());
        this.onBook = placeHold.inventoryNumber();
        this.dateOfHold = placeHold.dateOfHold();
    }

    public static Hold placeHold(PlaceHold command) {
        return new Hold(command);
    }

    public Hold then(UnaryOperator<Hold> function) {
        return function.apply(this);
    }

    public record HoldId(UUID id) implements Identifier {
    }

    public record PlaceHold(Book.Barcode inventoryNumber, LocalDate dateOfHold) {
    }
}
