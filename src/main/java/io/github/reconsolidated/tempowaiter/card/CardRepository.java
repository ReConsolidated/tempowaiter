package io.github.reconsolidated.tempowaiter.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByCompanyId(Long companyId);
}
