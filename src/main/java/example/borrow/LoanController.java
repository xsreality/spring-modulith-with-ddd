package example.borrow;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class LoanController {

    private final LoanManagement loans;

    @PostMapping("/loans")
    ResponseEntity<LoanDto> checkoutBook(@RequestBody CheckoutRequest request) {
        var barcode = request.barcode();
        var loanDto = loans.checkout(barcode);
        return ResponseEntity.ok(loanDto);
    }

    @DeleteMapping("/loans/{id}")
    ResponseEntity<LoanDto> checkinBook(@PathVariable("id") Long loanId) {
        var loanDto = loans.checkin(loanId);
        return ResponseEntity.ok(loanDto);
    }

    @GetMapping("/loans/{id}")
    ResponseEntity<LoanDto> viewSingleLoan(@PathVariable("id") Long loanId) {
        return loans.locate(loanId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/loans")
    ResponseEntity<List<LoanWithBookDto>> viewActiveLoans() {
        return ResponseEntity.ok(loans.activeLoans());
    }

    record CheckoutRequest(String barcode) {
    }
}
