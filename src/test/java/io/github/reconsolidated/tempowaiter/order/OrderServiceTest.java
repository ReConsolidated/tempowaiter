package io.github.reconsolidated.tempowaiter.order;

import io.github.reconsolidated.tempowaiter.TestConfig;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.domain.order.Order;
import io.github.reconsolidated.tempowaiter.domain.order.OrderRepository;
import io.github.reconsolidated.tempowaiter.application.order.OrderService;
import io.github.reconsolidated.tempowaiter.domain.order.orderEntry.OrderEntry;
import io.github.reconsolidated.tempowaiter.domain.table.TableInfo;
import io.github.reconsolidated.tempowaiter.application.table.TableService;
import io.github.reconsolidated.tempowaiter.domain.table.TableSession;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
public class OrderServiceTest {
    @Mock
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private TableSession tableSession;
    private TableInfo tableInfo;
    private List<OrderEntry> orderEntries;

    @BeforeEach
    public void setUp() {
        tableSession = new TableSession();
        tableInfo = new TableInfo();
        orderEntries = new ArrayList<>();
        orderEntries.add(new OrderEntry());
    }

    @Test
    @Transactional
    public void createOrder_shouldCreateOrderSuccessfully() {
        when(tableService.getSessionOrThrow(anyString())).thenReturn(tableSession);
        when(tableService.getTableInfo(any(TableSession.class))).thenReturn(tableInfo);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder("session123", orderEntries);

        assertNotNull(order);
        assertEquals(tableInfo, order.getTableInfo());
        assertEquals(orderEntries, order.getOrderEntries());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @Transactional
    public void createOrder_shouldThrowExceptionWhenOrderEntryHasId() {
        orderEntries.get(0).setId(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder("session123", orderEntries);
        });
    }

    @Test
    public void getTableOrders_shouldReturnOrders() {
        when(tableService.getSessionOrThrow(anyString())).thenReturn(tableSession);
        when(tableService.getTableInfo(any(TableSession.class))).thenReturn(tableInfo);

        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        when(orderRepository.findByTableInfoAndFulfilledAtIsNull(any(TableInfo.class))).thenReturn(orders);

        List<Order> result = orderService.getTableOrders("session123");

        assertNotNull(result);
        assertEquals(orders.size(), result.size());
        verify(orderRepository, times(1)).findByTableInfoAndFulfilledAtIsNull(tableInfo);
    }

    @Test
    public void getCompanyOrders_shouldReturnOrders() {
        AppUser currentUser = AppUser.builder()
                .companyId(1L).build();

        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        when(orderRepository.findByTableInfoCompanyId(anyLong())).thenReturn(orders);

        List<Order> result = orderService.getCompanyOrders(currentUser);

        assertNotNull(result);
        assertEquals(orders.size(), result.size());
        verify(orderRepository, times(1)).findByTableInfoCompanyId(currentUser.getCompanyId());
    }
}