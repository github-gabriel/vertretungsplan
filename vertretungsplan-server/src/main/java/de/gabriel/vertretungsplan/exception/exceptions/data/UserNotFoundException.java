package de.gabriel.vertretungsplan.exception.exceptions.data;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DataNotFoundException {

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
