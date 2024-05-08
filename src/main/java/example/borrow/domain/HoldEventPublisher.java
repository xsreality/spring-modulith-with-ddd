package example.borrow.domain;

import org.jmolecules.architecture.hexagonal.SecondaryPort;

@SecondaryPort
public interface HoldEventPublisher {

    void holdPlaced(BookPlacedOnHold event);

    default Hold holdPlaced(Hold hold) {
        BookPlacedOnHold event = new BookPlacedOnHold(hold.getId().id(), hold.getOnBook().barcode(), hold.getDateOfHold());
        this.holdPlaced(event);
        return hold;
    }

    void bookCheckedOut(BookCheckedOut event);

    default Hold bookCheckedOut(Hold hold) {
        BookCheckedOut event = new BookCheckedOut(hold.getId().id(), hold.getOnBook().barcode(), hold.getDateOfCheckout());
        this.bookCheckedOut(event);
        return hold;
    }
}
