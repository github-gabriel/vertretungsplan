package de.gabriel.vertretungsplan.exceptions;

import de.gabriel.vertretungsplan.exception.CustomExceptionHandler;
import de.gabriel.vertretungsplan.exception.ErrorResponse;
import de.gabriel.vertretungsplan.exception.exceptions.authentication.AuthenticationException;
import de.gabriel.vertretungsplan.exception.exceptions.data.DataNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

public class CustomExceptionHandlerTest {

    private final CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();

    @Test
    @DisplayName("Test custom exception handling of DataNotFoundException")
    public void handleDataNotFoundException() {
        DataNotFoundException exception = mock(DataNotFoundException.class);

        ResponseEntity<ErrorResponse> response = customExceptionHandler.handleDataNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.name(), Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getCode());
        assertThat(new Date()).isCloseTo(response.getBody().getTimestamp(), 1000);
        assertNull(response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test custom exception handling of AuthenticationException")
    public void handleAuthenticationException() {
        AuthenticationException exception = mock(AuthenticationException.class);

        ResponseEntity<ErrorResponse> response = customExceptionHandler.handleAuthenticationException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(HttpStatus.UNAUTHORIZED.name(), Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().getCode());
        assertEquals("Es gab ein Fehler beim Anmelden. Bitte überprüfe deine Anmeldedaten und versuche es erneut.",
                response.getBody().getMessage());
        assertThat(new Date()).isCloseTo(response.getBody().getTimestamp(), 1000);
    }

    @Test
    @DisplayName("Test custom exception handling of IllegalArgumentException")
    public void handleIllegalArgumentException() {
        IllegalArgumentException exception = mock(IllegalArgumentException.class);

        ResponseEntity<ErrorResponse> response = customExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.name(), Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getCode());
        assertThat(new Date()).isCloseTo(response.getBody().getTimestamp(), 1000);
        assertNull(response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test custom exception handling of unexpected (uncaught) exceptions")
    public void handleUnexpectedException() {
        Exception exception = mock(Exception.class);

        ResponseEntity<ErrorResponse> response = customExceptionHandler.handleUnexpectedException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getCode());
        assertThat(new Date()).isCloseTo(response.getBody().getTimestamp(), 1000);
        assertEquals("Ein unerwarteter Fehler ist aufgetreten. Bitte melde dies einem Administrator mit Schritten zur Reproduktion dieses Fehlers.",
                response.getBody().getMessage());
    }

}
