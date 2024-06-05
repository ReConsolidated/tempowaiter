package io.github.reconsolidated.tempowaiter.domain.performanceData.dto;

import io.github.reconsolidated.tempowaiter.domain.performanceData.TableSessionsData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CompaniesSessionsDataDto {
    List<CompanyDataDto<TableSessionsData>> companiesSessionsData;
}