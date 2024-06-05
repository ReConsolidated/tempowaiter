package io.github.reconsolidated.tempowaiter.domain.waitingCompanyAssignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaitingCompanyAssignmentRepository extends JpaRepository<WaitingCompanyAssignment, Long> {
    void deleteByEmail(String email);

    Optional<WaitingCompanyAssignment> findByEmail(String email);
}
