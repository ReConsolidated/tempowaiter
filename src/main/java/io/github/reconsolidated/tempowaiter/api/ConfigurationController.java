package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserRole;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.card.Card;
import io.github.reconsolidated.tempowaiter.card.CardService;
import io.github.reconsolidated.tempowaiter.company.Company;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.company.SingleStringDto;
import io.github.reconsolidated.tempowaiter.table.TableInfo;
import io.github.reconsolidated.tempowaiter.table.TableService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ConfigurationController {
    private final TableService tableService;
    private final CardService cardService;
    private final CompanyService companyService;

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

    @PutMapping("/tables/{tableId}")
    public ResponseEntity<TableInfo> updateTable(@CurrentUser AppUser currentUser, @PathVariable Long tableId, @RequestParam String tableDisplayName) {
        TableInfo tableInfo = tableService.updateTable(currentUser.getCompanyId(), tableId, tableDisplayName);
        return ResponseEntity.ok(tableInfo);
    }

    @DeleteMapping("/tables/{tableId}")
    public ResponseEntity<?> deleteTable(@CurrentUser AppUser currentUser, @PathVariable Long tableId) {
        tableService.deleteTable(currentUser.getCompanyId(), tableId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/table/{tableId}/cardId")
    public ResponseEntity<TableInfo> setTableCard(@CurrentUser AppUser currentUser,
                                                  @PathVariable Long tableId,
                                                  @RequestParam(required = false) Long cardId) {
        TableInfo tableInfo = tableService.setCardId(currentUser.getCompanyId(), tableId, cardId);
        return ResponseEntity.ok(tableInfo);
    }

    @DeleteMapping("/table/{tableId}/cardId")
    public ResponseEntity<TableInfo> removeCardFromTable(@CurrentUser AppUser currentUser, @PathVariable Long tableId, @RequestParam Long cardId) {
        TableInfo tableInfo = tableService.removeCardId(currentUser.getCompanyId(), tableId, cardId);
        return ResponseEntity.ok(tableInfo);
    }

    @PutMapping("/cards/{cardId}")
    public ResponseEntity<Card> addCardToCompany(@CurrentUser AppUser currentUser, @PathVariable Long cardId, @RequestParam Long companyId) {
        if (!currentUser.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("This endpoint is for Admins only");
        }
        return ResponseEntity.ok(cardService.setCardCompanyId(cardId, companyId));
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@CurrentUser AppUser currentUser, @PathVariable Long cardId) {
        if (!currentUser.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("This endpoint is for Admins only");
        }
        if (cardService.deleteCard(cardId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/public/cards")
    public ResponseEntity<Card> createCard(@RequestParam String e, @RequestParam String c) {
        return ResponseEntity.ok(cardService.createCard(e));
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

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> listCompanies(@CurrentUser AppUser currentUser) {
        if (!currentUser.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("List of all companies is for Admins only");
        }
        return ResponseEntity.ok(companyService.listCompanies());
    }

    @GetMapping("/companies/{companyId}")
    public ResponseEntity<Company> getCompany(@CurrentUser AppUser currentUser, @PathVariable Long companyId) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(companyService.getCompany(companyId, companyId));
        }
        return ResponseEntity.ok(companyService.getCompany(currentUser.getCompanyId(), companyId));
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@CurrentUser AppUser currentUser, @RequestParam String companyName) {
        if (!currentUser.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("Endpoint for Admins only");
        }
        return ResponseEntity.ok(companyService.createCompany(companyName));
    }

    @PutMapping("/companies/{companyId}")
    public ResponseEntity<Company> setCompanyName(@CurrentUser AppUser currentUser, @PathVariable Long companyId, @RequestParam String companyName) {
        return ResponseEntity.ok(companyService.setCompanyName(currentUser.getCompanyId(), companyId, companyName));
    }

    @PutMapping("/companies/{companyId}/menu_link")
    public ResponseEntity<Company> setCompanyMenuLink(@CurrentUser AppUser currentUser,
                                                      @PathVariable Long companyId,
                                                      @RequestBody SingleStringDto menuLink) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(companyService.setCompanyMenuLink(companyId, companyId, menuLink.getContent()));
        }
        return ResponseEntity.ok(
                companyService.setCompanyMenuLink(
                        currentUser.getCompanyId(),
                        companyId,
                        menuLink.getContent()
                )
        );
    }

    @PutMapping("/companies/{companyId}/background_image")
    public ResponseEntity<Company> setCompanyBackgroundImage(@CurrentUser AppUser currentUser,
                                                      @PathVariable Long companyId,
                                                      @RequestBody SingleStringDto backgroundImage) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(companyService.setCompanyBackgroundImage(companyId, companyId, backgroundImage.getContent()));
        }
        return ResponseEntity.ok(
                companyService.setCompanyBackgroundImage(
                        currentUser.getCompanyId(),
                        companyId,
                        backgroundImage.getContent()
                )
        );
    }

    @PutMapping("/companies/{companyId}/facebook_link")
    public ResponseEntity<Company> setCompanyFacebookLink(@CurrentUser AppUser currentUser,
                                                             @PathVariable Long companyId,
                                                             @RequestBody SingleStringDto facebookLink) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(companyService.setCompanyFacebookLink(companyId, companyId, facebookLink.getContent()));
        }
        return ResponseEntity.ok(
                companyService.setCompanyFacebookLink(
                        currentUser.getCompanyId(),
                        companyId,
                        facebookLink.getContent()
                )
        );
    }

    @PutMapping("/companies/{companyId}/instagram_link")
    public ResponseEntity<Company> setCompanyInstagramLink(@CurrentUser AppUser currentUser,
                                                          @PathVariable Long companyId,
                                                          @RequestBody SingleStringDto instagramLink) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(companyService.setCompanyInstagramLink(companyId, companyId, instagramLink.getContent()));
        }
        return ResponseEntity.ok(
                companyService.setCompanyInstagramLink(
                        currentUser.getCompanyId(),
                        companyId,
                        instagramLink.getContent()
                )
        );
    }
}
