package io.github.reconsolidated.tempowaiter.infrastracture.api.orders;

import io.github.reconsolidated.tempowaiter.order.OrderDto;
import io.github.reconsolidated.tempowaiter.order.OrderMapper;
import io.github.reconsolidated.tempowaiter.order.OrderService;
import io.github.reconsolidated.tempowaiter.order.orderEntry.OrderEntry;
import io.github.reconsolidated.tempowaiter.order.orderEntry.OrderEntryDto;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import io.github.reconsolidated.tempowaiter.table.TableService;
import io.github.reconsolidated.tempowaiter.table.TableSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/public/table-orders")
public class TableOrdersController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;


    @GetMapping
    public ResponseEntity<List<OrderDto>> getTableOrders(@RequestParam String sessionId) {
        return ResponseEntity.ok(
                orderService.getTableOrders(sessionId).stream().map(orderMapper::toDto).toList()
        );
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestParam String sessionId,
                                @RequestBody CreateOrderRequest orderRequest) {
        List<OrderEntry> orderEntries = orderRequest.getOrderEntries().stream().map(orderMapper::toEntity).toList();
        return ResponseEntity.ok(
                orderMapper.toDto(orderService.createOrder(sessionId, orderEntries))
        );
    }


    @Getter
    @Setter
    public static class CreateOrderRequest {
        private List<OrderEntryDto> orderEntries;
    }
}
