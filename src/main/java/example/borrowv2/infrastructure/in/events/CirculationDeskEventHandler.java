package example.borrowv2.infrastructure.in.events;

import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import example.borrowv2.domain.model.Book;
import example.borrowv2.domain.model.Book.BookId;
import example.borrowv2.domain.model.BookPlacedOnHold;
import example.borrowv2.domain.service.BorrowRepository;
import example.borrowv2.domain.service.BorrowServices;
import lombok.RequiredArgsConstructor;

@Transactional
@PrimaryAdapter
@Component
@RequiredArgsConstructor
public class CirculationDeskEventHandler {

    private final BorrowServices borrowServices;

    @ApplicationModuleListener
    public void on(BookPlacedOnHold event) {
        borrowServices.handle(event);
    }
}
