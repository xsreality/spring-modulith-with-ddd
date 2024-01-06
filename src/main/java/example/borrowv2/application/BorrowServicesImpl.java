package example.borrowv2.application;

import java.time.LocalDate;

import example.borrowv2.domain.Book.Barcode;
import example.borrowv2.domain.BookPlacedOnHold;
import example.borrowv2.domain.Hold;
import example.borrowv2.domain.Patron.PatronId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BorrowServicesImpl implements BorrowServices {

    private final BorrowRepository repository;

    @Override

    public Hold placeHold(PatronId patronId, Barcode inventoryNumber) {
        var book = repository.findAvailableBook(inventoryNumber)
                .orElseThrow(() -> new IllegalStateException("Book is not available!"));

        var dateOfHold = LocalDate.now();
        var hold = new Hold(book.getId(), patronId, dateOfHold);
        repository.saveHold(hold);
        repository.publish(
                new BookPlacedOnHold(
                        book.getId().id().toString(),
                        book.getIsbn(),
                        book.getInventoryNumber().barcode(),
                        patronId.id().toString(),
                        dateOfHold));
        return hold;
    }

    @Override
    public void checkout(PatronId patronId, Barcode inventoryNumber) {

    }

    @Override
    public void checkin(PatronId patronId, Barcode inventoryNumber) {

    }
}
