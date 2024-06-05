package io.github.reconsolidated.tempowaiter.domain.performanceData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TimeRange {
    private LocalDateTime from;
    private LocalDateTime to;
}
