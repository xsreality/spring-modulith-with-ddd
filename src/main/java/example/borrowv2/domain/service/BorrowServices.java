package example.borrowv2.domain.service;

import org.jmolecules.architecture.hexagonal.PrimaryPort;

import java.util.Optional;

import example.borrowv2.domain.model.Book.Barcode;
import example.borrowv2.domain.model.BookPlacedOnHold;
import example.borrowv2.domain.model.Hold;
import example.borrowv2.domain.model.Hold.HoldId;
import example.borrowv2.domain.model.Patron.PatronId;
import example.catalog.BookAddedToCatalog;

@PrimaryPort
public interface BorrowServices {

    Hold placeHold(PatronId patronId, Barcode inventoryNumber);

    void checkout(PatronId patronId, Barcode inventoryNumber);

    void checkin(PatronId patronId, Barcode inventoryNumber);

    Optional<Hold> fetchHold(HoldId holdId);

    void handle(BookAddedToCatalog event);

    void handle(BookPlacedOnHold event);
}
