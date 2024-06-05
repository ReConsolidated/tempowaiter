package io.github.reconsolidated.tempowaiter.domain.order.orderEntry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEntryDto {
    private Long id;
    private Long orderId;
}
