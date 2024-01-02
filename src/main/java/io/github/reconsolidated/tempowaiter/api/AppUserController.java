package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserRole;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/company_id")
    public ResponseEntity<AppUser> setCompanyId(@CurrentUser AppUser user, String email, @RequestParam Long companyId) {
        if (!user.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("This endpoint is for Admins only");
        }
        appUserService.setCompanyId(email, companyId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/role/{role}")
    public ResponseEntity<AppUser> becomeAdmin(@CurrentUser AppUser user, @RequestParam String email, @PathVariable String role) {
        if (!user.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("This endpoint is for Admins only");
        }
        AppUserRole appUserRole = AppUserRole.valueOf(role.toUpperCase());
        return ResponseEntity.ok(appUserService.setUserRole(email, appUserRole));
    }
}
