package io.github.reconsolidated.tempowaiter.infrastracture;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RefreshTokenExpiredException extends RuntimeException{
}
