package example.inventory;

import example.inventory.Book.Barcode;

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
class BookController {

    private final BookManagement books;

    @PostMapping("/books")
    ResponseEntity<BookDto> addBookToInventory(@RequestBody AddBookRequest request) {
        var bookDto = books.addToInventory(request.title(), new Barcode(request.inventoryNumber()), request.isbn(), request.author());
        return ResponseEntity.ok(bookDto);
    }

    @DeleteMapping("/books/{id}")
    ResponseEntity<Void> removeBookFromInventory(@PathVariable("id") Long id) {
        books.removeFromInventory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/books/{id}")
    ResponseEntity<BookDto> viewSingleBook(@PathVariable("id") Long id) {
        return books.locate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/books")
    ResponseEntity<List<BookDto>> viewIssuedBooks() {
        return ResponseEntity.ok(books.issuedBooks());
    }

    record AddBookRequest(String title, String inventoryNumber,
                          String isbn, String author) {
    }
}
