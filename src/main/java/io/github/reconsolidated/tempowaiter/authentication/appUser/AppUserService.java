package io.github.reconsolidated.tempowaiter.authentication.appUser;

import io.github.reconsolidated.tempowaiter.authentication.verification.VerificationService;
import io.github.reconsolidated.tempowaiter.authentication.verification.VerificationToken;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.infrastracture.email.EmailService;
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
    private final AppUserRepository appUserRepository;
    private final WaitingCompanyAssignmentRepository waitingCompanyAssignmentRepository;
    private final PasswordService passwordService;
    private final VerificationService verificationService;

    public AppUserDto register(String email, String password) {
        AppUser appUser = AppUser
                .builder()
                .email(email)
                .password(passwordService.hashPassword(password))
                .enabled(false)
                .build();
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        var waitingAssignment = waitingCompanyAssignmentRepository.findByEmail(email);
        if (waitingAssignment.isPresent()) {
            WaitingCompanyAssignment waitingCompanyAssignment = waitingAssignment.get();
            waitingCompanyAssignmentRepository.deleteByEmail(email);
            appUser.setCompanyId(waitingCompanyAssignment.getCompanyId());
        }
        appUser = appUserRepository.save(appUser);

        verificationService.sendVerificationToken(email);

        return AppUserDto
                .builder()
                .email(appUser.getEmail())
                .companyId(appUser.getCompanyId())
                .build();
    }

    public AppUserDto verify(String token) {
        VerificationToken verificationToken = verificationService.verify(token);
        if (verificationToken.getToken().equals("verification")) {
            AppUser appUser = appUserRepository.findByEmail(verificationToken.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            appUser.setEnabled(true);
            appUserRepository.save(appUser);
            return AppUserDto
                    .builder()
                    .email(appUser.getEmail())
                    .companyId(appUser.getCompanyId())
                    .build();
        } else if (verificationToken.getType().equals("reset-password")) {
            AppUser appUser = appUserRepository.findByEmail(verificationToken.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            appUser.setPassword(null);
            appUserRepository.save(appUser);
            return AppUserDto
                    .builder()
                    .email(appUser.getEmail())
                    .companyId(appUser.getCompanyId())
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public AppUserDto setPassword(String token, String newPassword) {
        AppUser appUser = appUserRepository.findByEmail(verify(token).getEmail()).orElseThrow();
        appUser.setPassword(passwordService.hashPassword(newPassword));
        appUserRepository.save(appUser);
        return AppUserDto
                .builder()
                .email(appUser.getEmail())
                .companyId(appUser.getCompanyId())
                .build();
    }

    public boolean validate(String email, String password) {
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        if (appUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!passwordService.checkPassword(password, appUser.get().getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        return true;
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

    public AppUserDto sendVerificationToken(String email) {
        verificationService.sendVerificationToken(email);
        return AppUserDto
                .builder()
                .email(email)
                .build();
    }

    public AppUserDto sendResetPasswordLink(String email) {
        verificationService.sendResetPasswordLink(email);
        return AppUserDto
                .builder()
                .email(email)
                .build();
    }
}
