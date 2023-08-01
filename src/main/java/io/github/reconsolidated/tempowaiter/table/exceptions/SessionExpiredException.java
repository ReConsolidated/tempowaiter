package io.github.reconsolidated.tempowaiter.table.exceptions;

public class SessionExpiredException extends RuntimeException{
    public SessionExpiredException() {
        super("Session expired. Try scanning again.");
    }
}
