package de.gabriel.vertretungsplan.exception;


import de.gabriel.vertretungsplan.exception.exceptions.authentication.AuthenticationException;
import de.gabriel.vertretungsplan.exception.exceptions.data.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException dataNotFoundException) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        log.warn("The requested resource couldn't be found", dataNotFoundException);

        return new ResponseEntity<>(new ErrorResponse(status), status);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException authenticationException) {
        HttpStatus status = HttpStatus.UNAUTHORIZED; // 401

        log.warn("An error occurred while authenticating", authenticationException);

        return new ResponseEntity<>(new ErrorResponse(status,
                "Es gab ein Fehler beim Anmelden. Bitte überprüfe deine Anmeldedaten und versuche es erneut."), status);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400

        log.warn("The provided arguments were invalid", illegalArgumentException);

        return new ResponseEntity<>(new ErrorResponse(status), status);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        log.error("An unexpected error has occurred", exception);

        return new ResponseEntity<>(new ErrorResponse(status,
                "Ein unerwarteter Fehler ist aufgetreten. Bitte melde dies einem Administrator mit Schritten zur Reproduktion dieses Fehlers."), status);
    }

}
