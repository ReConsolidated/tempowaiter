package io.github.reconsolidated.tempowaiter.infrastracture.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    Optional<JwtRefreshToken> findByToken(String token);
}
