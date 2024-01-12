package example.borrowv2.domain.model;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.LocalDate;
import java.util.UUID;

@DomainEvent
public record BookPlacedOnHold(UUID bookId,
                               String title,
                               String isbn,
                               String inventoryNumber,
                               UUID patronId,
                               LocalDate dateOfHold) {
}
