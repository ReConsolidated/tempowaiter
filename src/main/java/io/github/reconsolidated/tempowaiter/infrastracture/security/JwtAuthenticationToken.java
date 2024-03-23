package io.github.reconsolidated.tempowaiter.infrastracture.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken implements Authentication {

    private String token;
    private String email;
    private boolean isAuthenticated;

    public JwtAuthenticationToken(String token) {
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Możesz dostosować do swoich potrzeb
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return email;
    }

    // Metody do ustawiania nazwy użytkownika
    public void setEmail(String email) {
        this.email = email;
    }
}
