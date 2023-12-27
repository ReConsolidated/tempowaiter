package io.github.reconsolidated.tempowaiter.performanceData;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TablePerformanceData {
    private Long companyId;
    private String tableName;
    private Double averageTimeSeconds;
    private Integer count;
}
