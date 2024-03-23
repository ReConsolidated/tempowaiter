package io.github.reconsolidated.tempowaiter.infrastracture.security;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRefreshToken {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private String email;
    private LocalDateTime expiresAt;
}
