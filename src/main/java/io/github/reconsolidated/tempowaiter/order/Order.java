package io.github.reconsolidated.tempowaiter.order;

import io.github.reconsolidated.tempowaiter.order.orderEntry.OrderEntry;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
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
    private LocalDateTime orderedAt;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime fulfilledAt;
}
