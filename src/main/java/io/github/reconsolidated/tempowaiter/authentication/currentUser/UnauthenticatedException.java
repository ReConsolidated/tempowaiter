package io.github.reconsolidated.tempowaiter.authentication.currentUser;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(reason = "Authentication required", code = HttpStatus.UNAUTHORIZED)
public class UnauthenticatedException extends RuntimeException{
    public UnauthenticatedException(String message) {
        super(message);
    }
}
