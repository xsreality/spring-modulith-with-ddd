package example.borrow.loan;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class BorrowController {

    private final LoanManagement loans;

    @PostMapping("/borrow/loans")
    ResponseEntity<LoanDto> holdBook(@RequestBody HoldRequest request) {
        var loanDto = loans.hold(request.barcode(), request.patronId());
        return ResponseEntity.ok(loanDto);
    }

    @PostMapping("/borrow/loans/{id}/checkout")
    ResponseEntity<LoanDto> checkoutBook(@PathVariable("id") Long loanId) {
        var loanDto = loans.checkout(loanId);
        return ResponseEntity.ok(loanDto);
    }

    @PostMapping("/borrow/loans/{id}/checkin")
    ResponseEntity<LoanDto> checkinBook(@PathVariable("id") Long loanId) {
        var loanDto = loans.checkin(loanId);
        return ResponseEntity.ok(loanDto);
    }

    @GetMapping("/borrow/loans/{id}")
    ResponseEntity<LoanDto> viewSingleLoan(@PathVariable("id") Long loanId) {
        return loans.locate(loanId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/borrow/loans")
    ResponseEntity<List<LoanDto>> viewActiveLoans(@RequestParam String type) {
        if ("onhold".equalsIgnoreCase(type)) {
            return ResponseEntity.ok(loans.onHoldLoans());
        }
        return ResponseEntity.ok(loans.activeLoans());
    }

    record HoldRequest(String barcode, Long patronId) {
    }
}
