package io.github.reconsolidated.tempowaiter.domain.table.exceptions;

import lombok.Getter;

@Getter
public class OutdatedTableRequestException extends RuntimeException {
    private final long cardId;
    public OutdatedTableRequestException(long cardId) {
        super("Outdated table request. Try scanning again.");
        this.cardId = cardId;
    }
}
