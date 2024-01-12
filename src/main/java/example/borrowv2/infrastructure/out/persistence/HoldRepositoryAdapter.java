package example.borrowv2.infrastructure.out.persistence;

import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import example.borrowv2.domain.model.BookPlacedOnHold;
import example.borrowv2.domain.model.Hold;
import example.borrowv2.domain.service.HoldRepository;
import example.borrowv2.infrastructure.out.persistence.entity.HoldEntity;
import lombok.RequiredArgsConstructor;

@SecondaryAdapter
@Transactional
@Component
@RequiredArgsConstructor
public class HoldRepositoryAdapter implements HoldRepository {

    private final HoldJpaRepository holds;
    private final ApplicationEventPublisher events;

    @Override
    public void saveHold(Hold hold) {
        holds.save(HoldEntity.fromDomain(hold));
    }

    @Override
    public void publish(BookPlacedOnHold event) {
        events.publishEvent(event);
    }

    @Override
    public Optional<Hold> findById(Hold.HoldId id) {
        return holds.findById(id.id())
                .map(HoldEntity::toDomain);
    }
}
