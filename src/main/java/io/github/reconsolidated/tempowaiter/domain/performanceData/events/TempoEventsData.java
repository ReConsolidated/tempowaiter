package io.github.reconsolidated.tempowaiter.domain.performanceData.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class TempoEventsData {
    private Long companyId;
    private String eventName;
    private String additionalData;
    private Long count;

    public TempoEventsData(Map<String, Object> map) {
        this.companyId = Long.parseLong( "" + map.get("company_id"));
        this.eventName = (String) map.get("event_name");
        this.additionalData = (String) map.get("additional_data");
        this.count = Long.parseLong("" + map.get("count"));
    }
}