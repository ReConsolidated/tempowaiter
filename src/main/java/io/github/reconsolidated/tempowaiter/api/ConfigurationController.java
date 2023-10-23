package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserRole;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.card.Card;
import io.github.reconsolidated.tempowaiter.card.CardService;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import io.github.reconsolidated.tempowaiter.table.TableService;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ConfigurationController {
    private final TableService tableService;
    private final CardService cardService;

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
    public ResponseEntity<TableInfo> addCardToTable(@CurrentUser AppUser currentUser, @PathVariable Long tableId, @RequestParam Long cardId) {
        TableInfo tableInfo = tableService.setCardId(currentUser.getCompanyId(), tableId, cardId);
        return ResponseEntity.ok(tableInfo);
    }

    @DeleteMapping("/table/{tableId}/cardId")
    public ResponseEntity<TableInfo> removeCardFromTable(@CurrentUser AppUser currentUser, @PathVariable Long tableId, @RequestParam Long cardId) {
        TableInfo tableInfo = tableService.removeCardId(currentUser.getCompanyId(), tableId, cardId);
        return ResponseEntity.ok(tableInfo);
    }

    @PostMapping("/cards/{cardId}")
    public ResponseEntity<Card> addCardToCompany(@CurrentUser AppUser currentUser, @PathVariable Long cardId, @RequestParam Long companyId) {
        if (!currentUser.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("This endpoint is for Admins only");
        }
        return ResponseEntity.ok(cardService.setCardCompanyId(cardId, companyId));
    }

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> listCards(@CurrentUser AppUser currentUser, @RequestParam(required = false) Long companyId) {
        if (companyId == null && !currentUser.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("List of all cards is for Admins only");
        }
        if (companyId != null) {
            return ResponseEntity.ok(cardService.getCards(companyId));
        } else {
            return ResponseEntity.ok(cardService.getCards());
        }
    }
}
