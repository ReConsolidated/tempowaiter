package io.github.reconsolidated.tempowaiter.infrastracture.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.waiter.RequestState;
import io.github.reconsolidated.tempowaiter.waiter.WaiterRequest;
import io.github.reconsolidated.tempowaiter.waiter.WaiterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class WaiterController {
    private final WaiterService waiterService;

    @GetMapping("/requests")
    public ResponseEntity<List<WaiterRequest>> getRequests(@CurrentUser AppUser currentUser) {
        List<WaiterRequest> requests = waiterService.getRequests(currentUser.getId(), currentUser.getCompanyId());
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/requests/{requestId}/set_state")
    public ResponseEntity<WaiterRequest> setRequestState(@CurrentUser AppUser currentUser, @PathVariable Long requestId, @RequestParam RequestState state) {
        WaiterRequest request = waiterService.setRequestState(currentUser.getId(), currentUser.getCompanyId(), requestId, state);
        return ResponseEntity.ok(request);
    }

}