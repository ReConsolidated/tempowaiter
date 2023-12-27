package io.github.reconsolidated.tempowaiter.performanceData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class TablePerformanceData {
    private Long companyId;
    private String tableName;
    private Double averageTimeSeconds;
    private Integer count;
}
