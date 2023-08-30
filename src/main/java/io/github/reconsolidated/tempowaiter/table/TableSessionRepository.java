package io.github.reconsolidated.tempowaiter.table;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TableSessionRepository extends JpaRepository<TableSession, Long> {

    Optional<TableSession> findBySessionIdEqualsAndTableIdEqualsAndExpirationDateIsGreaterThan(String sessionId, long tableId, LocalDateTime expirationDate);


}
