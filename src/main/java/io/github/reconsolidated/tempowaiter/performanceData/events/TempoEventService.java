package io.github.reconsolidated.tempowaiter.performanceData.events;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TempoEventService {
    private final TempoEventRepository tempoEventRepository;

    public void reportEvent(TempoEvent event) {
        tempoEventRepository.save(event);
    }
}
