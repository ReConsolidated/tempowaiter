package io.github.reconsolidated.tempowaiter.authentication.appUser;

import io.github.reconsolidated.tempowaiter.waitingCompanyAssignment.WaitingCompanyAssignment;
import io.github.reconsolidated.tempowaiter.waitingCompanyAssignment.WaitingCompanyAssignmentRepository;
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
    private final WaitingCompanyAssignmentRepository waitingCompanyAssignmentRepository;
    private final Logger logger = Logger.getLogger(AppUserService.class.getName());

    public AppUserService(AppUserRepository appUserRepository, WaitingCompanyAssignmentRepository waitingCompanyAssignmentRepository) {
        this.appUserRepository = appUserRepository;
        this.waitingCompanyAssignmentRepository = waitingCompanyAssignmentRepository;
    }

    public Optional<AppUser> findUserById(Long appUserId) {
        return appUserRepository.findById(appUserId);
    }

    public Optional<AppUser> findUserByEmail(String inviteeEmail) {
        return appUserRepository.findByEmail(inviteeEmail);
    }

    public AppUser getOrCreateUser(String keycloakId, String email, String firstName, String lastName) {
        return appUserRepository.findByKeycloakId(keycloakId).orElseGet(() -> {
            var opt = appUserRepository.findByEmail(email);
            if (opt.isPresent()) throw new UserAlreadyExistsException();
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
        Optional<AppUser> user = appUserRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            user.get().setCompanyId(companyId);
            appUserRepository.save(user.get());
        } else {
            waitingCompanyAssignmentRepository.deleteByEmail(userEmail);
            waitingCompanyAssignmentRepository.save(new WaitingCompanyAssignment(null, companyId, userEmail));
        }
    }

    public AppUser setUserRole(String email, AppUserRole appUserRole) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(appUserRole);
        return appUserRepository.save(user);
    }
}
