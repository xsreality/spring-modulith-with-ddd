package example.borrow.book;

import java.time.LocalDate;

public record BookPlacedOnHold(Long bookId,
                               String isbn,
                               String inventoryNumber,
                               Long patronId,
                               LocalDate dateOfHold) {
}
