package io.github.reconsolidated.tempowaiter.performanceData;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TableSessionsData {
    private Long companyId;
    private String tableDisplayName;
    private Integer count;
}
