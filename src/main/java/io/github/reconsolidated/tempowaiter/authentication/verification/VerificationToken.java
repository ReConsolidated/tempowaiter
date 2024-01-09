package io.github.reconsolidated.tempowaiter.authentication.verification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VerificationToken {
    @Id
    @GeneratedValue
    private Long id;
    private String type;
    private String token;
    @Column(unique = true)
    private String email;
    private LocalDateTime lastResent;
}
