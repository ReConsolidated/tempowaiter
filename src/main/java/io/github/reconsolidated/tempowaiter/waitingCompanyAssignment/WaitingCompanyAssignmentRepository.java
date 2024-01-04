package io.github.reconsolidated.tempowaiter.waitingCompanyAssignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingCompanyAssignmentRepository extends JpaRepository<WaitingCompanyAssignment, Long> {
    void deleteByEmail(String email);
}
