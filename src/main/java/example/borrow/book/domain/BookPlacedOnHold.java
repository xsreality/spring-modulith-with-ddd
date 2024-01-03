package example.borrow.book.domain;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.LocalDate;

@DomainEvent
public record BookPlacedOnHold(Long bookId,
                               String isbn,
                               String inventoryNumber,
                               Long patronId,
                               LocalDate dateOfHold) {
}
