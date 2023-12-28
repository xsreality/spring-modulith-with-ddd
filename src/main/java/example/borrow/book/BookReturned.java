package example.borrow.book;

import java.time.LocalDate;

public record BookReturned(Long bookId,
                           String isbn,
                           String inventoryNumber,
                           Long patronId,
                           LocalDate dateOfReturn) {
}
