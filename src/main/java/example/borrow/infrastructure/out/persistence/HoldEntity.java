package example.borrow.infrastructure.out.persistence;

import org.jmolecules.ddd.annotation.Identity;

import java.time.LocalDate;
import java.util.UUID;

import example.borrow.domain.Book;
import example.borrow.domain.Hold;
import example.borrow.domain.Patron.PatronId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Entity
@NoArgsConstructor
@Table(name = "borrow_holds")
public class HoldEntity {

    @Identity
    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "barcode", column = @Column(name = "bookBarcode"))
    private Book.Barcode book;

    private String patronId;

    private LocalDate dateOfHold;

    private LocalDate dateOfCheckout;

    @Enumerated(EnumType.STRING)
    private HoldStatus status;

    @Version
    private Long version;

    public Hold toDomain() {
        return new Hold(new Hold.HoldId(id), book, new PatronId(patronId), dateOfHold, dateOfCheckout);
    }

    public static HoldEntity fromDomain(Hold hold) {
        var entity = new HoldEntity();
        entity.id = hold.getId().id();
        entity.book = hold.getOnBook();
        entity.patronId = hold.getHeldBy().email();
        entity.dateOfHold = hold.getDateOfHold();
        entity.status = HoldStatus.HOLDING;
        entity.version = 0L;
        return entity;
    }
}

enum HoldStatus {
    HOLDING, ACTIVE, COMPLETED
}
