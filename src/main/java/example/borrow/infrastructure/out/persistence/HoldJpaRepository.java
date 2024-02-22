package example.borrow.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HoldJpaRepository extends JpaRepository<HoldEntity, UUID> {
}
