package example.borrow.book;

import java.time.LocalDate;

public record BookCollected(Long bookId,
                            String isbn,
                            String inventoryNumber,
                            Long patronId,
                            LocalDate dateOfCheckout) {
}
