package io.github.reconsolidated.tempowaiter.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByCompanyId(Long companyId);

    Optional<Card> findByCardUid(String cardUid);

    Optional<Card> findByTableId(Long tableId);
}
