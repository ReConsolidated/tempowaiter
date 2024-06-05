package io.github.reconsolidated.tempowaiter.domain.table;

import io.github.reconsolidated.tempowaiter.domain.company.Company;
import io.github.reconsolidated.tempowaiter.application.company.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TableInfoMapper {
    private final CompanyService companyService;

    public TableInfoDto toDto(TableInfo tableInfo) {
        Company company = companyService.getById(tableInfo.getCompanyId());
        return new TableInfoDto(
                tableInfo.getTableId(),
                tableInfo.getCardId(),
                tableInfo.getCompanyId(),
                tableInfo.getTableDisplayName(),
                tableInfo.getLastCtr(),
                company.getName(),
                company.getMenuLink()
                );
    }
}
