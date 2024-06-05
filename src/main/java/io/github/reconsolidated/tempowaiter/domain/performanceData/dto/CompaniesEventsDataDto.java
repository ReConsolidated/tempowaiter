package io.github.reconsolidated.tempowaiter.domain.performanceData.dto;

import io.github.reconsolidated.tempowaiter.domain.performanceData.events.TempoEventsData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CompaniesEventsDataDto {
    List<CompanyDataDto<TempoEventsData>> companiesEventsData;
}
