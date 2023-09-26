package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class WaiterController {
    private final WaiterService waiterService;
    private final CompanyService companyService;

    @GetMapping("/requests")
    public ResponseEntity<List<WaiterRequest>> getRequests(@CurrentUser AppUser currentUser) {
        List<WaiterRequest> requests = waiterService.getRequests(currentUser.getId(), currentUser.getCompanyId());
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/requests/{requestId}/set_state")
    public ResponseEntity<WaiterRequest> setRequestState(@CurrentUser AppUser currentUser, Long requestId, RequestState state) {
        WaiterRequest request = waiterService.setRequestState(currentUser.getId(), currentUser.getCompanyId(), requestId, state);
        return ResponseEntity.ok(request);
    }

}