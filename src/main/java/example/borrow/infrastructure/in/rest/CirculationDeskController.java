package example.borrow.infrastructure.in.rest;

import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

import example.borrow.application.CheckoutDto;
import example.borrow.application.CirculationDesk;
import example.borrow.application.HoldDto;
import example.borrow.domain.Book;
import example.borrow.domain.Hold;
import example.borrow.domain.Patron.PatronId;
import example.useraccount.web.Authenticated;
import example.useraccount.UserAccount;
import lombok.RequiredArgsConstructor;

@PrimaryAdapter
@RestController
@RequiredArgsConstructor
public class CirculationDeskController {

    private final CirculationDesk circulationDesk;

    @PostMapping("/borrow/holds")
    ResponseEntity<HoldDto> holdBook(@RequestBody HoldRequest request, @Authenticated UserAccount userAccount) {
        var command = new Hold.PlaceHold(new Book.Barcode(request.barcode()), LocalDate.now(), new PatronId(userAccount.email()));
        var holdDto = circulationDesk.placeHold(command);
        return ResponseEntity.ok(holdDto);
    }

    @PostMapping("/borrow/holds/{id}/checkout")
    ResponseEntity<CheckoutDto> checkoutBook(@PathVariable("id") UUID holdId, @Authenticated UserAccount userAccount) {
        var command = new Hold.Checkout(new Hold.HoldId(holdId), LocalDate.now(), new PatronId(userAccount.email()));
        var checkoutDto = circulationDesk.checkout(command);
        return ResponseEntity.ok(checkoutDto);
    }

    @GetMapping("/borrow/holds/{id}")
    ResponseEntity<HoldDto> viewSingleHold(@PathVariable("id") UUID holdId) {
        return circulationDesk.locate(holdId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    record HoldRequest(String barcode) {
    }
}
