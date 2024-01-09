package io.github.reconsolidated.tempowaiter.authentication.verification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"type", "email"}))
public class VerificationToken {
    @Id
    @GeneratedValue
    private Long id;
    private String type;
    private String token;
    private String email;
    private LocalDateTime lastResent;
}
