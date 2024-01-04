package io.github.reconsolidated.tempowaiter.performanceData.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TempoEventRepository extends JpaRepository<TempoEvent, Long> {

    @Query(value = "SELECT company_id, event_name, additional_data, COUNT(*) " +
            "FROM tempo_event " +
            "WHERE time > :start_date " +
            "AND time < :end_date " +
            "GROUP BY company_id, event_name, additional_data")
    List<TempoEventsData> getEventsData(@Param("start_date") LocalDateTime startDate,
                                        @Param("end_date") LocalDateTime endDate);
}
