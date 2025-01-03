package io.github.reconsolidated.tempowaiter.infrastracture.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserDto;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.domain.UserCredentials;
import io.github.reconsolidated.tempowaiter.infrastracture.security.JwtService;
import io.github.reconsolidated.tempowaiter.utils.DummyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AppUserService appUserService;

    @GetMapping("/user-info")
    public ResponseEntity<?> userInfo(@CurrentUser AppUser currentUser) {
        return ResponseEntity.ok(AppUserDto.builder()
                        .email(currentUser.getEmail())
                        .isVerified(currentUser.isEnabled())
                        .build());
    }

    @PostMapping("/refresh-token/{refreshToken}/access-token")
    public ResponseEntity<?> refreshToken(@PathVariable String refreshToken) {
        return ResponseEntity.ok(Map.of("accessToken", jwtService.getFromRefreshToken(refreshToken)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials credentials) {
        if (appUserService.validate(credentials.getEmail(), credentials.getPassword())) {
            return ResponseEntity.ok(
                    new DummyDto(
                            Map.of(
                                    "token", jwtService.generateToken(credentials.getEmail()),
                                    "refreshToken", jwtService.generateRefreshToken(credentials.getEmail())
                            )
                    )
            );
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Credentials don't match user");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody UserCredentials credentials) {
        return ResponseEntity.ok(
                RegisterResponse.builder()
                        .email(appUserService.register(credentials.getEmail(), credentials.getPassword()).getEmail())
                        .token(jwtService.generateToken(credentials.getEmail()))
                        .refreshToken(jwtService.generateRefreshToken(credentials.getEmail()))
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CurrentUser AppUser currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("User not logged in");
        }
        jwtService.logout(currentUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<AppUserDto> verify(@RequestParam String token) {
        AppUserDto appUserDto = appUserService.verify(token);
        return ResponseEntity.ok(appUserDto);
    }

    @PostMapping("/set-password")
    public ResponseEntity<AppUserDto> setPassword(@RequestParam String token, @RequestParam String newPassword) {
        AppUserDto appUserDto = appUserService.setPassword(token, newPassword);
        return ResponseEntity.ok(appUserDto);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AppUserDto> sendResetPasswordLink(@RequestParam String email) {
        AppUserDto appUserDto = appUserService.sendResetPasswordLink(email);
        return ResponseEntity.ok(appUserDto);
    }

    @PostMapping("/resend-verification-token")
    public ResponseEntity<AppUserDto> resendVerificationToken(@RequestParam String email) {
        AppUserDto appUserDto = appUserService.sendVerificationToken(email);
        return ResponseEntity.ok(appUserDto);
    }

    @Builder
    @Getter
    public static class RegisterResponse {
        private final String email;
        private final String token;
        private final String refreshToken;
    }
}
