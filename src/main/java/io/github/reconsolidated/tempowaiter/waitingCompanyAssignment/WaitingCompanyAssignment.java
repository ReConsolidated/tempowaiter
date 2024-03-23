package io.github.reconsolidated.tempowaiter.waitingCompanyAssignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WaitingCompanyAssignment {
    @Id
    @GeneratedValue
    private Long id;
    private Long companyId;
    @Column(unique = true)
    private String email;
}
