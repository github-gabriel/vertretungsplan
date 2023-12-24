package de.gabriel.vertretungsplan.exception.exceptions.authentication;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AuthenticationException extends RuntimeException {

    private final HttpStatus status;

    public AuthenticationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
