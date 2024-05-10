package example.borrow.domain;

import org.jmolecules.architecture.hexagonal.SecondaryPort;

import java.util.Optional;

@SecondaryPort
public interface HoldRepository {

    Hold save(Hold hold);

    Optional<Hold> findById(Hold.HoldId id);
}
