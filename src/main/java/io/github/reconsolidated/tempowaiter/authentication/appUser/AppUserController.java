package io.github.reconsolidated.tempowaiter.authentication.appUser;

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

    @PostMapping("/first_name")
    public ResponseEntity<?> setFirstName(@CurrentUser AppUser user,
                                          @RequestParam String name) {
        appUserService.setFirstName(user, name);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/last_name")
    public ResponseEntity<AppUser> setLastName(@CurrentUser AppUser user, @RequestParam String name) {
        appUserService.setLastName(user, name);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/company_id")
    public ResponseEntity<AppUser> setCompanyId(@CurrentUser AppUser user, String email, @RequestParam Long companyId) {
        if (!user.getRole().equals(AppUserRole.ADMIN)) {
            throw new IllegalArgumentException("This endpoint is for Admins only");
        }
        appUserService.setCompanyId(email, companyId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/become_admin")
    public ResponseEntity<AppUser> becomeAdmin(@CurrentUser AppUser user, @RequestParam String password) {
        if (password.equals("test_password")) {
            return ResponseEntity.ok(appUserService.makeAdmin(user));
        }
        return ResponseEntity.badRequest().build();
    }
}
