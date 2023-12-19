package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.card.Card;
import io.github.reconsolidated.tempowaiter.card.CardService;
import io.github.reconsolidated.tempowaiter.company.Company;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.table.exceptions.OutdatedTableRequestException;
import io.github.reconsolidated.tempowaiter.table.exceptions.SessionExpiredException;
import io.github.reconsolidated.tempowaiter.table.exceptions.TableNotFoundException;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@AllArgsConstructor
@Service
public class TableService {
    private static final Logger log = Logger.getLogger(TableService.class.getName());
    public static final long SESSION_EXPIRATION_TIME_MINUTES = 15;

    private final TableSessionRepository sessionRepository;
    private final TableInfoRepository tableInfoRepository;
    private final WaiterService waiterService;
    private final CardService cardService;
    private final TableInfoMapper tableInfoMapper;
    private final CompanyService companyService;

    public TableInfoDto startSession(String sessionId, String cardUid, Long ctr) {
        Long cardId = cardService.getCardId(cardUid);
        log.info("Card ID %d scanned".formatted(cardId));
        Optional<TableSession> tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(sessionId, cardId, LocalDateTime.now());

        if (tableSession.isPresent()) {
            return tableInfoMapper.toDto(
                    tableInfoRepository.findByCardIdEquals(cardId).orElseThrow(
                            () -> new TableNotFoundException(cardId)));
        }

        TableInfo tableInfo = tableInfoRepository.findByCardIdEquals(cardId).orElseThrow(() -> new TableNotFoundException(cardId));
        if (tableInfo.getLastCtr() >= ctr) {
            throw new OutdatedTableRequestException(cardId);
        }

        Optional<TableSession> overwrittenSession = sessionRepository
                .findByCardIdAndIsOverwrittenFalseAndExpirationDateGreaterThan(cardId, LocalDateTime.now());
        if (overwrittenSession.isPresent()) {
            overwrittenSession.get().setOverwritten(true);
            sessionRepository.save(overwrittenSession.get());
        }

        Company company = companyService.getById(tableInfo.getCompanyId());

        tableInfo.setLastCtr(ctr);
        tableInfoRepository.save(tableInfo);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(SESSION_EXPIRATION_TIME_MINUTES);
        TableSession newTableSession = new TableSession(tableInfo, company, sessionId, expirationDate, false);
        sessionRepository.save(newTableSession);
        return tableInfoMapper.toDto(tableInfo);
    }

    public TableInfoDto startSessionAdmin(AppUser currentUser, String sessionId, Long tableId) {
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        if (!currentUser.getCompanyId().equals(tableInfo.getCompanyId())) {
            throw new TableNotFoundException(tableId);
        }
        Long cardId = tableInfo.getCardId();
        if (cardId == null) {
            throw new IllegalArgumentException("Table doesn't have a card assigned.");
        }
        Optional<TableSession> tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(sessionId, cardId, LocalDateTime.now());

        if (tableSession.isPresent()) {
            return tableInfoMapper.toDto(tableInfo);
        }

        Optional<TableSession> overwrittenSession = sessionRepository
                .findByCardIdAndIsOverwrittenFalseAndExpirationDateGreaterThan(cardId, LocalDateTime.now());
        if (overwrittenSession.isPresent()) {
            overwrittenSession.get().setOverwritten(true);
            sessionRepository.save(overwrittenSession.get());
        }

        Company company = companyService.getById(tableInfo.getCompanyId());

        tableInfoRepository.save(tableInfo);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(SESSION_EXPIRATION_TIME_MINUTES);
        TableSession newTableSession = new TableSession(tableInfo, company, sessionId, expirationDate, false);
        sessionRepository.save(newTableSession);
        return tableInfoMapper.toDto(tableInfo);
    }

    public WaiterRequest callWaiter(String sessionId, String requestType, Long cardId) {
        // tableSession has to be polled for security to check if session exists
        TableSession tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(
                        sessionId,
                        cardId,
                        LocalDateTime.now())
                .orElseThrow(SessionExpiredException::new);
        tableSession.setLastRequestAt(System.currentTimeMillis());
        TableInfo tableInfo = tableInfoRepository.findByCardIdEquals(cardId).orElseThrow(() -> new TableNotFoundException(cardId));
        return waiterService.callToTable(requestType, tableInfo, cardId);
    }

    public TableInfo setCardId(Long companyId, Long tableId, @Nullable Long cardId) {
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        Long oldCardId = tableInfo.getCardId();
        if (!tableInfo.getCompanyId().equals(companyId)) {
            throw new TableNotFoundException(tableId);
        }
        if (cardId == null) {
            if (oldCardId != null && oldCardId != 0) {
                cardService.removeCardTableId(oldCardId);
            }
            tableInfo.setCardId(null);
            tableInfoRepository.save(tableInfo);
        } else {
            if (tableInfoRepository.findByCardIdEquals(cardId).isPresent()) {
                throw new IllegalArgumentException("This card is already assigned to a table.");
            }
            Long cardCompanyId = cardService.getCardCompanyId(cardId);
            if (cardCompanyId == null) {
                throw new IllegalArgumentException("Card doesn't have company id set.");
            }
            if (!cardCompanyId.equals(companyId)) {
                throw new IllegalArgumentException("Card %d is not assigned to your company.".formatted(cardId));
            }
            if (oldCardId != null && oldCardId != 0) {
                cardService.removeCardTableId(oldCardId);
            }
            cardService.setCardTableId(cardId, tableId);
            tableInfo.setCardId(cardId);
            tableInfoRepository.save(tableInfo);
        }
        return tableInfo;
    }

    public TableInfo removeCardId(Long companyId, Long tableId, Long cardId) {
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        if (!tableInfo.getCompanyId().equals(companyId)) {
            throw new TableNotFoundException(tableId);
        }
        cardService.removeCardTableId(cardId);
        tableInfo.setCardId(cardId);
        tableInfoRepository.save(tableInfo);
        return tableInfo;
    }

    public TableInfo createTable(@NotNull Long companyId, String tableDisplayName) {
        TableInfo tableInfo = new TableInfo(null, null, companyId, tableDisplayName, 0L);
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
        return waiterService.deleteRequest(tableSession.getTableId());
    }

    public List<TableInfo> listTables(Long companyId) {
        return tableInfoRepository.findAllByCompanyIdEquals(companyId);
    }

    public Optional<WaiterRequest> getRequest(String sessionId, Long cardId) {
        TableSession tableSession = sessionRepository
                .findBySessionIdAndCardIdAndExpirationDateGreaterThanAndIsOverwrittenFalse(
                        sessionId,
                        cardId,
                        LocalDateTime.now())
                .orElseThrow(SessionExpiredException::new);
        return waiterService.getRequest(tableSession.getTableId());
    }

    public void deleteTable(Long companyId, Long tableId) {
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        if (!tableInfo.getCompanyId().equals(companyId)) {
            throw new TableNotFoundException(tableId);
        }
        Optional<Card> card = cardService.getCardByTable(tableId);
        card.ifPresent(value -> cardService.removeCardTableId(value.getId()));
        tableInfoRepository.deleteById(tableId);
    }

    public TableInfo updateTable(Long currentUserCompanyId, Long tableId, String tableDisplayName) {
        TableInfo tableInfo = tableInfoRepository.findById(tableId).orElseThrow(() -> new TableNotFoundException(tableId));
        if (!tableInfo.getCompanyId().equals(currentUserCompanyId)) {
            throw new TableNotFoundException(tableId);
        }
        tableInfo.setTableDisplayName(tableDisplayName);
        tableInfoRepository.save(tableInfo);
        return tableInfo;
    }

    public void clearCardData(Long cardId) {
        tableInfoRepository.findByCardIdEquals(cardId).ifPresent((tableInfo) -> {
            tableInfo.setCardId(null);
            tableInfoRepository.save(tableInfo);
        });
    }
}
