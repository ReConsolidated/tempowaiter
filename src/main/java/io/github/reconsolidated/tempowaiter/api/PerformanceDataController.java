package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserRole;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.performanceData.CompaniesPerformanceDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.CompaniesSessionsDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.PerformanceDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PerformanceDataController {
    private final PerformanceDataService performanceDataService;

    @GetMapping("/performance-data/table-average-time")
    public ResponseEntity<CompaniesPerformanceDataDto> tableAverageTime(@CurrentUser AppUser currentUser) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(performanceDataService.getTablePerformanceData(null));
        } else {
            return ResponseEntity.ok(performanceDataService.getTablePerformanceData(currentUser.getCompanyId()));
        }
    }

    @GetMapping("/performance-data/table-sessions")
    public ResponseEntity<CompaniesSessionsDataDto> sessionsPerTable(@CurrentUser AppUser currentUser) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(performanceDataService.getTableSessionData(null));
        } else {
            return ResponseEntity.ok(performanceDataService.getTableSessionData(currentUser.getCompanyId()));
        }
    }

    @GetMapping("/public/sessions")
    public ResponseEntity<CompaniesSessionsDataDto> sessionsPerTable2() {
        return ResponseEntity.ok(performanceDataService.getTableSessionData(null));
    }
}
