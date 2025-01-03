package io.github.reconsolidated.tempowaiter.table;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TableInfoRepository extends JpaRepository<TableInfo, Long> {
    Optional<TableInfo> findByCardIdEquals(Long cardId);

    List<TableInfo> findAllByCompanyIdEquals(Long companyId);
}
