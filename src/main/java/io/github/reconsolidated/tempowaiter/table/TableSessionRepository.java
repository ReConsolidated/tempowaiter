package io.github.reconsolidated.tempowaiter.table;

import com.sun.xml.bind.v2.schemagen.episode.SchemaBindings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TableSessionRepository extends JpaRepository<TableSession, Long> {

    Optional<TableSession> findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(String sessionId, Long cardId, LocalDateTime now);

    Optional<TableSession> findByCardIdAndIsOverwrittenFalseAndExpirationDateGreaterThan(Long cardId, LocalDateTime expirationDate);

    Optional<TableSession> findBySessionIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(String sessionId, LocalDateTime now);
}
