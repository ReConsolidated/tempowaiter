package io.github.reconsolidated.tempowaiter.domain.table;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableSessionRepository extends JpaRepository<TableSession, Long> {

    Optional<TableSession> findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(String sessionId, Long cardId, LocalDateTime now);

    List<TableSession> findAllByCardIdAndIsOverwrittenFalseAndExpirationDateGreaterThan(Long cardId, LocalDateTime expirationDate);

    Optional<TableSession> findBySessionIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(String sessionId, LocalDateTime now);

}
