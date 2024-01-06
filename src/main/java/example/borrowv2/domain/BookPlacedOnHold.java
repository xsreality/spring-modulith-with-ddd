package example.borrowv2.domain;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.LocalDate;

@DomainEvent
public record BookPlacedOnHold(String bookId,
                               String isbn,
                               String inventoryNumber,
                               String patronId,
                               LocalDate dateOfHold) {
}
