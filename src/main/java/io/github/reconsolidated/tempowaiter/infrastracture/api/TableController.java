package io.github.reconsolidated.tempowaiter.infrastracture.api;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.domain.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItemDto;
import io.github.reconsolidated.tempowaiter.domain.table.TableInfoDto;
import io.github.reconsolidated.tempowaiter.application.table.TableService;
import io.github.reconsolidated.tempowaiter.domain.table.TableSession;
import io.github.reconsolidated.tempowaiter.domain.table.TableSessionDto;
import io.github.reconsolidated.tempowaiter.infrastracture.ntagDecryption.NtagDecryptionService;
import io.github.reconsolidated.tempowaiter.infrastracture.ntagDecryption.NtagInfo;
import io.github.reconsolidated.tempowaiter.table.*;
import io.github.reconsolidated.tempowaiter.domain.waiter.WaiterRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class TableController {

    private final TableService tableService;
    private final NtagDecryptionService ntagDecryptionService;


    @PostMapping("/public/start_session")
    public ResponseEntity<?> startSession(@RequestParam String e, @RequestParam String c) {
        NtagInfo ntagInfo = ntagDecryptionService.decryptNtag(e);
        String sessionId = UUID.randomUUID().toString();
        TableInfoDto tableInfo = tableService.startSession(sessionId, ntagInfo.getCardId(), ntagInfo.getCtr());
        ObjectNode responseBody = JsonNodeFactory.instance.objectNode();
        responseBody.set("tableInfo", JsonNodeFactory.instance.pojoNode(tableInfo));
        responseBody.put("sessionId", sessionId);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/start_session_admin")
    public ResponseEntity<?> startSessionAdmin(@CurrentUser AppUser currentUser, Long tableId) {
        String sessionId = UUID.randomUUID().toString();
        TableInfoDto tableInfo = tableService.startSessionAdmin(currentUser, sessionId, tableId);
        ObjectNode responseBody = JsonNodeFactory.instance.objectNode();
        responseBody.set("tableInfo", JsonNodeFactory.instance.pojoNode(tableInfo));
        responseBody.put("sessionId", sessionId);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/public/session_info")
    public ResponseEntity<TableSessionDto> sessionInfo(@RequestParam String sessionId) {
        Optional<TableSession> tableSession = tableService.getSession(sessionId);
        if (tableSession.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TableSessionDto(tableSession.get()));
    }

    @PostMapping("/public/call")
    public ResponseEntity<WaiterRequest> callWaiter(@RequestParam String sessionId,
                                                 @RequestParam Long cardId,
                                                 @RequestParam String callType,
                                                 @RequestParam(required = false) String additionalData) {
        return ResponseEntity.ok(tableService.callWaiter(sessionId, callType, cardId, additionalData));
    }

    @PatchMapping("/public/call")
    public ResponseEntity<WaiterRequest> updateWaiterCall(@RequestParam String sessionId,
                                                    @RequestParam Long cardId,
                                                    @RequestParam String callType,
                                                    @RequestParam(required = false) String additionalData) {
        return ResponseEntity.ok(tableService.updateWaiterCall(sessionId, callType, cardId, additionalData));
    }

    @GetMapping("/public/menu-items")
    public List<MenuItemDto> getMenuItems(@RequestParam String sessionId,
                                          @RequestParam Long cardId) {
        return tableService.listMenuItems(sessionId, cardId);
    }

    @PostMapping("/public/cancel_call")
    public ResponseEntity<?> cancelCall(@RequestParam String sessionId,
                                                    @RequestParam Long cardId,
                                                    @RequestParam String callType) {
        if (tableService.cancelCall(sessionId, cardId, callType)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/public/call_state")
    public ResponseEntity<List<WaiterRequest>> callState(@RequestParam String sessionId, @RequestParam Long cardId) {
        return ResponseEntity.ok(tableService.getRequests(sessionId, cardId));
    }
}
