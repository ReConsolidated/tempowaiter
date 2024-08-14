package io.github.reconsolidated.tempowaiter.infrastracture.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.reconsolidated.tempowaiter.TestConfig;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.domain.card.Card;
import io.github.reconsolidated.tempowaiter.application.card.CardService;
import io.github.reconsolidated.tempowaiter.domain.company.Company;
import io.github.reconsolidated.tempowaiter.application.company.CompanyService;
import io.github.reconsolidated.tempowaiter.infrastracture.api.orders.TableOrdersController;
import io.github.reconsolidated.tempowaiter.domain.order.OrderDto;
import io.github.reconsolidated.tempowaiter.domain.order.orderEntry.OrderEntryDto;
import io.github.reconsolidated.tempowaiter.domain.table.TableInfo;
import io.github.reconsolidated.tempowaiter.application.table.TableService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class TableOrdersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TableService tableService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CardService cardService;
    private AppUser appUser;
    private TableInfo tableInfo;
    private String sessionId = "test-session-id";

    @BeforeEach
    void beforeEach() {
        try {
            appUserService.register("test@test.com", "test");
            Company company = companyService.createCompany("test-company");
            appUserService.setCompanyId("test@test.com", company.getId());
            appUser = appUserService.getUser("test@test.com").orElseThrow();
            tableInfo = tableService.createTable(company.getId(), "test-table");
            Card card = cardService.createCard("427B63B9836F5EC8A0BCF3DB7599328E");
            cardService.setCardCompanyId(card.getId(), company.getId());
            card.setCompanyId(company.getId());
            tableService.setCardId(company.getId(), tableInfo.getTableId(), card.getId());
            tableService.startSessionAdmin(appUser, sessionId, tableInfo.getTableId());
        } catch (Exception ignored) {
            // There are exceptions in secondary runs, but everything is already initiated, so it's ok.
        }

    }

    @Test
    @Transactional
    @Rollback
    void getTableOrders_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/public/table-orders")
                        .param("sessionId", sessionId))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    void createOrder_ShouldReturnOk() throws Exception {
        TableOrdersController.CreateOrderRequest request = new TableOrdersController.CreateOrderRequest();
        request.setOrderEntries(Collections.emptyList());

        mockMvc.perform(post("/public/table-orders")
                        .param("sessionId", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    void createAndRetrieveOrders() throws Exception {
        String sessionId = "test-session-id";

        // Create first order
        TableOrdersController.CreateOrderRequest firstOrderRequest = new TableOrdersController.CreateOrderRequest();
        firstOrderRequest.setOrderEntries(Collections.emptyList());

        mockMvc.perform(post("/public/table-orders")
                        .param("sessionId", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstOrderRequest)))
                .andExpect(status().isOk());

        // Create second order
        TableOrdersController.CreateOrderRequest secondOrderRequest = new TableOrdersController.CreateOrderRequest();
        secondOrderRequest.setOrderEntries(Collections.emptyList());

        mockMvc.perform(post("/public/table-orders")
                        .param("sessionId", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondOrderRequest)))
                .andExpect(status().isOk());

        // Retrieve orders
        MvcResult result = mockMvc.perform(get("/public/table-orders")
                        .param("sessionId", sessionId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<OrderDto> orders = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDto.class));

        // Assert that two orders are retrieved
        assertThat(orders).hasSize(2);
    }

    @Test
    @Transactional
    @Rollback
    void createAndRetrieveOrdersWithEntries() throws Exception {

        // Create first order with entries
        TableOrdersController.CreateOrderRequest firstOrderRequest = new TableOrdersController.CreateOrderRequest();
        firstOrderRequest.setOrderEntries(Arrays.asList(
                new OrderEntryDto(),
                new OrderEntryDto()
        ));

        mockMvc.perform(post("/public/table-orders")
                        .param("sessionId", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstOrderRequest)))
                .andExpect(status().isOk());

        // Create second order with entries
        TableOrdersController.CreateOrderRequest secondOrderRequest = new TableOrdersController.CreateOrderRequest();
        secondOrderRequest.setOrderEntries(Arrays.asList(
                new OrderEntryDto(),
                new OrderEntryDto()
        ));

        mockMvc.perform(post("/public/table-orders")
                        .param("sessionId", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondOrderRequest)))
                .andExpect(status().isOk());

        // Retrieve orders
        MvcResult result = mockMvc.perform(get("/public/table-orders")
                        .param("sessionId", sessionId))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<OrderDto> orders = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDto.class));

        // Assert that two orders are retrieved
        assertThat(orders).hasSize(2);

        // Assert that the order entries are saved correctly
        assertThat(orders.get(0).getOrderEntries()).hasSize(2);
        assertThat(orders.get(0).getOrderEntries().get(0).getOrderId()).isEqualTo(orders.get(0).getId());
        assertThat(orders.get(0).getOrderEntries().get(1).getOrderId()).isEqualTo(orders.get(0).getId());

        assertThat(orders.get(1).getOrderEntries()).hasSize(2);
        assertThat(orders.get(1).getOrderEntries().get(0).getOrderId()).isEqualTo(orders.get(1).getId());
        assertThat(orders.get(1).getOrderEntries().get(1).getOrderId()).isEqualTo(orders.get(1).getId());
    }
}
