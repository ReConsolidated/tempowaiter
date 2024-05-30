package io.github.reconsolidated.tempowaiter.order;

import io.github.reconsolidated.tempowaiter.order.orderEntry.OrderEntry;
import io.github.reconsolidated.tempowaiter.order.orderEntry.OrderEntryDto;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private Long tableId;
    private List<OrderEntryDto> orderEntries;
    private LocalDateTime orderedAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime fulfilledAt;
}
