package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.table.exceptions.OutdatedTableRequestException;
import io.github.reconsolidated.tempowaiter.table.exceptions.SessionExpiredException;
import io.github.reconsolidated.tempowaiter.table.exceptions.TableNotFoundException;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TableService {
    private final TableSessionRepository sessionRepository;
    private final TableInfoRepository tableInfoRepository;
    private final WaiterService waiterService;

    public TableInfo startSession(String sessionId, Long tableId, Long ctr) {
        Optional<TableSession> tableSession = sessionRepository
                .findBySessionIdEqualsAndTableIdEqualsAndExpirationDateIsGreaterThan(sessionId, tableId, LocalDateTime.now());
        if (tableSession.isPresent()) {
            return tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        }
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        if (tableInfo.getLastCtr() >= ctr) {
            throw new OutdatedTableRequestException();
        }
        tableInfo.setLastCtr(ctr);
        tableInfoRepository.save(tableInfo);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(1);
        TableSession newTableSession = new TableSession(tableInfo, sessionId, expirationDate);
        sessionRepository.save(newTableSession);
        return tableInfo;
    }

    public TableInfo callWaiter(String sessionId, Long tableId) {
        // tableSession has to be polled for security to check if session exists
        TableSession tableSession = sessionRepository
                .findBySessionIdEqualsAndTableIdEqualsAndExpirationDateIsGreaterThan(
                        sessionId,
                        tableId,
                        LocalDateTime.now())
                .orElseThrow(SessionExpiredException::new);
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        waiterService.callToTable(tableInfo);
        return tableInfo;
    }

    public TableInfo createTable(Long companyId, String tableDisplayName) {
        TableInfo tableInfo = new TableInfo(null, companyId, tableDisplayName, 0L);
        tableInfo = tableInfoRepository.save(tableInfo);
        return tableInfo;
    }
}
