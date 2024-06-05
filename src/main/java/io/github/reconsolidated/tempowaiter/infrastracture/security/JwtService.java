package io.github.reconsolidated.tempowaiter.infrastracture.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.domain.RefreshTokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class JwtService {
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final String secretKey;
    private final Logger logger = Logger.getLogger(JwtService.class.getName());

    public JwtService(JwtRefreshTokenRepository jwtRefreshTokenRepository,
                      @Value("${JWT_SECRET_KEY}") String secretKey) {
        this.jwtRefreshTokenRepository = jwtRefreshTokenRepository;
        this.secretKey = secretKey;
        if (secretKey.equals("secret")) {
            logger.warning("Secret key is 'secret', please set the JWT_SECRET_KEY " +
                    "environment variable to something more secure!");
        }
    }

    public String getFromRefreshToken(String refreshToken) {
        JwtRefreshToken token = jwtRefreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenExpiredException();
        } else {
            return generateToken(token.getEmail());
        }
    }

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            // nieprawidłowy token
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    public String generateRefreshToken(String email) {
        JwtRefreshToken token = JwtRefreshToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusYears(3))
                .build();
        jwtRefreshTokenRepository.save(token);
        return token.getToken();
    }

    @Transactional
    public void logout(AppUser currentUser) {
        jwtRefreshTokenRepository.deleteByEmail(currentUser.getEmail());
    }
}
