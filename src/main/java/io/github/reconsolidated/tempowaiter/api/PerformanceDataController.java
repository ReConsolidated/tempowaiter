package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserRole;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.performanceData.CompaniesPerformanceDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.CompaniesSessionsDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.PerformanceDataService;
import io.github.reconsolidated.tempowaiter.performanceData.TimeRange;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PerformanceDataController {
    private final PerformanceDataService performanceDataService;

    @GetMapping("/performance-data/table-average-time")
    public ResponseEntity<CompaniesPerformanceDataDto> tableAverageTime(@CurrentUser AppUser currentUser,
                                                                        @RequestBody TimeRange timeRange) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(performanceDataService.getTablePerformanceData(null, timeRange));
        } else {
            return ResponseEntity.ok(performanceDataService.getTablePerformanceData(currentUser.getCompanyId(), timeRange));
        }
    }

    @GetMapping("/performance-data/table-sessions")
    public ResponseEntity<CompaniesSessionsDataDto> sessionsPerTable(@CurrentUser AppUser currentUser,
                                                                     @RequestBody TimeRange timeRange) {
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(performanceDataService.getTableSessionData(null, timeRange));
        } else {
            return ResponseEntity.ok(performanceDataService.getTableSessionData(currentUser.getCompanyId(), timeRange));
        }
    }
}
