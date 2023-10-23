package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ConfigurationController {
    private final TableService tableService;
    private final WaiterService waiterService;

    @PostMapping("/table")
    public ResponseEntity<TableInfo> createTable(@CurrentUser AppUser currentUser, @RequestParam String tableDisplayName) {
        TableInfo tableInfo = tableService.createTable(currentUser.getCompanyId(), tableDisplayName);
        return ResponseEntity.ok(tableInfo);
    }

    @GetMapping("/tables")
    public ResponseEntity<List<TableInfo>> listTables(@CurrentUser AppUser currentUser) {
        List<TableInfo> tableInfos = tableService.listTables(currentUser.getCompanyId());
        return ResponseEntity.ok(tableInfos);
    }

    @PostMapping("/table/{tableId}/cardId")
    public ResponseEntity<TableInfo> addCard(@CurrentUser AppUser currentUser, @PathVariable Long tableId, @RequestParam Long cardId) {
        TableInfo tableInfo = tableService.setCardId(currentUser.getCompanyId(), tableId, cardId);
        return ResponseEntity.ok(tableInfo);
    }
}
