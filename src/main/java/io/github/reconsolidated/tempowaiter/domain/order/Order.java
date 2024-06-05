package io.github.reconsolidated.tempowaiter.domain.order;

import io.github.reconsolidated.tempowaiter.domain.order.orderEntry.OrderEntry;
import io.github.reconsolidated.tempowaiter.domain.table.TableInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "customer_order")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private TableInfo tableInfo;
    @OneToMany(mappedBy = "order")
    @Cascade(CascadeType.ALL)
    private List<OrderEntry> orderEntries;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime fulfilledAt;
}
