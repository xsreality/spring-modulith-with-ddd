package example.borrowv2.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Sets up the AOP for wrapping methods inside classes marked with {@link TransactionalPort} in a
 * {@link Transactional}.
 */
@Configuration
public class PrimaryPortTransactionalConfiguration {

    @Bean
    PrimaryPortTransactionalAspect primaryPortTransactionalAspect(PrimaryPortTransactionalWrapper wrapper) {
        return new PrimaryPortTransactionalAspect(wrapper);
    }

    @Bean
    PrimaryPortTransactionalWrapper primaryPortTransactionalWrapper() {
        return new PrimaryPortTransactionalWrapper();
    }
}

class PrimaryPortTransactionalWrapper {

    @Transactional
    public <T> T executeInTransaction(Supplier<T> executable) {
        return executable.get();
    }
}

@Aspect
@RequiredArgsConstructor
class PrimaryPortTransactionalAspect {

    private final PrimaryPortTransactionalWrapper primaryPortTransactionalWrapper;

    @Pointcut("@within(port)")
    void inTransactionalPort(TransactionalPort port) {
    }

    @SuppressWarnings("ArgNamesWarningsInspection")
    @Around("inTransactionalPort(port)")
    Object primaryPort(ProceedingJoinPoint proceedingJoinPoint, @SuppressWarnings("unused") TransactionalPort port) {
        return primaryPortTransactionalWrapper.executeInTransaction(() -> proceed(proceedingJoinPoint));
    }

    @SneakyThrows
    private Object proceed(ProceedingJoinPoint proceedingJoinPoint) {
        return proceedingJoinPoint.proceed();
    }
}