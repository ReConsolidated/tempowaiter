package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.waiter.RequestState;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/table/{tableId}/addCard")
    public ResponseEntity<TableInfo> addCard(@CurrentUser AppUser currentUser, @RequestParam Long tableId, @RequestParam Long cardId) {
        TableInfo tableInfo = tableService.addCardId(currentUser.getCompanyId(), tableId, cardId);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/table/{tableId}/removeCard")
    public ResponseEntity<TableInfo> removeCard(@CurrentUser AppUser currentUser, @RequestParam Long tableId, @RequestParam Long cardId) {
        TableInfo tableInfo = tableService.removeCardId(currentUser.getCompanyId(), tableId, cardId);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/public/call")
    public ResponseEntity<CallState> callWaiter(HttpSession session,
                                                 @RequestParam Long cardId,
                                                 @RequestParam String callType) {
        TableInfo tableInfo = tableService.callWaiter(session.getId(), callType, cardId);
        CallState result = new CallState(tableInfo.getTableId(), callType, RequestState.WAITING);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/public/call_state")
    public ResponseEntity<CallState> callState(HttpSession session, @RequestParam Long requestId) {
        WaiterRequest request = waiterService.getRequest(session.getId(), requestId).orElseThrow();
        CallState result = new CallState(request.getTableId(), request.getType(), request.getState());
        return ResponseEntity.ok(result);
    }
}
