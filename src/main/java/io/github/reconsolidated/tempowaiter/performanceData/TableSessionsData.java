package io.github.reconsolidated.tempowaiter.performanceData;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TableSessionsData {
    private Long tableId;
    private Integer count;
}
