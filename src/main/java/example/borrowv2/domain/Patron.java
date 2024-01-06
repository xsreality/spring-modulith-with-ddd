package example.borrowv2.domain;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

import lombok.Getter;

@Getter
public class Patron implements AggregateRoot<Patron, Patron.PatronId> {

    private PatronId id;

    private Membership status;

    public Patron(Membership status) {
        this.id = new PatronId(UUID.randomUUID());
        this.status = status;
    }

    public enum Membership {
        ACTIVE, INACTIVE
    }
    public record PatronId(UUID id) implements Identifier {}
}
