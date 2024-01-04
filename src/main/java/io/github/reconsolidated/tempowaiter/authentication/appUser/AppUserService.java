package io.github.reconsolidated.tempowaiter.authentication.appUser;

import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.infrastracture.security.PasswordService;
import lombok.AllArgsConstructor;
import io.github.reconsolidated.tempowaiter.waitingCompanyAssignment.WaitingCompanyAssignment;
import io.github.reconsolidated.tempowaiter.waitingCompanyAssignment.WaitingCompanyAssignmentRepository;
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
    private final WaitingCompanyAssignmentRepository waitingCompanyAssignmentRepository;
    private final Logger logger = Logger.getLogger(AppUserService.class.getName());
    private final PasswordService passwordService;
    private final CompanyService companyService;

    public AppUserDto register(String email, String password) {
        AppUser appUser = AppUser
                .builder()
                .email(email)
                .password(passwordService.hashPassword(password))
                .build();
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email %s already exists".formatted(email));
        }

        var waitingAssignment = waitingCompanyAssignmentRepository.findByEmail(email);
        if (waitingAssignment.isPresent()) {
            WaitingCompanyAssignment waitingCompanyAssignment = waitingAssignment.get();
            waitingCompanyAssignmentRepository.deleteByEmail(email);
            appUser.setCompanyId(waitingCompanyAssignment.getCompanyId());
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
