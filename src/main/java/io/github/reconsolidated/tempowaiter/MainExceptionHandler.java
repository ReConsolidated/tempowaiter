package io.github.reconsolidated.tempowaiter;

import io.github.reconsolidated.tempowaiter.table.exceptions.OutdatedTableRequestException;
import io.github.reconsolidated.tempowaiter.table.exceptions.SessionExpiredException;
import io.github.reconsolidated.tempowaiter.table.exceptions.TableNotFoundException;
import io.github.reconsolidated.tempowaiter.utils.DummyDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {
    private boolean debug = true;

    @ExceptionHandler(value = { RuntimeException.class})
    protected ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        StringBuilder bodyOfResponse = new StringBuilder(ex.getMessage());
        if (debug) {
            for (StackTraceElement line : ex.getStackTrace()) {
                bodyOfResponse.append("\n");
                bodyOfResponse.append(line.toString());
            }
        }
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value
            = { TableNotFoundException.class })
    protected ResponseEntity<Object> handleTableNotFound(
            TableNotFoundException ex, WebRequest request) {
        StringBuilder bodyOfResponse = new StringBuilder(ex.getMessage());
        if (debug) {
            for (StackTraceElement line : ex.getStackTrace()) {
                bodyOfResponse.append("\n");
                bodyOfResponse.append(line.toString());
            }
        }
        return handleExceptionInternal(ex, new DummyDto(Map.of("error", bodyOfResponse.toString(),
                "cardId", ex.getCardId())),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = { NoSuchElementException.class })
    protected ResponseEntity<Object> handleNoElement(
            NoSuchElementException ex, WebRequest request) {
        StringBuilder bodyOfResponse = new StringBuilder(ex.getMessage());
        if (debug) {
            for (StackTraceElement line : ex.getStackTrace()) {
                bodyOfResponse.append("\n");
                bodyOfResponse.append(line.toString());
            }
        }
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = { SessionExpiredException.class })
    protected ResponseEntity<Object> handleSessionExpired(
            SessionExpiredException ex, WebRequest request) {
        StringBuilder bodyOfResponse = new StringBuilder(ex.getMessage());
        if (debug) {
            for (StackTraceElement line : ex.getStackTrace()) {
                bodyOfResponse.append("\n");
                bodyOfResponse.append(line.toString());
            }
        }
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value
            = { OutdatedTableRequestException.class })
    protected ResponseEntity<Object> handleOutdatedTable(
            OutdatedTableRequestException ex, WebRequest request) {
        StringBuilder bodyOfResponse = new StringBuilder(ex.getMessage());
        if (debug) {
            for (StackTraceElement line : ex.getStackTrace()) {
                bodyOfResponse.append("\n");
                bodyOfResponse.append(line.toString());
            }
        }
        return handleExceptionInternal(ex, new DummyDto(Map.of("error", bodyOfResponse.toString(),
                "cardId", ex.getCardId())),
                new HttpHeaders(), HttpStatus.GONE, request);
    }

    @ExceptionHandler(value
            = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        StringBuilder bodyOfResponse = new StringBuilder(ex.getMessage());
        if (debug) {
            for (StackTraceElement line : ex.getStackTrace()) {
                bodyOfResponse.append("\n");
                bodyOfResponse.append(line.toString());
            }
        }
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
