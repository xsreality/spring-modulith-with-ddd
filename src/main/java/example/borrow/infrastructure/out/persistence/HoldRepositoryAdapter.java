package example.borrow.infrastructure.out.persistence;

import org.jmolecules.architecture.hexagonal.SecondaryAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import example.borrow.domain.Hold;
import example.borrow.domain.HoldRepository;
import lombok.RequiredArgsConstructor;

@SecondaryAdapter
@Component
@Transactional
@RequiredArgsConstructor
public class HoldRepositoryAdapter implements HoldRepository {

    private final HoldJpaRepository holds;

    @Override
    public Hold save(Hold hold) {
        holds.save(HoldEntity.fromDomain(hold));
        return hold;
    }

    @Override
    public Optional<Hold> findById(Hold.HoldId id) {
        return holds.findById(id.id())
                .map(HoldEntity::toDomain);
    }

}
