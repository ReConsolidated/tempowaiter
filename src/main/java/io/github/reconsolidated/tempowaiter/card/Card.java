package io.github.reconsolidated.tempowaiter.card;

import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Card {
    @Id
    @GeneratedValue(generator = "card_id_generator")
    @SequenceGenerator(name = "card_id_generator", allocationSize = 1)
    private Long id;
    private String cardUid;
    private Long tableId;
    private Long companyId;
    private String displayName;

    public Card(String cardUid) {
        this.cardUid = cardUid;
    }
}
