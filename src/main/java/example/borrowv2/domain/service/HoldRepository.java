package example.borrowv2.domain.service;

import org.jmolecules.architecture.hexagonal.SecondaryPort;

import java.util.Optional;

import example.borrowv2.domain.model.BookPlacedOnHold;
import example.borrowv2.domain.model.Hold;

@SecondaryPort
public interface HoldRepository {

    Optional<Hold> findById(Hold.HoldId id);

    void saveHold(Hold hold);

    void publish(BookPlacedOnHold event);
}
