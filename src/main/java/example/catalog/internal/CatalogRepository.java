package example.catalog.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import example.catalog.internal.CatalogBook.Barcode;

public interface CatalogRepository extends JpaRepository<CatalogBook, Long> {

    Optional<CatalogBook> findByIsbn(String isbn);

    Optional<CatalogBook> findByCatalogNumber(Barcode catalogNumber);
}
