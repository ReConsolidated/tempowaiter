package io.github.reconsolidated.tempowaiter.domain.company;

import io.github.reconsolidated.tempowaiter.domain.table.TableInfo;
import io.github.reconsolidated.tempowaiter.application.table.TableService;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CompanyListDto {
    private final List<CompanyDto> companies;

    public CompanyListDto(List<Company> companies, TableService tableService) {
        this.companies = companies.stream().map((company ->
                new CompanyDto(company, tableService.listTables(company.getId())))).toList();
    }

    @Getter
    static class CompanyDto {
        private final Long id;
        private final String name;
        private final String menuLink;
        private final List<String> backgroundImages;
        private final String facebookLink;
        private final String instagramLink;
        private final String tiktokLink;
        private final String googleReviewLink;
        private final String tripadvisorLink;
        private final List<CompanyListTableInfoDto> tableList;
        private final LocalDateTime lastViewedRequests;

        public CompanyDto(Company company, List<TableInfo> tableInfoList) {
            this.id = company.getId();
            this.name = company.getName();
            this.menuLink = company.getMenuLink();
            this.backgroundImages = company.getBackgroundImages();
            this.facebookLink = company.getFacebookLink();
            this.instagramLink = company.getInstagramLink();
            this.tiktokLink = company.getTiktokLink();
            this.googleReviewLink = company.getGoogleReviewLink();
            this.tripadvisorLink = company.getTripadvisorLink();
            this.tableList = tableInfoList.stream().map((CompanyListTableInfoDto::new)).toList();
            this.lastViewedRequests = company.getLastViewedRequests();
        }
    }

    @Getter
    static class CompanyListTableInfoDto {
        private final Long id;
        private final Long cardId;
        private final String tableDisplayName;

        public CompanyListTableInfoDto(TableInfo tableInfo) {
            this.id = tableInfo.getTableId();
            this.cardId = tableInfo.getCardId();
            this.tableDisplayName = tableInfo.getTableDisplayName();
        }
    }
}
