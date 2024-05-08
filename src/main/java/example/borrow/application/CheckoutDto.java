package example.borrow.application;

import java.time.LocalDate;

import example.borrow.domain.Hold;
import lombok.Getter;

@Getter
public class CheckoutDto {

    private final String holdId;
    private final String patronId;
    private final LocalDate dateOfCheckout;

    public CheckoutDto(String holdId, String patronId, LocalDate dateOfCheckout) {
        this.holdId = holdId;
        this.patronId = patronId;
        this.dateOfCheckout = dateOfCheckout;
    }

    public static CheckoutDto from(Hold hold) {
        return new CheckoutDto(
                hold.getId().id().toString(),
                hold.getHeldBy().id().toString(),
                hold.getDateOfCheckout());
    }
}
