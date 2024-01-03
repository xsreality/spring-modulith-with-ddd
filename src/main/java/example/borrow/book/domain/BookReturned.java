package example.borrow.book.domain;

import org.jmolecules.event.annotation.DomainEvent;

import java.time.LocalDate;

@DomainEvent
public record BookReturned(Long bookId,
                           String isbn,
                           String inventoryNumber,
                           Long patronId,
                           LocalDate dateOfReturn) {
}
