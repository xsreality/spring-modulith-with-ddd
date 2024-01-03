package example.borrow.patron;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import example.borrow.patron.domain.Patron;
import example.borrow.patron.domain.Patron.Membership;
import example.borrow.patron.domain.PatronRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PatronInitializer {

    private final PatronRepository patrons;

    @Bean
    CommandLineRunner init() {
        // create a patron with ID 1
        return args -> patrons.save(Patron.of(Membership.ACTIVE));
    }
}
