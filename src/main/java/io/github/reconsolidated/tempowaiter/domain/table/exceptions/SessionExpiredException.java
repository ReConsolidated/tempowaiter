package io.github.reconsolidated.tempowaiter.domain.table.exceptions;

public class SessionExpiredException extends RuntimeException{
    public SessionExpiredException() {
        super("Session expired. Try scanning again.");
    }
}
