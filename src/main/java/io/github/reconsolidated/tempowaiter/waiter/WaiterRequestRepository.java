package io.github.reconsolidated.tempowaiter.waiter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WaiterRequestRepository extends JpaRepository<WaiterRequest, Long> {
    List<WaiterRequest> findByStateNotAndCompanyIdEquals(RequestState state, Long companyId);

    List<WaiterRequest> findByStateNotAndTableId(RequestState done, Long tableId);

    @Query(value = "SELECT company_id, table_name, AVG((resolved_at - requested_at)/1000) AS \"average time seconds\", COUNT(*) " +
            "FROM waiter_request " +
            "WHERE to_timestamp(requested_at/1000) > :startDate " +
            "AND to_timestamp(requested_at/1000) < :endDate " +
            "AND resolved_at > 0 " +
            "GROUP BY company_id, table_name", nativeQuery = true)
    Collection<Map<String, Object>> getTablePerformanceData(@Param("startDate") LocalDateTime startDate,
                                                            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT table_session.company_id AS company_id, table_display_name, COUNT(*) AS count " +
            "FROM table_session " +
            "LEFT JOIN table_info " +
            "ON table_session.table_id=table_info.table_id " +
            "WHERE to_timestamp(started_at/1000) > :startDate " +
            "AND to_timestamp(started_at/1000) < :endDate " +
            "GROUP BY table_session.company_id, table_display_name", nativeQuery = true)
    List<Map<String, Object>> getTableSessionsData(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
}
