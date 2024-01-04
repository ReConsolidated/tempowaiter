package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserRole;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.performanceData.dto.CompaniesPerformanceDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.dto.CompaniesSessionsDataDto;
import io.github.reconsolidated.tempowaiter.performanceData.PerformanceDataService;
import io.github.reconsolidated.tempowaiter.performanceData.TimeRange;
import io.github.reconsolidated.tempowaiter.performanceData.events.TempoEvent;
import io.github.reconsolidated.tempowaiter.performanceData.events.TempoEventDto;
import io.github.reconsolidated.tempowaiter.performanceData.events.TempoEventService;
import io.github.reconsolidated.tempowaiter.table.TableService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@AllArgsConstructor
public class PerformanceDataController {
    private final PerformanceDataService performanceDataService;
    private final TempoEventService tempoEventService;
    private final TableService tableService;

    @GetMapping("/performance-data/table-average-time")
    public ResponseEntity<CompaniesPerformanceDataDto> tableAverageTime(@CurrentUser AppUser currentUser,
                                                                        @RequestParam Long unixMillisFrom,
                                                                        @RequestParam Long unixMillisTo) {
        TimeRange timeRange = new TimeRange(
                LocalDateTime.ofEpochSecond(unixMillisFrom/1000, 0, ZoneOffset.UTC),
                LocalDateTime.ofEpochSecond(unixMillisTo/1000, 0, ZoneOffset.UTC));
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(performanceDataService.getTablePerformanceData(null, timeRange));
        } else {
            return ResponseEntity.ok(performanceDataService.getTablePerformanceData(currentUser.getCompanyId(), timeRange));
        }
    }

    @GetMapping("/performance-data/table-sessions")
    public ResponseEntity<CompaniesSessionsDataDto> sessionsPerTable(@CurrentUser AppUser currentUser,
                                                                     @RequestParam Long unixMillisFrom,
                                                                     @RequestParam Long unixMillisTo) {
        TimeRange timeRange = new TimeRange(
                LocalDateTime.ofEpochSecond(unixMillisFrom/1000, 0, ZoneOffset.UTC),
                LocalDateTime.ofEpochSecond(unixMillisTo/1000, 0, ZoneOffset.UTC));

        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(performanceDataService.getTableSessionData(null, timeRange));
        } else {
            return ResponseEntity.ok(performanceDataService.getTableSessionData(currentUser.getCompanyId(), timeRange));
        }
    }

    @PostMapping("/performance-data/events")
    public ResponseEntity<?> reportEvent(@RequestParam @NotNull String sessionId,
                                         @RequestBody TempoEventDto event) {
        tableService.getSession(sessionId).orElseThrow(() -> new IllegalArgumentException("No such session"));
        tempoEventService.reportEvent(new TempoEvent(event));
        return ResponseEntity.ok().build();
    }
}
