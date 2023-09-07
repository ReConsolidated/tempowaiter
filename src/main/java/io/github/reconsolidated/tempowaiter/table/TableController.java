package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.waiter.RequestState;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/public")
@AllArgsConstructor
public class TableController {
    private final TableService tableService;
    private final WaiterService waiterService;

    @GetMapping("/start_session")
    public ResponseEntity<TableInfo> startSession(HttpSession session, @RequestParam Long uid, @RequestParam Long ctr) {
        TableInfo tableInfo = tableService.startSession(session.getId(), uid, ctr);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/table")
    public ResponseEntity<TableInfo> createTable(@RequestParam Long companyId, @RequestParam Long tableId, @RequestParam String tableDisplayName) {
        TableInfo tableInfo = tableService.createTable(companyId, tableId, tableDisplayName);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/call")
    public ResponseEntity<CallState> callWaiter(HttpSession session,
                                                 @RequestParam Long tableId,
                                                 @RequestParam String callType) {
        TableInfo tableInfo = tableService.callWaiter(session.getId(), callType, tableId);
        CallState result = new CallState(tableInfo.getTableId(), callType, RequestState.WAITING);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/call_state")
    public ResponseEntity<CallState> callState(HttpSession session, @RequestParam Long requestId) {
        WaiterRequest request = waiterService.getRequest(session.getId(), requestId).orElseThrow();
        CallState result = new CallState(request.getTableId(), request.getType(), request.getState());
        return ResponseEntity.ok(result);
    }
}
