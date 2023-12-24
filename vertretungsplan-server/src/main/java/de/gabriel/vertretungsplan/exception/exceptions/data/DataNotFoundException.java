package de.gabriel.vertretungsplan.exception.exceptions.data;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class DataNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public DataNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
