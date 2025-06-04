package example.borrow.application;

import java.time.LocalDate;

import example.borrow.domain.Hold;
import lombok.Getter;

@Getter
public class HoldInformation {

    private final String id;
    private final String bookBarcode;
    private final String patronId;
    private final LocalDate dateOfHold;
    private final LocalDate dateOfCheckout;
    private final LocalDate dateOfCheckin;
    private final Hold.HoldStatus holdStatus;

    private HoldInformation(String id, String bookBarcode, String patronId, LocalDate dateOfHold, LocalDate dateOfCheckout, LocalDate dateOfCheckin, Hold.HoldStatus holdStatus) {
        this.id = id;
        this.bookBarcode = bookBarcode;
        this.patronId = patronId;
        this.dateOfHold = dateOfHold;
        this.dateOfCheckout = dateOfCheckout;
        this.dateOfCheckin = dateOfCheckin;
        this.holdStatus = holdStatus;
    }

    public static HoldInformation from(Hold hold) {
        return new HoldInformation(
                hold.getId().id().toString(),
                hold.getOnBook().barcode(),
                hold.getHeldBy().email(),
                hold.getDateOfHold(),
                hold.getDateOfCheckout(),
                hold.getDateOfCheckin(),
                hold.getStatus());
    }
}