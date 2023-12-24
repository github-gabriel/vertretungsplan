package de.gabriel.vertretungsplan.exception.exceptions.authentication;

import org.springframework.http.HttpStatus;

public class InvalidJwtException extends AuthenticationException {

    public InvalidJwtException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
