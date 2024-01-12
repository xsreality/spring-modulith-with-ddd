package example.borrowv2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import example.borrowv2.domain.model.BookPlacedOnHold;
import example.borrowv2.domain.model.Hold;
import example.borrowv2.domain.service.HoldRepository;

public class InMemoryHolds implements HoldRepository {

    private final Map<UUID, Hold> holds = new HashMap<>();

    @Override
    public void saveHold(Hold hold) {
        holds.put(hold.getId().id(), hold);
    }

    @Override
    public void publish(BookPlacedOnHold event) {

    }

    @Override
    public Optional<Hold> findById(Hold.HoldId id) {
        return Optional.of(holds.get(id.id()));
    }
}
