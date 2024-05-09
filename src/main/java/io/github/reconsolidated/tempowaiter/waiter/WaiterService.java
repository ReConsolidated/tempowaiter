package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.company.Company;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItemDto;
import io.github.reconsolidated.tempowaiter.infrastracture.email.EmailService;
import io.github.reconsolidated.tempowaiter.performanceData.events.TempoEvent;
import io.github.reconsolidated.tempowaiter.performanceData.events.TempoEventDto;
import io.github.reconsolidated.tempowaiter.performanceData.events.TempoEventService;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WaiterService {
    private final WaiterRequestRepository waiterRequestRepository;
    private final Notifier webSocketNotifier;
    private final EmailService emailService;
    private final CompanyService companyService;
    private final TempoEventService tempoEventService;

    private Optional<WaiterRequest> findByStateNotAndTableIdAndType(RequestState requestState, Long tableId, String type) {
        List<WaiterRequest> list = waiterRequestRepository.findByStateNotAndTableIdAndType(requestState, tableId, type);
        if (list.isEmpty()) {
            return Optional.empty();
        }

        // There was a bug that somehow created multiple requests for the same table
        // We need to delete all but the newest one
        list.sort((r1, r2) -> Long.compare(r2.getRequestedAt(), r1.getRequestedAt()));
        WaiterRequest newestRequest = list.get(0);
        if (list.size() > 1) {
            list.subList(1, list.size()).forEach(waiterRequestRepository::delete);
        }

        return Optional.of(newestRequest);
    }

    @Scheduled(fixedRate = 50000)
    @Transactional
    public void remindWaiter() {
        List<WaiterRequest> unresolvedRequests = waiterRequestRepository.findByState(RequestState.WAITING);
        List<Long> companyIds = unresolvedRequests.stream().map(WaiterRequest::getCompanyId).distinct().toList();
        for (Long companyId : companyIds) {
            WaiterRequest latestRequest = unresolvedRequests.stream()
                    .filter(request -> request.getCompanyId().equals(companyId))
                    .max(Comparator.comparing((request) -> {
                        if (request.getLastNotificationAt() == null) {
                            return LocalDateTime.ofEpochSecond(request.getRequestedAt()/1000,
                                    0, ZoneOffset.ofHours(0));
                        }
                        return request.getLastNotificationAt();
                    }))
                    .orElseThrow();

            if (latestRequest.getLastNotificationAt() != null &&
                    LocalDateTime.now().isAfter(latestRequest.getLastNotificationAt().plusSeconds(30))) {
                latestRequest.setLastNotificationAt(LocalDateTime.now());
                waiterRequestRepository.save(latestRequest);
                webSocketNotifier.sendRequestsNotification(companyId, latestRequest, true);
            }
        }
        Map<Long, Integer> companyOldRequestCount = unresolvedRequests.stream()
                .filter(request -> request.getEmailReportedAt() == null)
                .filter(request -> System.currentTimeMillis() - request.getRequestedAt() > 600000)
                .peek((request) -> {
                    request.setEmailReportedAt(LocalDateTime.now());
                    waiterRequestRepository.save(request);
                })
                .collect(Collectors.groupingBy(WaiterRequest::getCompanyId, Collectors.summingInt(request -> 1)));

        for (Long companyId : companyOldRequestCount.keySet()) {
            Integer count = companyOldRequestCount.get(companyId);
            Company company = companyService.getById(companyId);
            if (count != null && count > 0) {
                sendEmail(companyId, company, count, unresolvedRequests);
            }
        }
    }

    private void sendEmail(Long companyId, Company company, Integer count, List<WaiterRequest> unresolvedRequests) {
        String subject = company.getName() + " - zadzwoń, nieobsłużone zgłoszenia: " + count;
        StringBuilder content = new StringBuilder();
        content.append("Instrukcja obsługi dostępna tutaj: https://tempowaiter.com/instrukcja-obslugi<br/>");
        content.append("Nowe zgłoszenia czekające na rozpatrzenie w restauracji ")
                .append(company.getName()).append(":<br/>");
        for (WaiterRequest request : unresolvedRequests) {
            if (request.getCompanyId().equals(companyId)) {
                Date date = new Date(request.getRequestedAt());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
                String formattedDate = sdf.format(date);
                content.append("Stolik ")
                        .append(request.getTableName()).append(" - ")
                        .append(request.getType())
                        .append(" - ")
                        .append(formattedDate)
                        .append("<br/>");
            }
        }
        List<String> mailingList = List.of("gracjanpasik@gmail.com", "marekluksin@gmail.com", "maksym1305@gmail.com");
        for (String email : mailingList) {
            emailService.sendSimpleMessage(email, subject, content.toString());
        }
    }

    public WaiterRequest callToTable(String requestType, TableInfo tableInfo, Long cardId, String additionalData) {
        Optional<WaiterRequest> existing = findByStateNotAndTableIdAndType(RequestState.DONE, tableInfo.getTableId(), requestType);
        if (existing.isPresent()) {
            return existing.get();
        }
        WaiterRequest request = new WaiterRequest();
        request.setRequestedAt(System.currentTimeMillis());
        request.setType(requestType);
        request.setCardId(cardId);
        request.setCompanyId(tableInfo.getCompanyId());
        request.setTableId(tableInfo.getTableId());
        request.setState(RequestState.WAITING);
        request.setTableName(tableInfo.getTableDisplayName());
        request.setAdditionalData(additionalData);
        request.setLastNotificationAt(LocalDateTime.now());
        WaiterRequest result = waiterRequestRepository.save(request);
        webSocketNotifier.sendRequestsNotification(tableInfo.getCompanyId(), request, true);
        return result;
    }

    public WaiterRequest updateCallToTable(String requestType, TableInfo tableInfo, Long cardId, String additionalData) {
        Optional<WaiterRequest> existing = findByStateNotAndTableIdAndType(RequestState.DONE, tableInfo.getTableId(), requestType);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("You do not have any active request, so you can't update one");
        }
        WaiterRequest request = existing.get();
        request.setType(requestType);
        request.setAdditionalData(additionalData);
        request = waiterRequestRepository.save(request);
        webSocketNotifier.sendRequestsNotification(tableInfo.getCompanyId(), request, false);
        return request;
    }

    public List<WaiterRequest> getRequests(Long appUserId, Long companyId) {
        List<WaiterRequest> allCompanyRequests = waiterRequestRepository.findByStateNotAndCompanyIdEquals(RequestState.DONE, companyId);
        List<WaiterRequest> waiterRequests = allCompanyRequests.stream().filter(
                request -> !request.getState().equals(RequestState.IN_PROGRESS)
                        || request.getInProgressWaiterAppUserId().equals(appUserId))
                .toList();

        Thread reportRequestsViewed = new Thread(() -> {
            companyService.onRequestsViewed(companyId);
        });
        reportRequestsViewed.start();

        return waiterRequests.stream().sorted((request1, request2) -> {
            int score1 = scoreRequest(request1);
            int score2 = scoreRequest(request2);
            return score2 - score1;
        }).collect(Collectors.toList());
    }

    public List<WaiterRequest> getRequests(Long tableId) {
        return waiterRequestRepository.findByStateNotAndTableId(RequestState.DONE, tableId);
    }

    private int scoreRequest(WaiterRequest request) {
        int score = 0;
        if (request.getState().equals(RequestState.IN_PROGRESS)) {
            score += 3600; // 1 hour extra scoring if it's in progress
        }
        score += (System.currentTimeMillis() - request.getRequestedAt()) / 1000; // each second is 1 point
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
            throw new RuntimeException("Cannot set state to " + state + " because current state is "
                    + request.getState() + " for request id " + requestId);
        }
        request.setState(state);
        if (state.equals(RequestState.IN_PROGRESS)) {
            request.setPutInProgressAt(System.currentTimeMillis());
            request.setInProgressWaiterAppUserId(currentUserId);
        } else if (state.equals(RequestState.DONE)) {
            request.setResolvedAt(System.currentTimeMillis());
        }
        webSocketNotifier.sendRequestsNotification(companyId, request, false);
        webSocketNotifier.sendTableNotification(request.getTableId(), state.name());
        return waiterRequestRepository.save(request);
    }

    public boolean deleteRequest(Long tableId, String callType) {
        Optional<WaiterRequest> request = findByStateNotAndTableIdAndType(RequestState.DONE, tableId, callType);
        if (request.isPresent()) {
            waiterRequestRepository.delete(request.get());
            webSocketNotifier.sendRequestsNotification(request.get().getCompanyId(), request.get(), false);
            webSocketNotifier.sendTableNotification(request.get().getTableId(), "CANCELLED");
            return true;
        }
        return false;
    }
}
