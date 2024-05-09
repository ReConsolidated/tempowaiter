package io.github.reconsolidated.tempowaiter.performanceData;

import io.github.reconsolidated.tempowaiter.card.CardService;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.performanceData.dto.CompaniesPerformanceDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.dto.CompaniesSessionsDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.dto.CompanyDataDto;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PerformanceDataService {
    private final WaiterRequestRepository waiterRequestRepository;
    private final CompanyService companyService;
    private final CardService cardService;

    public CompaniesPerformanceDataDto getTablePerformanceData(@Nullable Long companyId,
                                                               TimeRange timeRange) {
        Collection<TablePerformanceData> data = waiterRequestRepository.getTablePerformanceData(timeRange.getFrom(), timeRange.getTo())
                .stream().map(TablePerformanceData::new).collect(Collectors.toList());
        if (companyId != null) {
            data = data.stream()
                    .filter(tablePerformanceData -> tablePerformanceData.getCompanyId().equals(companyId))
                    .toList();
        }

        Map<Long, List<TablePerformanceData>> result = data.stream()
                .collect(Collectors.groupingBy(TablePerformanceData::getCompanyId));
        List<CompanyDataDto<TablePerformanceData>> companyDataDtos = result.entrySet().stream()
                .map(entry -> new CompanyDataDto<>(
                        entry.getKey(),
                        companyService.getCompany(entry.getKey(), entry.getKey()).getName(),
                        getCompanyCardsCount(entry.getKey()),
                        entry.getValue()))
                .toList();
        return new CompaniesPerformanceDataDto(companyDataDtos);
    }

    private int getCompanyCardsCount(Long companyId) {
        return cardService.getCards(companyId).size();
    }

    public CompaniesSessionsDataDto getTableSessionData(Long companyId,
                                                        TimeRange timeRange) {
        Collection<TableSessionsData> data = waiterRequestRepository.getTableSessionsData(timeRange.getFrom(), timeRange.getTo())
                .stream().map(TableSessionsData::new).collect(Collectors.toList());
        if (companyId != null) {
            data = data.stream()
                    .filter(tableSessionsData -> tableSessionsData.getCompanyId().equals(companyId))
                    .collect(Collectors.toList());
        }
        Map<Long, List<TableSessionsData>> result = data.stream()
                .collect(Collectors.groupingBy(TableSessionsData::getCompanyId));
        List<CompanyDataDto<TableSessionsData>> companyDataDtos = result.entrySet().stream()
                .map(entry -> new CompanyDataDto<>(
                        entry.getKey(),
                        companyService.getCompany(entry.getKey(), entry.getKey()).getName(),
                        getCompanyCardsCount(entry.getKey()),
                        entry.getValue()))
                .toList();
        return new CompaniesSessionsDataDto(companyDataDtos);
    }
}
