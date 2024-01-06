package example.borrowv2.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import example.borrowv2.infrastructure.out.persistence.entity.HoldEntity;

public interface HoldJpaRepository extends JpaRepository<HoldEntity, UUID> {
}
