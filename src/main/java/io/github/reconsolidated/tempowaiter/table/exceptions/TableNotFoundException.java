package io.github.reconsolidated.tempowaiter.table.exceptions;

public class TableNotFoundException extends RuntimeException{
    public TableNotFoundException(long tableId) {
        super("Table for card id %d not found.".formatted(tableId));
    }
}
