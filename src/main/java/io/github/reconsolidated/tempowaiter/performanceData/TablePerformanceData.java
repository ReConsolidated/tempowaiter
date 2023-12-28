package io.github.reconsolidated.tempowaiter.performanceData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class TablePerformanceData {
    private Long companyId;
    private String tableName;
    private Double averageTimeSeconds;
    private Integer count;

    public TablePerformanceData(Map<String, Object> map) {
        this.companyId = Long.parseLong( "" + map.get("company_id"));
        this.tableName = (String) map.get("table_name");
        this.averageTimeSeconds = (Double) map.get("average_time_seconds");
        this.count = Integer.parseInt("" + map.get("count"));
    }
}
