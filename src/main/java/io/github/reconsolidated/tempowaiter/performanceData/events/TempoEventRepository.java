package io.github.reconsolidated.tempowaiter.performanceData.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface TempoEventRepository extends JpaRepository<TempoEvent, Long> {

    @Query(value = "SELECT company_id, event_name, additional_data, COUNT(*) " +
            "FROM tempo_event " +
            "WHERE time > :startDate " +
            "AND time < :endDate " +
            "GROUP BY company_id, event_name, additional_data", nativeQuery = true)
    List<Map<String, Object>> getEventsData(@Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);
}
