package io.github.reconsolidated.tempowaiter.table.exceptions;

import lombok.Getter;

public class TableNotFoundException extends RuntimeException{
    @Getter
    private final long cardId;
    public TableNotFoundException(long cardId) {
        super("Table for card id %d not found.".formatted(cardId));
        this.cardId = cardId;
    }
}
