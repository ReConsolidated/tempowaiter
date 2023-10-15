package io.github.reconsolidated.tempowaiter.authentication.appUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.logging.Logger;


@Service
@Validated
public class AppUserService {

    private final static String USER_NOT_FOUND_MESSAGE =
            "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final Logger logger = Logger.getLogger(AppUserService.class.getName());

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public Optional<AppUser> findUserById(Long appUserId) {
        return appUserRepository.findById(appUserId);
    }

    public Optional<AppUser> findUserByEmail(String inviteeEmail) {
        return appUserRepository.findByEmail(inviteeEmail);
    }

    public AppUser getOrCreateUser(String keycloakId, String email, String firstName, String lastName) {
        logger.info("getOrCreateUser: " + keycloakId + " " + email + " " + firstName + " " + lastName);
        return appUserRepository.findByKeycloakId(keycloakId).orElseGet(() -> {
            AppUser appUser = new AppUser();
            appUser.setKeycloakId(keycloakId);
            appUser.setEmail(email);
            appUser.setFirstName(firstName);
            appUser.setLastName(lastName);

            return appUserRepository.save(appUser);
        });
    }

    public void setFirstName(AppUser user, String name) {
        user.setFirstName(name);
        appUserRepository.save(user);
    }

    public void setLastName(AppUser user, String name) {
        user.setLastName(name);
        appUserRepository.save(user);
    }

    public AppUser getUser(Long appUserId) {
        return appUserRepository.findById(appUserId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void deleteUser(AppUser user) {
        appUserRepository.delete(user);
    }

    public void setCompanyId(String userEmail, Long companyId) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(String.format(USER_NOT_FOUND_MESSAGE, userEmail)));
        user.setCompanyId(companyId);
        appUserRepository.save(user);
    }

    public AppUser makeAdmin(AppUser user) {
        user.setRole(AppUserRole.ADMIN);
        return appUserRepository.save(user);
    }
}
