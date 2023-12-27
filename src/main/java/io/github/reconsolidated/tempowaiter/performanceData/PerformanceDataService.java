package io.github.reconsolidated.tempowaiter.performanceData;

import io.github.reconsolidated.tempowaiter.waiter.WaiterRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@AllArgsConstructor
public class PerformanceDataService {
    private final WaiterRequestRepository waiterRequestRepository;

    public Collection<TablePerformanceData> getTablePerformanceData(Long companyId) {
        return waiterRequestRepository.getTablePerformanceData(LocalDateTime.now().minusHours(24), companyId);
    }

    public Collection<TableSessionsData> getTableSessionData(Long companyId) {
        return waiterRequestRepository.getTableSessionsData(LocalDateTime.now().minusHours(24), companyId);
    }
}
