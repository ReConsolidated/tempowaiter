package io.github.reconsolidated.tempowaiter.domain.performanceData.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TempoEventDto {
    private LocalDateTime time;
    private Long companyId;
    private String eventName;
    private String additionalData;
}
