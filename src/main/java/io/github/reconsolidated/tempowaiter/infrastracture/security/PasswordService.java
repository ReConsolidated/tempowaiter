package io.github.reconsolidated.tempowaiter.infrastracture.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordService() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean checkPassword(String password, String hashedPassword) {
        return bCryptPasswordEncoder.matches(password, hashedPassword);
    }
}
