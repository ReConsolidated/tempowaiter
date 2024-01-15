package io.github.reconsolidated.tempowaiter.authentication.currentUser;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Authentication required", code = HttpStatus.UNAUTHORIZED)
public class UnauthenticatedException extends RuntimeException{
}
