package io.github.reconsolidated.tempowaiter;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.company.Company;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import io.github.reconsolidated.tempowaiter.table.TableService;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WaiterRequestIT {
    @Autowired
    private WaiterService waiterService;
    @Autowired
    private TableService tableService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AppUserService appUserService;

    @Test
    @Transactional
    public void requestSent() {
        Company company = companyService.createCompany("test company");
        TableInfo tableInfo = tableService.createTable(company.getId(), "test table");
        waiterService.callToTable("test_request_type", tableInfo);
        AppUser appUser = appUserService.getOrCreateUser("test_user",
                "test@user.com", "Tom", "Hanks");

        appUserService.setCompanyId(appUser, company.getId());
        List<WaiterRequest> requestList = waiterService.getRequests(appUser.getId(), appUser.getCompanyId());

        assertThat(requestList).hasSize(1);
    }
}
