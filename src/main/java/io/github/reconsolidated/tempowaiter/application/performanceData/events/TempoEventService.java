package io.github.reconsolidated.tempowaiter.application.performanceData.events;

import io.github.reconsolidated.tempowaiter.application.card.CardService;
import io.github.reconsolidated.tempowaiter.application.company.CompanyService;
import io.github.reconsolidated.tempowaiter.domain.performanceData.TimeRange;
import io.github.reconsolidated.tempowaiter.domain.performanceData.dto.CompaniesEventsDataDto;
import io.github.reconsolidated.tempowaiter.domain.performanceData.dto.CompanyDataDto;
import io.github.reconsolidated.tempowaiter.domain.performanceData.events.TempoEvent;
import io.github.reconsolidated.tempowaiter.domain.performanceData.events.TempoEventRepository;
import io.github.reconsolidated.tempowaiter.domain.performanceData.events.TempoEventsData;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TempoEventService {
    private final TempoEventRepository tempoEventRepository;
    private final CompanyService companyService;
    private final CardService cardService;

    public void reportEvent(TempoEvent event) {
        tempoEventRepository.save(event);
    }

    public CompaniesEventsDataDto getEventsData(@Nullable Long companyId, TimeRange timeRange) {
        List<TempoEventsData> data = tempoEventRepository.getEventsData(timeRange.getFrom(), timeRange.getTo())
                .stream().map(TempoEventsData::new).collect(Collectors.toList());;
        if (companyId != null) {
            data = data.stream()
                    .filter(event -> event.getCompanyId().equals(companyId))
                    .collect(Collectors.toList());
        }
        Map<Long, List<TempoEventsData>> result = data.stream()
                .collect(Collectors.groupingBy(TempoEventsData::getCompanyId));
        List<CompanyDataDto<TempoEventsData>> companyDataDtos = result.entrySet().stream()
                .map(entry -> new CompanyDataDto<>(
                        entry.getKey(),
                        companyService.getCompany(entry.getKey(), entry.getKey()).getName(),
                        getCompanyCardsCount(entry.getKey()),
                        entry.getValue()))
                .toList();
        return new CompaniesEventsDataDto(companyDataDtos);
    }

    private int getCompanyCardsCount(Long companyId) {
        return cardService.getCards(companyId).size();
    }
}
