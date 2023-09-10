package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.waiter.RequestState;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@AllArgsConstructor
public class TableController {
    private final TableService tableService;
    private final WaiterService waiterService;

    @GetMapping("/public/start_session")
    public ResponseEntity<TableInfo> startSession(HttpSession session, @RequestParam Long cardId, @RequestParam Long ctr) {
        TableInfo tableInfo = tableService.startSession(session.getId(), cardId, ctr);
        return ResponseEntity.ok(tableInfo);
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

    @GetMapping("/public/call_state")
    public ResponseEntity<WaiterRequest> callState(HttpSession session, @RequestParam Long cardId, @RequestParam Long requestId) {
        WaiterRequest request = tableService.getRequest(session.getId(), cardId, requestId).orElseThrow();
        return ResponseEntity.ok(request);
    }
}
