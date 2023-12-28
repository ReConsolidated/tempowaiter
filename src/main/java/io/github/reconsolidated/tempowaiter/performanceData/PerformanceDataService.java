package io.github.reconsolidated.tempowaiter.performanceData;

import io.github.reconsolidated.tempowaiter.waiter.WaiterRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PerformanceDataService {
    private final WaiterRequestRepository waiterRequestRepository;

    public CompaniesPerformanceDataDto getTablePerformanceData(@Nullable Long companyId,
                                                               TimeRange timeRange) {
        Collection<TablePerformanceData> data = waiterRequestRepository.getTablePerformanceData(timeRange.getFrom(), timeRange.getTo())
                .stream().map(TablePerformanceData::new).collect(Collectors.toList());
        if (companyId != null) {
            data = data.stream()
                    .filter(tablePerformanceData -> tablePerformanceData.getCompanyId().equals(companyId))
                    .collect(Collectors.toList());
        }
        Map<Long, List<TablePerformanceData>> result = data.stream()
                .collect(Collectors.groupingBy(TablePerformanceData::getCompanyId));
        return new CompaniesPerformanceDataDto(result);
    }

    public CompaniesSessionsDataDto getTableSessionData(Long companyId,
                                                        TimeRange timeRange) {
        Collection<TableSessionsData> data = waiterRequestRepository.getTableSessionsData(timeRange.getFrom(), timeRange.getTo())
                .stream().map(TableSessionsData::new).collect(Collectors.toList());
        if (companyId != null) {
            data = data.stream()
                    .filter(tableSessionsData -> tableSessionsData.getCompanyId().equals(companyId))
                    .collect(Collectors.toList());
        }
        Map<Long, List<TableSessionsData>> result = data.stream()
                .collect(Collectors.groupingBy(TableSessionsData::getCompanyId));
        return new CompaniesSessionsDataDto(result);
    }
}
