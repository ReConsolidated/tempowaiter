package io.github.reconsolidated.tempowaiter.waiter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaiterRequestRepository extends JpaRepository<WaiterRequest, Long> {
    List<WaiterRequest> findByStateNotAndCompanyIdEquals(RequestState state, Long companyId);

    Optional<WaiterRequest> findByStateNotAndTableId(RequestState done, Long tableId);
}
