package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.company.Company;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import lombok.AllArgsConstructor;
import org.hibernate.tool.schema.extract.internal.TableInformationImpl;
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
