package de.gabriel.vertretungsplan.exception.exceptions.data;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends DataNotFoundException {

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
