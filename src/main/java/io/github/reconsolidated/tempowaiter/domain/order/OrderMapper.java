package io.github.reconsolidated.tempowaiter.domain.order;

import io.github.reconsolidated.tempowaiter.domain.order.orderEntry.OrderEntry;
import io.github.reconsolidated.tempowaiter.domain.order.orderEntry.OrderEntryDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderedAt(order.getOrderedAt());
        orderDto.setOrderEntries(
                order.getOrderEntries().stream().map(this::toDto).toList()
        );
        return orderDto;
    }

    public OrderEntryDto toDto(OrderEntry orderEntry) {
        OrderEntryDto orderEntryDto = new OrderEntryDto();
        orderEntryDto.setId(orderEntry.getId());
        orderEntryDto.setOrderId(orderEntry.getOrder().getId());
        return orderEntryDto;
    }

    public OrderEntry toEntity(OrderEntryDto orderEntryDto) {
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setId(orderEntryDto.getId());
        return orderEntry;
    }
}
