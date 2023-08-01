package io.github.reconsolidated.tempowaiter.table;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableSessionRepository extends JpaRepository<TableSession, Long> {
    Optional<TableSession> findBySessionIdEqualsAndExpirationDateIsNullAndTableIdEquals(String sessionId, Long tableId);

}
