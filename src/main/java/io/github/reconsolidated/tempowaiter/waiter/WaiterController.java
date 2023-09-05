package io.github.reconsolidated.tempowaiter.waiter;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/public")
@AllArgsConstructor
public class WaiterController {
    private final WaiterService waiterService;
    private final CompanyService companyService;

    @GetMapping("/requests")
    public ResponseEntity<List<WaiterRequest>> getRequests(@CurrentUser AppUser currentUser) {
        List<WaiterRequest> requests = waiterService.getRequests(currentUser.getId(), currentUser.getCompanyId());
        return ResponseEntity.ok(requests);
    }

}