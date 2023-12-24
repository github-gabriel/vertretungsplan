package de.gabriel.vertretungsplan.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gabriel.vertretungsplan.exception.ErrorResponse;
import de.gabriel.vertretungsplan.exception.exceptions.authentication.InvalidJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,
                                 @NonNull HttpServletResponse response,
                                 @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            HttpStatus status = HttpStatus.UNAUTHORIZED; // 401

            log.warn("An error occurred while Authenticating", e);

            final ErrorResponse errorResponse = getErrorResponse(e, status);

            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
        }
    }

    private ErrorResponse getErrorResponse(RuntimeException e, HttpStatus status) {
        ErrorResponse errorResponse;
        if (e instanceof InvalidJwtException || e instanceof MalformedJwtException) {
            errorResponse = new ErrorResponse(status, "Es gab ein Problem beim Einloggen. Bitte versuche dich erneut einzuloggen.");
        } else if (e instanceof ExpiredJwtException) {
            errorResponse = new ErrorResponse(status, "Deine Sitzung ist abgelaufen. Bitte logge dich erneut ein.");
        } else {
            errorResponse = new ErrorResponse(status);
        }
        return errorResponse;
    }

}