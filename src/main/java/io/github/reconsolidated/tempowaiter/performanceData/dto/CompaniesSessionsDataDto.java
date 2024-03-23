package io.github.reconsolidated.tempowaiter.performanceData.dto;

import io.github.reconsolidated.tempowaiter.performanceData.TableSessionsData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CompaniesSessionsDataDto {
    List<CompanyDataDto<TableSessionsData>> companiesSessionsData;
}