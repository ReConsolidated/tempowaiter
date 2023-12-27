package io.github.reconsolidated.tempowaiter.performanceData;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class CompaniesPerformanceDataDto {
    Map<Long, List<TablePerformanceData>> companyIdToPerformanceData;
}
