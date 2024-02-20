package example.borrow.application;

import java.time.LocalDate;

import example.borrow.domain.Hold;
import lombok.Getter;

@Getter
public class HoldDto {

    private final String id;
    private final String bookBarcode;
    private final LocalDate dateOfHold;

    private HoldDto(String id, String bookBarcode, LocalDate dateOfHold) {
        this.id = id;
        this.bookBarcode = bookBarcode;
        this.dateOfHold = dateOfHold;
    }

    public static HoldDto from(Hold hold) {
        return new HoldDto(
                hold.getId().id().toString(),
                hold.getOnBook().barcode(),
                hold.getDateOfHold());
    }
}
