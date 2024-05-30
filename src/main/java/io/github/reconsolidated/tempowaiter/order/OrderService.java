package io.github.reconsolidated.tempowaiter.order;

import io.github.reconsolidated.tempowaiter.order.orderEntry.OrderEntry;
import io.github.reconsolidated.tempowaiter.order.orderEntry.OrderEntryRepository;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import io.github.reconsolidated.tempowaiter.table.TableService;
import io.github.reconsolidated.tempowaiter.table.TableSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final TableService tableService;
    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(String sessionId, List<OrderEntry> entries) {
        TableSession tableSession = tableService.getSessionOrThrow(sessionId);
        TableInfo tableInfo = tableService.getTableInfo(tableSession);
        for (OrderEntry entry : entries) {
            if (entry.getId() != null) {
                throw new IllegalArgumentException("OrderEntry should not have an ID set");
            }
        }

        Order order = new Order();
        order.setTableInfo(tableInfo);
        order.setOrderEntries(entries);

        // Set the order reference in each order entry
        for (OrderEntry entry : entries) {
            entry.setOrder(order);
        }

        // Save the order (this will also save the entries because of the cascade setting)
        return orderRepository.save(order);
    }

    public List<Order> getTableOrders(String sessionId) {
        TableSession tableSession = tableService.getSessionOrThrow(sessionId);
        TableInfo tableInfo = tableService.getTableInfo(tableSession);
        return orderRepository.findByTableInfoAndFulfilledAtIsNull(tableInfo);
    }
}