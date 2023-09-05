package io.github.reconsolidated.tempowaiter.table;

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

    @GetMapping("/start_session")
    public ResponseEntity<TableInfo> startSession(HttpSession session, @RequestParam Long uid, @RequestParam Long ctr) {
        TableInfo tableInfo = tableService.startSession(session.getId(), uid, ctr);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/table")
    public ResponseEntity<TableInfo> createTable(@RequestParam Long companyId, @RequestParam String tableDisplayName) {
        TableInfo tableInfo = tableService.createTable(companyId, tableDisplayName);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/call")
    public ResponseEntity<CallResult> callWaiter(HttpSession session,
                                                 @RequestParam Long tableId,
                                                 @RequestParam String callType) {
        TableInfo tableInfo = tableService.callWaiter(session.getId(), callType, tableId);
        CallResult result = new CallResult(tableInfo.getTableId(), callType, LocalDateTime.now().plusSeconds(30));
        return ResponseEntity.ok(result);
    }
}
