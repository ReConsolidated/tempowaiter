package io.github.reconsolidated.tempowaiter.performanceData.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempoEventRepository extends JpaRepository<TempoEvent, Long> {
}
