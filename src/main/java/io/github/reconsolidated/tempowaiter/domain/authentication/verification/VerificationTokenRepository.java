package io.github.reconsolidated.tempowaiter.domain.authentication.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByEmailAndType(String email, String type);

    Optional<VerificationToken> findByToken(String token);
}
