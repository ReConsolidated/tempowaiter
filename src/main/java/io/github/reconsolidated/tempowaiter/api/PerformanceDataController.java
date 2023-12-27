package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.performanceData.PerformanceDataService;
import io.github.reconsolidated.tempowaiter.performanceData.TablePerformanceData;
import io.github.reconsolidated.tempowaiter.performanceData.TableSessionsData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@AllArgsConstructor
public class PerformanceDataController {
    private final PerformanceDataService performanceDataService;

    @GetMapping("/performance-data/table-average-time")
    public ResponseEntity<Collection<TablePerformanceData>> tableAverageTime(@CurrentUser AppUser currentUser) {
        return ResponseEntity.ok(performanceDataService.getTablePerformanceData(currentUser.getCompanyId()));
    }

    @GetMapping("/performance-data/table-sessions")
    public ResponseEntity<Collection<TableSessionsData>> sessionsPerTable(@CurrentUser AppUser currentUser) {
        return ResponseEntity.ok(performanceDataService.getTableSessionData(currentUser.getCompanyId()));
    }
}
