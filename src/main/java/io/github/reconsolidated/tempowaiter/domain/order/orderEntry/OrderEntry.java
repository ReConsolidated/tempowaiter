package io.github.reconsolidated.tempowaiter.domain.order.orderEntry;

import io.github.reconsolidated.tempowaiter.domain.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntry {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Order order;
}
