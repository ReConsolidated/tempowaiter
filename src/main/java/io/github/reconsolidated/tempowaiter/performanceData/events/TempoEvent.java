package io.github.reconsolidated.tempowaiter.performanceData.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TempoEvent {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime time;
    private Long companyId;
    private String eventName;
    private String additionalData;

    public TempoEvent(TempoEventDto dto) {
        this.time = dto.getTime();
        this.companyId = dto.getCompanyId();
        this.eventName = dto.getEventName();
        this.additionalData = dto.getAdditionalData();
    }
}
