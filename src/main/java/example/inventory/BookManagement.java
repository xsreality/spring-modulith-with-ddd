package example.inventory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class BookManagement {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    /**
     * Add a new book to the library.
     */
    public BookDto addToInventory(String title, Book.Barcode inventoryNumber, String isbn, String authorName) {
        var book = new Book(title, inventoryNumber, isbn, new Book.Author(authorName));
        return mapper.toDto(bookRepository.save(book));
    }

    /**
     * Remove a book from library.
     */
    public void removeFromInventory(Long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));
        if (book.issued()) {
            throw new IllegalStateException("Book is currently issued!");
        }
        bookRepository.deleteById(bookId);
    }

    /**
     * Issue the book from library.
     *
     * @param barcode Unique identifier of the book
     */
    public void issue(String barcode) {
        var inventoryNumber = new Book.Barcode(barcode);
        var book = bookRepository.findByInventoryNumber(inventoryNumber)
                .map(Book::markIssued)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));
        bookRepository.save(book);
    }

    /**
     * Release the book into library.
     *
     * @param barcode Unique identifier of the book
     */
    public void release(String barcode) {
        var inventoryNumber = new Book.Barcode(barcode);
        var book = bookRepository.findByInventoryNumber(inventoryNumber)
                .map(Book::markAvailable)
                .orElseThrow(() -> new IllegalArgumentException("Book not found!"));
        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Optional<BookDto> locate(Long id) {
        return bookRepository.findById(id)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<BookDto> issuedBooks() {
        return bookRepository.findByStatus(Book.BookStatus.ISSUED)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
