package io.github.reconsolidated.tempowaiter.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{
    List<MenuItem> findByCompanyId(Long companyId);
}
