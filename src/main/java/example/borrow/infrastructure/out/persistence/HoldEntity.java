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

    private UUID patronId;

    private LocalDate dateOfHold;

    @Enumerated(EnumType.STRING)
    private HoldStatus status;

    @Version
    private Long version;

    public Hold toDomain() {
        if (this.status == HoldStatus.HOLDING) {
            return Hold.placeHold(new Hold.PlaceHold(book, dateOfHold, new PatronId(patronId)));
        } else {
            return null;
        }
    }

    public static HoldEntity fromDomain(Hold hold) {
        var entity = new HoldEntity();
        entity.id = hold.getId().id();
        entity.book = hold.getOnBook();
        entity.patronId = hold.getHeldBy().id();
        entity.dateOfHold = hold.getDateOfHold();
        entity.status = HoldStatus.HOLDING;
        entity.version = 0L;
        return entity;
    }
}

enum HoldStatus {
    HOLDING, ACTIVE, COMPLETED
}
