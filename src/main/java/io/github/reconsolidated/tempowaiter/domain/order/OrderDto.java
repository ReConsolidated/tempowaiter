package io.github.reconsolidated.tempowaiter.domain.order;

import io.github.reconsolidated.tempowaiter.domain.order.orderEntry.OrderEntryDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private Long tableId;
    private String tableName;
    private List<OrderEntryDto> orderEntries;
    private OrderStatus orderStatus;
    private LocalDateTime orderedAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime fulfilledAt;
}