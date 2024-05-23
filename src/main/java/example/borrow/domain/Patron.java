package example.borrow.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class Patron {

    private final PatronId id;

    @Enumerated(EnumType.STRING)
    private Membership status;

    private Patron(String email) {
        this.id = new PatronId(email);
    }

    public static Patron of(String email) {
        return new Patron(email);
    }

    public void deactivate() {
        this.status = Membership.INACTIVE;
    }

    public record PatronId(String email) {
    }

    public enum Membership {
        ACTIVE, INACTIVE
    }
}
