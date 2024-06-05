package io.github.reconsolidated.tempowaiter.infrastracture.api.orders;

import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.domain.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.domain.order.OrderDto;
import io.github.reconsolidated.tempowaiter.domain.order.OrderMapper;
import io.github.reconsolidated.tempowaiter.application.order.OrderService;
import io.github.reconsolidated.tempowaiter.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class PanelOrdersController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(@CurrentUser AppUser currentUser) {
        return ResponseEntity.ok(
                orderService.getCompanyOrders(currentUser).stream()
                        .map(orderMapper::toDto)
                        .toList()
        );
    }

    @PostMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> setStatus(@CurrentUser AppUser currentUser, @PathVariable Long orderId, @RequestParam String status) {
        return ResponseEntity.ok(
                orderMapper.toDto(orderService.setOrderStatus(currentUser, orderId, OrderStatus.valueOf(status)))
        );
    }

}
