package io.github.reconsolidated.tempowaiter.authentication.appUser;

import io.github.reconsolidated.tempowaiter.infrastracture.security.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.logging.Logger;


@Service
@Validated
@AllArgsConstructor
public class AppUserService {

    private final static String USER_NOT_FOUND_MESSAGE =
            "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final PasswordService passwordService;

    public AppUserDto register(String email, String password) {
        AppUser appUser = AppUser
                .builder()
                .email(email)
                .password(passwordService.hashPassword(password))
                .build();
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email %s already exists".formatted(email));
        }
        appUser = appUserRepository.save(appUser);
        return AppUserDto
                .builder()
                .email(appUser.getEmail())
                .build();
    }

    public boolean validate(String email, String password) {
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        if (appUser.isEmpty()) {
            return false;
        }
        return passwordService.checkPassword(password, appUser.get().getPassword());
    }

    public Optional<AppUser> getUser(String email) {
        return appUserRepository.findByEmail(email);
    }

    public void setCompanyId(String userEmail, Long companyId) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND_MESSAGE, userEmail)));
        user.setCompanyId(companyId);
        appUserRepository.save(user);
    }

    public AppUser setUserRole(String email, AppUserRole appUserRole) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(appUserRole);
        return appUserRepository.save(user);
    }
}
