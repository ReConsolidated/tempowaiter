package io.github.reconsolidated.tempowaiter.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private LocalDateTime orderedAt;
}
