package io.github.reconsolidated.tempowaiter.table.exceptions;

public class OutdatedTableRequestException extends RuntimeException {
    public OutdatedTableRequestException() {
        super("Outdated table request. Try scanning again.");
    }
}
