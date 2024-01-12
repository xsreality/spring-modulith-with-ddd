package example.borrowv2.infrastructure.out.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;

import example.borrowv2.domain.model.Book.BookId;
import example.borrowv2.domain.model.Hold;
import example.borrowv2.domain.model.Hold.HoldStatus;
import example.borrowv2.domain.model.Patron.PatronId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class HoldEntity {

    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "bookId"))
    private BookId bookId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "patronId"))
    private PatronId patronId;

    private LocalDate dateOfCheckout;

    private LocalDate dateOfHold;

    private LocalDate dateOfCheckin;

    private int holdDurationInDays;

    private int loanDurationInDays;

    @Enumerated(EnumType.STRING)
    private HoldStatus status;

    @Version
    private Long version;

    public Hold toDomain() {
        return new Hold(
                bookId,
                patronId,
                dateOfHold,
                dateOfCheckout,
                holdDurationInDays,
                loanDurationInDays,
                dateOfCheckin,
                status);
    }

    public static HoldEntity fromDomain(Hold hold) {
        var entity = new HoldEntity();
        entity.id = hold.getId().id();
        entity.bookId = hold.getOnBook().getId();
        entity.patronId = hold.getHeldBy().getId();
        entity.dateOfHold = hold.getDateOfHold();
        entity.dateOfCheckout = hold.getDateOfCheckout();
        entity.dateOfCheckin = hold.getDateOfCheckin();
        entity.holdDurationInDays = hold.getHoldDurationInDays();
        entity.loanDurationInDays = hold.getLoanDurationInDays();
        entity.status = hold.getStatus();
        entity.version = 0L;
        return entity;
    }
}
