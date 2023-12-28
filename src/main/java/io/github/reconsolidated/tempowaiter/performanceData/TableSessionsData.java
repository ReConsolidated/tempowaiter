package io.github.reconsolidated.tempowaiter.performanceData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Map;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class TableSessionsData {
    private Long companyId;
    private String tableDisplayName;
    private Long count;

    public TableSessionsData(Map<String, Object> map) {
        this.companyId = Long.parseLong( "" + map.get("company_id"));
        this.tableDisplayName = (String) map.get("table_display_name");
        this.count = Long.parseLong("" + map.get("count"));
    }
}
