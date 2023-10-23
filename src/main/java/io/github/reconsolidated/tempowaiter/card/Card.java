package io.github.reconsolidated.tempowaiter.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Card {
    @Id
    private Long id;
    private Long tableId;
    private Long companyId;

    public Card(Long id, Long companyId) {
        this.id = id;
        this.companyId = companyId;
    }
}
