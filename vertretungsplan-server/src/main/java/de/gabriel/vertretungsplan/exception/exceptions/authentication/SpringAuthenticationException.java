package de.gabriel.vertretungsplan.exception.exceptions.authentication;

import org.springframework.http.HttpStatus;

public class SpringAuthenticationException extends AuthenticationException {

    public SpringAuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
