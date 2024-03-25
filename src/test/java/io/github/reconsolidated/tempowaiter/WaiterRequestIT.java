package io.github.reconsolidated.tempowaiter;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserDto;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.card.Card;
import io.github.reconsolidated.tempowaiter.card.CardService;
import io.github.reconsolidated.tempowaiter.company.Company;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.infrastracture.email.EmailService;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import io.github.reconsolidated.tempowaiter.table.TableService;
import io.github.reconsolidated.tempowaiter.waiter.RequestState;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WaiterRequestIT {
    @MockBean
    private JavaMailSender javaMailSender;
    @Autowired
    private WaiterService waiterService;
    @Autowired
    private TableService tableService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private CardService cardService;

    @Test
    @Transactional
    public void requestSent() {
        Company company = companyService.createCompany("test company");
        TableInfo tableInfo = tableService.createTable(company.getId(),  "test table");
        waiterService.callToTable("test_request_type", tableInfo, 1L, "");
        String email = "test@user.com";
        appUserService.register(email, email);
        AppUser appUser = appUserService.getUser(email).orElseThrow();

        appUserService.setCompanyId(email, company.getId());
        List<WaiterRequest> requestList = waiterService.getRequests(appUser.getId(), appUser.getCompanyId());

        assertThat(requestList).hasSize(1);
    }

    @Test
    @Transactional
    public void cancelCall() {
        Card card = cardService.createCard("427B63B9836F5EC8A0BCF3DB7599328E");
        Company company = companyService.createCompany("test company");
        TableInfo tableInfo = tableService.createTable(company.getId(),  "test table");
        tableInfo.setCardId(card.getId());
        String sessionId = "abc123";
        tableService.startSession(sessionId, card.getCardUid(), 15L);
        WaiterRequest request = waiterService.callToTable("test_request_type", tableInfo, card.getId(), "");
        String email = "test@user.com";
        appUserService.register(email, email);
        AppUser appUser = appUserService.getUser(email).orElseThrow();

        appUserService.setCompanyId(email, company.getId());
        List<WaiterRequest> requestList = waiterService.getRequests(appUser.getId(), appUser.getCompanyId());

        assertThat(requestList).hasSize(1);

        assertThat(tableService.cancelCall(sessionId, card.getId(), "test_request_type")).isTrue();

        requestList = waiterService.getRequests(appUser.getId(), appUser.getCompanyId());

        assertThat(requestList).hasSize(0);
    }

    @Test
    @Transactional
    public void processRequest() {
        Company company = companyService.createCompany("test company");
        TableInfo tableInfo = tableService.createTable(company.getId(), "test table");
        String sessionId = "any_id";
        WaiterRequest request = waiterService.callToTable("test_request_type", tableInfo, 1L, "");
        String appUserEmail = "test@user.com";
        appUserService.register(appUserEmail, appUserEmail);
        AppUser appUser = appUserService.getUser(appUserEmail).orElseThrow();
        String otherUserEmail = "other@waiter.com";
        appUserService.register(otherUserEmail, otherUserEmail);
        AppUser otherWaiter = appUserService.getUser(otherUserEmail).orElseThrow();

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
