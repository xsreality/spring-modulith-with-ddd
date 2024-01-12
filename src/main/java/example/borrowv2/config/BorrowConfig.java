package example.borrowv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import example.borrowv2.domain.service.CirculationDesk;
import example.borrowv2.domain.service.BorrowRepository;
import example.borrowv2.domain.service.BorrowServices;
import example.borrowv2.domain.service.HoldRepository;

/**
 * Wiring the Primary Port implementation for dependency injection.
 */
@Configuration
public class BorrowConfig {

    @Bean
    public BorrowServices borrowServices(BorrowRepository repository, HoldRepository holds) {
        return new CirculationDesk(repository, holds);
    }
}
