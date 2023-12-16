package io.github.reconsolidated.tempowaiter.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Card {
    @Id
    @GeneratedValue(generator = "card_id_generator")
    private Long id;
    private String cardUid;
    private Long tableId;
    private Long companyId;
    private String displayName;

    public Card(String cardUid) {
        this.cardUid = cardUid;
    }
}
