package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.table.TableInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WaiterService {
    private final WaiterRequestRepository waiterRequestRepository;

    public WaiterRequest callToTable(String clientSessionId, String requestType, TableInfo tableInfo, Long cardId) {
        Optional<WaiterRequest> existing = waiterRequestRepository.findByStateNotAndTableId(RequestState.DONE, tableInfo.getTableId());
        if (existing.isPresent()) {
            return existing.get();
        }
        WaiterRequest request = new WaiterRequest();
        request.setClientSessionId(clientSessionId);
        request.setRequestedAt(System.currentTimeMillis());
        request.setType(requestType);
        request.setCardId(cardId);
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

    public List<WaiterRequest> getRequests(String sessionId) {
        return waiterRequestRepository.findByClientSessionIdEquals(sessionId);
    }

    private int scoreRequest(WaiterRequest request) {
        int score = 0;
        if (request.getState().equals(RequestState.IN_PROGRESS)) {
            score += 600; // 10 minutes extra if it's in progress
        }
        score += (request.getRequestedAt() - System.currentTimeMillis()) / 1000; // each second is 1 point
        return score;
    }

    public Optional<WaiterRequest> getRequest(String sessionId, Long requestId) {
        WaiterRequest request = waiterRequestRepository.findById(requestId).orElseThrow();
        return Optional.of(request);
    }

    public WaiterRequest setRequestState(Long currentUserId, Long companyId, Long requestId, RequestState state) {
        WaiterRequest request = waiterRequestRepository.findById(requestId).orElseThrow();
        if (!request.getCompanyId().equals(companyId)) {
            throw new RuntimeException("Request does not belong to this company");
        }
        if (request.getState().ordinal() >= state.ordinal()) {
            throw new RuntimeException("Cannot set state to " + state + " because current state is " + request.getState());
        }
        request.setState(state);
        if (state.equals(RequestState.IN_PROGRESS)) {
            request.setPutInProgressAt(System.currentTimeMillis());
            request.setInProgressWaiterAppUserId(currentUserId);
        } else if (state.equals(RequestState.DONE)) {
            request.setResolvedAt(System.currentTimeMillis());
        }

        return waiterRequestRepository.save(request);
    }

    public boolean deleteRequest(String clientSessionId) {
        Optional<WaiterRequest> request = waiterRequestRepository.findByStateNotAndClientSessionId(RequestState.DONE, clientSessionId);
        if (request.isPresent()) {
            waiterRequestRepository.delete(request.get());
            return true;
        }
        return false;
    }
}
