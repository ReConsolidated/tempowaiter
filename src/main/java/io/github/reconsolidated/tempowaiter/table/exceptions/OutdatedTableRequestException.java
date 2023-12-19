package io.github.reconsolidated.tempowaiter.table.exceptions;

import io.github.reconsolidated.tempowaiter.card.Card;
import lombok.Getter;

@Getter
public class OutdatedTableRequestException extends RuntimeException {
    private final Card card;
    public OutdatedTableRequestException(Card card) {
        super("Outdated table request. Try scanning again.");
        this.card = card;
    }
}
