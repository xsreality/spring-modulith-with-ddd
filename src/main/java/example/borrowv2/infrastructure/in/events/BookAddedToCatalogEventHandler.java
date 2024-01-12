package example.borrowv2.infrastructure.in.events;

import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import example.borrowv2.domain.service.BorrowServices;
import example.catalog.BookAddedToCatalog;
import lombok.RequiredArgsConstructor;

@PrimaryAdapter
@Component
@RequiredArgsConstructor
public class BookAddedToCatalogEventHandler {

    private final BorrowServices borrowServices;

    @ApplicationModuleListener
    public void on(BookAddedToCatalog event) {
        borrowServices.handle(event);
    }
}
