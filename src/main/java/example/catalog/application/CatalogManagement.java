package example.catalog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import example.catalog.domain.CatalogBook;
import example.catalog.domain.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CatalogManagement {

    private final CatalogRepository catalogRepository;

    /**
     * Add a new book to the library.
     */
    public BookDto addToCatalog(String title, CatalogBook.Barcode catalogNumber, String isbn, String authorName) {
        var book = new CatalogBook(title, catalogNumber, isbn, new CatalogBook.Author(authorName));
        return BookDto.from(catalogRepository.save(book));
    }

    @Transactional(readOnly = true)
    public Optional<BookDto> locate(Long id) {
        return catalogRepository.findById(id)
                .map(BookDto::from);
    }

    @Transactional(readOnly = true)
    public List<BookDto> fetchBooks() {
        return catalogRepository.findAll()
                .stream()
                .map(BookDto::from)
                .toList();
    }
}
