package example.borrow.infrastructure.out.events;

import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import example.borrow.domain.BookCheckedOut;
import example.borrow.domain.BookPlacedOnHold;
import example.borrow.domain.HoldEventPublisher;
import lombok.RequiredArgsConstructor;

@SecondaryAdapter
@Component
@RequiredArgsConstructor
public class BorrowEventsPublisher implements HoldEventPublisher {

    private final ApplicationEventPublisher publisher;

    @Override
    public void holdPlaced(BookPlacedOnHold event) {
        publisher.publishEvent(event);
    }

    @Override
    public void bookCheckedOut(BookCheckedOut event) {
        publisher.publishEvent(event);
    }
}
