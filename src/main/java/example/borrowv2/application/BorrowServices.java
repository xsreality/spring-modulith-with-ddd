package example.borrowv2.application;

import org.jmolecules.architecture.hexagonal.PrimaryPort;

import example.borrowv2.domain.Book.Barcode;
import example.borrowv2.domain.Hold;
import example.borrowv2.domain.Patron.PatronId;

@PrimaryPort
public interface BorrowServices {

    Hold placeHold(PatronId patronId, Barcode inventoryNumber);

    void checkout(PatronId patronId, Barcode inventoryNumber);

    void checkin(PatronId patronId, Barcode inventoryNumber);
}
