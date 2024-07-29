package example.borrow.domain;

import org.springframework.data.repository.CrudRepository;

public interface HoldRepository extends CrudRepository<Hold, Hold.HoldId> {

}
