package io.github.reconsolidated.tempowaiter.performanceData.events;

import io.github.reconsolidated.tempowaiter.card.CardService;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.performanceData.TablePerformanceData;
import io.github.reconsolidated.tempowaiter.performanceData.TableSessionsData;
import io.github.reconsolidated.tempowaiter.performanceData.TimeRange;
import io.github.reconsolidated.tempowaiter.performanceData.dto.CompaniesEventsDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.dto.CompaniesSessionsDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.dto.CompanyDataDto;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
