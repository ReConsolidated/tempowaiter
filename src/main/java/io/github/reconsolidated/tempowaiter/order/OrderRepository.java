package io.github.reconsolidated.tempowaiter.order;

import io.github.reconsolidated.tempowaiter.table.TableInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTableInfoAndFulfilledAtIsNull(TableInfo tableInfo);

    List<Order> findByTableInfoCompanyId(Long companyId);
}
