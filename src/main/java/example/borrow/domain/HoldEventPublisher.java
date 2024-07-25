package example.borrow.domain;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import example.borrow.domain.Book.BookCheckedOut;
import example.borrow.domain.Book.BookPlacedOnHold;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HoldEventPublisher {

    private final ApplicationEventPublisher publisher;

    public Hold holdPlaced(Hold hold) {
        BookPlacedOnHold event = new BookPlacedOnHold(hold.getId().id(), hold.getOnBook().barcode(), hold.getDateOfHold());
        publisher.publishEvent(event);
        return hold;
    }

    public Hold bookCheckedOut(Hold hold) {
        BookCheckedOut event = new BookCheckedOut(hold.getId().id(), hold.getOnBook().barcode(), hold.getDateOfCheckout());
        publisher.publishEvent(event);
        return hold;
    }
}
