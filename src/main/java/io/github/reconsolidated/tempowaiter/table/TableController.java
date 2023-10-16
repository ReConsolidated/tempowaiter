package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class TableController {
    private final TableService tableService;
    private final WaiterService waiterService;

    @GetMapping("/public/session")
    public ResponseEntity<?> getSessionId(@RequestParam String sessionId) {
        return ResponseEntity.ok("Session id: " + sessionId);
    }

    @PostMapping("/public/start_session")
    public ResponseEntity<TableInfo> startSession(@RequestParam Long cardId, @RequestParam Long ctr) {
        String sessionId = UUID.randomUUID().toString();
        TableInfo tableInfo = tableService.startSession(sessionId, cardId, ctr);
        return ResponseEntity.ok().headers(headers -> headers.add("sessionId", sessionId)).body(tableInfo);
    }

    @GetMapping("/public/session_info")
    public ResponseEntity<TableSession> sessionInfo(@RequestParam String sessionId) {
        Optional<TableSession> tableSession = tableService.getSession(sessionId);
        return ResponseEntity.of(tableSession);
    }

    @GetMapping("/tables")
    public ResponseEntity<List<TableInfo>> listTables(@CurrentUser AppUser currentUser) {
        List<TableInfo> tableInfos = tableService.listTables(currentUser.getCompanyId());
        return ResponseEntity.ok(tableInfos);
    }

    @PostMapping("/table")
    public ResponseEntity<TableInfo> createTable(@CurrentUser AppUser currentUser, @RequestParam String tableDisplayName) {
        TableInfo tableInfo = tableService.createTable(currentUser.getCompanyId(), tableDisplayName);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/table/{tableId}/cardId")
    public ResponseEntity<TableInfo> addCard(@CurrentUser AppUser currentUser, @PathVariable Long tableId, @RequestParam Long cardId) {
        TableInfo tableInfo = tableService.setCardId(currentUser.getCompanyId(), tableId, cardId);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/public/call")
    public ResponseEntity<WaiterRequest> callWaiter(HttpSession session,
                                                 @RequestParam Long cardId,
                                                 @RequestParam String callType) {
        return ResponseEntity.ok(tableService.callWaiter(session.getId(), callType, cardId));
    }

    @PostMapping("/public/cancel_call")
    public ResponseEntity<?> cancelCall(HttpSession session,
                                                    @RequestParam Long cardId) {
        if (tableService.cancelCall(session.getId(), cardId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/public/call_state")
    public ResponseEntity<WaiterRequest> callState(HttpSession session, @RequestParam Long cardId, @RequestParam Long requestId) {
        WaiterRequest request = tableService.getRequest(session.getId(), cardId, requestId).orElseThrow();
        return ResponseEntity.ok(request);
    }
}
