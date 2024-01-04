package io.github.reconsolidated.tempowaiter.waitingCompanyAssignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WaitingCompanyAssignment {
    private Long id;
    private Long companyId;
    @Column(unique = true)
    private String email;
}
