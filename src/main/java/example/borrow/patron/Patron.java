package example.borrow.patron;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Membership status;

    Patron(Membership status) {
        this.status = status;
    }

    public static Patron of(Membership status) {
        return new Patron(status);
    }

    public enum Membership {
        ACTIVE, INACTIVE
    }
}
