package io.github.reconsolidated.tempowaiter;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.company.Company;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import io.github.reconsolidated.tempowaiter.table.TableService;
import io.github.reconsolidated.tempowaiter.waiter.RequestState;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
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
        TableInfo tableInfo = tableService.createTable(company.getId(),  "test table");
        waiterService.callToTable("any_id","test_request_type", tableInfo);
        String email = "test@user.com";
        AppUser appUser = appUserService.getOrCreateUser("test_user",
                email, "Tom", "Hanks");

        appUserService.setCompanyId(email, company.getId());
        List<WaiterRequest> requestList = waiterService.getRequests(appUser.getId(), appUser.getCompanyId());

        assertThat(requestList).hasSize(1);
    }

    @Test
    @Transactional
    public void processRequest() {
        Company company = companyService.createCompany("test company");
        TableInfo tableInfo = tableService.createTable(company.getId(), "test table");
        String sessionId = "any_id";
        WaiterRequest request = waiterService.callToTable(sessionId,"test_request_type", tableInfo);
        String appUserEmail = "test@user.com";
        AppUser appUser = appUserService.getOrCreateUser("test_user",
                appUserEmail, "Tom", "Hanks");
        String otherUserEmail = "other@waiter.com";
        AppUser otherWaiter = appUserService.getOrCreateUser("other_waiter",
                "other@waiter.com", "Other", "Waiter");

        appUserService.setCompanyId(appUserEmail, company.getId());
        appUserService.setCompanyId(otherUserEmail, company.getId());
        List<WaiterRequest> requestList = waiterService.getRequests(appUser.getId(), appUser.getCompanyId());

        assertThat(requestList).hasSize(1);

        assertThat(waiterService.getRequest(sessionId, request.getId()).isPresent()).isTrue();
        assertThat(waiterService.getRequest(sessionId, request.getId()).get().getState()).isEqualTo(RequestState.WAITING);

        waiterService.setRequestState(appUser.getId(), company.getId(), request.getId(), RequestState.IN_PROGRESS);

        requestList = waiterService.getRequests(appUser.getId(), appUser.getCompanyId());
        assertThat(requestList).hasSize(1);
        assertThat(waiterService.getRequests(otherWaiter.getId(), company.getId())).isEmpty();

        assertThat(waiterService.getRequest(sessionId, request.getId()).get().getState()).isEqualTo(RequestState.IN_PROGRESS);

        waiterService.setRequestState(appUser.getId(), company.getId(), request.getId(), RequestState.DONE);
        assertThat(waiterService.getRequest(sessionId, request.getId()).get().getState()).isEqualTo(RequestState.DONE);
        assertThat(waiterService.getRequests(appUser.getId(), appUser.getCompanyId())).isEmpty();
    }
}
