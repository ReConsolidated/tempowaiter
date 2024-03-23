package io.github.reconsolidated.tempowaiter.performanceData.dto;

import io.github.reconsolidated.tempowaiter.performanceData.TablePerformanceData;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CompaniesPerformanceDataDto {
    List<CompanyDataDto<TablePerformanceData>> companiesPerformanceData;
}
