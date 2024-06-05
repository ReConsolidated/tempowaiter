package io.github.reconsolidated.tempowaiter.infrastracture.api;

import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUserRole;
import io.github.reconsolidated.tempowaiter.domain.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.domain.performanceData.dto.CompaniesPerformanceDataDto;
import io.github.reconsolidated.tempowaiter.domain.performanceData.dto.CompaniesSessionsDataDto;
import io.github.reconsolidated.tempowaiter.application.performanceData.PerformanceDataService;
import io.github.reconsolidated.tempowaiter.domain.performanceData.TimeRange;
import io.github.reconsolidated.tempowaiter.domain.performanceData.events.TempoEvent;
import io.github.reconsolidated.tempowaiter.domain.performanceData.events.TempoEventDto;
import io.github.reconsolidated.tempowaiter.application.performanceData.events.TempoEventService;
import io.github.reconsolidated.tempowaiter.application.table.TableService;
import io.github.reconsolidated.tempowaiter.domain.table.exceptions.SessionExpiredException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
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
        TimeRange timeRange = getTimeRange(unixMillisFrom, unixMillisTo);
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
        TimeRange timeRange = getTimeRange(unixMillisFrom, unixMillisTo);
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(performanceDataService.getTableSessionData(null, timeRange));
        } else {
            return ResponseEntity.ok(performanceDataService.getTableSessionData(currentUser.getCompanyId(), timeRange));
        }
    }

    @GetMapping("/performance-data/events")
    public ResponseEntity<?> getEvents(@CurrentUser AppUser currentUser,
                                       @RequestParam Long unixMillisFrom,
                                       @RequestParam Long unixMillisTo) {
        TimeRange timeRange = getTimeRange(unixMillisFrom, unixMillisTo);
        if (currentUser.getRole().equals(AppUserRole.ADMIN)) {
            return ResponseEntity.ok(tempoEventService.getEventsData(null, timeRange));
        } else {
            return ResponseEntity.ok(tempoEventService.getEventsData(currentUser.getCompanyId(), timeRange));
        }
    }

    @PostMapping("/performance-data/events")
    public ResponseEntity<?> reportEvent(@RequestParam @NotNull String sessionId,
                                         @RequestBody TempoEventDto event) {
        tableService.getSession(sessionId).orElseThrow(SessionExpiredException::new);
        tempoEventService.reportEvent(new TempoEvent(event));
        return ResponseEntity.ok().build();
    }

    private static TimeRange getTimeRange(Long unixMillisFrom, Long unixMillisTo) {
        return new TimeRange(
                LocalDateTime.ofEpochSecond(unixMillisFrom/1000, 0, ZoneOffset.UTC),
                LocalDateTime.ofEpochSecond(unixMillisTo/1000, 0, ZoneOffset.UTC));
    }
}
