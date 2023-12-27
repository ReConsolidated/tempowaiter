package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.performanceData.TablePerformanceData;
import io.github.reconsolidated.tempowaiter.performanceData.TableSessionsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaiterRequestRepository extends JpaRepository<WaiterRequest, Long> {
    List<WaiterRequest> findByStateNotAndCompanyIdEquals(RequestState state, Long companyId);

    Optional<WaiterRequest> findByStateNotAndTableId(RequestState done, Long tableId);

    @Query(value = "SELECT table_name, AVG((resolved_at - requested_at)/1000) AS \"average time seconds\", COUNT(*) " +
            "FROM waiter_request " +
            "WHERE to_timestamp(requested_at/1000) > :startDate " +
            "AND company_id = :companyId " +
            "AND resolved_at > 0 " +
            "GROUP BY table_name", nativeQuery = true)
    Collection<TablePerformanceData> getTablePerformanceData(@Param("startDate") LocalDateTime startDate,
                                                             @Param("companyId") Long companyId);

    @Query(value = "SELECT table_id, COUNT(*) " +
            "FROM table_session " +
            "WHERE to_timestamp(started_at/1000) > :startDate " +
            "AND company_id = :companyId " +
            "GROUP BY table_id", nativeQuery = true)
    List<TableSessionsData> getTableSessionsData(@Param("startDate") LocalDateTime startDate,
                                                 @Param("companyId") Long companyId);
}
