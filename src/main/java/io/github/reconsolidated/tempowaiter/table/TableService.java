package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.table.exceptions.OutdatedTableRequestException;
import io.github.reconsolidated.tempowaiter.table.exceptions.SessionExpiredException;
import io.github.reconsolidated.tempowaiter.table.exceptions.TableNotFoundException;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TableService {
    private final TableSessionRepository sessionRepository;
    private final TableInfoRepository tableInfoRepository;
    private final WaiterService waiterService;

    public TableInfo startSession(String sessionId, Long cardId, Long ctr) {
        Optional<TableSession> tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(sessionId, cardId, LocalDateTime.now());
        if (tableSession.isPresent()) {
            return tableInfoRepository.findByCardIdEquals(cardId).orElseThrow(() -> new TableNotFoundException(cardId));
        }

        TableInfo tableInfo = tableInfoRepository.findByCardIdEquals(cardId).orElseThrow(() -> new TableNotFoundException(cardId));
        if (tableInfo.getLastCtr() >= ctr) {
            throw new OutdatedTableRequestException();
        }

        Optional<TableSession> overwrittenSession = sessionRepository
                .findByCardIdAndIsOverwrittenFalseAndExpirationDateGreaterThan(cardId, LocalDateTime.now());
        if (overwrittenSession.isPresent()) {
            overwrittenSession.get().setOverwritten(true);
            sessionRepository.save(overwrittenSession.get());
        }

        tableInfo.setLastCtr(ctr);
        tableInfoRepository.save(tableInfo);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(180);
        TableSession newTableSession = new TableSession(tableInfo, sessionId, expirationDate, false);
        sessionRepository.save(newTableSession);
        return tableInfo;
    }

    public Optional<WaiterRequest> getRequest(String sessionId, Long cardId, Long requestId) {
        TableSession tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(
                        sessionId,
                        cardId,
                        LocalDateTime.now())
                .orElseThrow(SessionExpiredException::new);
        return waiterService.getRequest(sessionId, requestId);
    }

    public WaiterRequest callWaiter(String sessionId, String requestType, Long cardId) {
        // tableSession has to be polled for security to check if session exists
        TableSession tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(
                        sessionId,
                        cardId,
                        LocalDateTime.now())
                .orElseThrow(SessionExpiredException::new);
        TableInfo tableInfo = tableInfoRepository.findByCardIdEquals(cardId).orElseThrow(() -> new TableNotFoundException(cardId));
        return waiterService.callToTable(sessionId, requestType, tableInfo, cardId);
    }

    public TableInfo setCardId(Long companyId, Long tableId, Long cardId) {
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        if (!tableInfo.getCompanyId().equals(companyId)) {
            throw new TableNotFoundException(tableId);
        }
        tableInfo.setCardId(cardId);
        tableInfoRepository.save(tableInfo);
        return tableInfo;
    }

    public TableInfo removeCardId(Long companyId, Long tableId, Long cardId) {
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        if (!tableInfo.getCompanyId().equals(companyId)) {
            throw new TableNotFoundException(tableId);
        }
        tableInfo.setCardId(cardId);
        tableInfoRepository.save(tableInfo);
        return tableInfo;
    }

    public TableInfo createTable(@NotNull Long companyId, String tableDisplayName) {
        TableInfo tableInfo = new TableInfo(null, 0L, companyId, tableDisplayName, 0L);
        tableInfo = tableInfoRepository.save(tableInfo);
        return tableInfo;
    }

    public Optional<TableSession> getSession(String sessionId) {
        return sessionRepository.findBySessionIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(sessionId, LocalDateTime.now());
    }

    public boolean cancelCall(String sessionId, Long cardId) {
        TableSession tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(
                        sessionId,
                        cardId,
                        LocalDateTime.now())
                .orElseThrow(SessionExpiredException::new);
        return waiterService.deleteRequest(tableSession.getId());
    }

    public List<TableInfo> listTables(Long companyId) {
        return tableInfoRepository.findAllByCompanyIdEquals(companyId);
    }

    public List<WaiterRequest> getRequests(String sessionId, Long cardId) {
        TableSession tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(
                        sessionId,
                        cardId,
                        LocalDateTime.now())
                .orElseThrow(SessionExpiredException::new);
        return waiterService.getRequests(sessionId);
    }
}
