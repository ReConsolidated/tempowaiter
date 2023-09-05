package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.table.TableInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WaiterService {
    private final WaiterRequestRepository waiterRequestRepository;

    public WaiterRequest callToTable(String requestType, TableInfo tableInfo) {
        WaiterRequest request = new WaiterRequest();
        request.setRequestedAt(System.currentTimeMillis());
        request.setType(requestType);
        request.setCompanyId(tableInfo.getCompanyId());
        request.setTableId(tableInfo.getTableId());
        request.setState(RequestState.WAITING);
        return waiterRequestRepository.save(request);
    }

    public List<WaiterRequest> getRequests(Long appUserId, Long companyId) {
        List<WaiterRequest> allCompanyRequests = waiterRequestRepository.findByStateNotAndCompanyIdEquals(RequestState.DONE, companyId);
        List<WaiterRequest> waiterRequests = allCompanyRequests.stream().filter(
                request -> !request.getState().equals(RequestState.IN_PROGRESS) || request.getInProgressWaiterAppUserId() == appUserId)
                .toList();

        return waiterRequests.stream().sorted((request1, request2) -> {
            int score1 = scoreRequest(request1);
            int score2 = scoreRequest(request2);
            return score1 - score2;
        }).collect(Collectors.toList());
    }

    private int scoreRequest(WaiterRequest request) {
        int score = 0;
        if (request.getState().equals(RequestState.IN_PROGRESS)) {
            score += 600; // 10 minutes extra if it's in progress
        }
        score += (request.getRequestedAt() - System.currentTimeMillis()) / 1000; // each second is 1 point
        return score;
    }
}
