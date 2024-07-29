package example.borrow.domain;

import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.UnaryOperator;

import example.borrow.domain.Patron.PatronId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@NoArgsConstructor
@Table(name = "borrow_holds")
@Getter
public class Hold extends AbstractAggregateRoot<Hold> {

    @EmbeddedId
    private HoldId id;

    @Embedded
    @AttributeOverride(name = "barcode", column = @Column(name = "book_barcode"))
    private Book.Barcode onBook;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "patron_id"))
    private PatronId heldBy;

    private LocalDate dateOfHold;

    private LocalDate dateOfCheckout;

    @Enumerated(EnumType.STRING)
    private HoldStatus status;

    @SuppressWarnings("unused")
    @Version
    private Long version;

    private Hold(PlaceHold placeHold) {
        this.id = new HoldId(UUID.randomUUID());
        this.onBook = placeHold.inventoryNumber();
        this.dateOfHold = placeHold.dateOfHold();
        this.heldBy = placeHold.patronId();
        this.status = HoldStatus.HOLDING;
        this.registerEvent(new BookPlacedOnHold(id.id(), onBook.barcode(), dateOfHold));
    }

    public static Hold placeHold(PlaceHold command) {
        return new Hold(command);
    }

    public Hold checkout(Checkout command) {
        this.dateOfCheckout = command.dateOfCheckout();
        this.registerEvent(new BookCheckedOut(id.id(), onBook.barcode(), dateOfCheckout));
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

    public enum HoldStatus {
        HOLDING, ACTIVE, COMPLETED
    }

    ///
    // Commands
    ///

    public record PlaceHold(Book.Barcode inventoryNumber, LocalDate dateOfHold, PatronId patronId) {
    }

    public record Checkout(HoldId holdId, LocalDate dateOfCheckout, PatronId patronId) {

    }

    ///
    // Events
    ///

    @DomainEvent
    public record BookCheckedOut(UUID holdId,
                                 String inventoryNumber,
                                 LocalDate dateOfCheckout) {
    }

    @DomainEvent
    public record BookPlacedOnHold(UUID holdId,
                                   String inventoryNumber,
                                   LocalDate dateOfHold) {
    }
}
