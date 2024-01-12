package example.borrowv2.infrastructure.in.rest;

import org.jmolecules.architecture.hexagonal.PrimaryAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

import example.borrowv2.domain.model.Book.Barcode;
import example.borrowv2.domain.model.Hold.HoldId;
import example.borrowv2.domain.model.Patron.PatronId;
import example.borrowv2.domain.service.BorrowServices;
import lombok.RequiredArgsConstructor;

@PrimaryAdapter
@RestController
@RequiredArgsConstructor
public class BorrowV2Controller {

    private final BorrowServices borrowServices;

    @PostMapping("/borrow/holds")
    ResponseEntity<Hold> placeHold(@RequestBody HoldRequest request) {
        var hold = borrowServices.placeHold(new PatronId(request.patronId), new Barcode(request.barcode));
        return ResponseEntity.ok(
                new Hold(
                        hold.getId().id(),
                        hold.getOnBook().getId().id(),
                        hold.getDateOfHold())
        );
    }

    @GetMapping("/borrow/holds/{id}")
    ResponseEntity<Hold> viewHold(@PathVariable("id") UUID holdId) {
        return borrowServices.fetchHold(new HoldId(holdId))
                .map(hold -> new Hold(
                        hold.getId().id(),
                        hold.getOnBook().getId().id(),
                        hold.getDateOfHold()))
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    record HoldRequest(String barcode, UUID patronId) {
    }

    record Hold(UUID holdId, UUID bookId, LocalDate dateOfHold) {
    }
}
