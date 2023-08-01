package io.github.reconsolidated.tempowaiter.table.exceptions;

public class TableNotFoundException extends RuntimeException{
    public TableNotFoundException(long tableId) {
        super("Table with id %d not found.".formatted(tableId));
    }
}
