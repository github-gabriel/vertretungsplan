package de.gabriel.vertretungsplan.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gabriel.vertretungsplan.exception.ErrorResponse;
import de.gabriel.vertretungsplan.exception.exceptions.authentication.InvalidJwtException;
import de.gabriel.vertretungsplan.security.filter.ExceptionHandlerFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ExceptionHandlerFilterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ExceptionHandlerFilter exceptionHandlerFilter = new ExceptionHandlerFilter();

    @Test
    @DisplayName("Verify that the ExceptionHandlerFilter handles JWT Exceptions correctly")
    public void doFilterInternal_shouldHandleJwtException() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(writer);
        doThrow(new InvalidJwtException("Invalid JWT")).when(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));

        exceptionHandlerFilter.doFilter(req, res, chain);

        verify(res).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(res).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertEquals(objectMapper.writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED, "Es gab ein Problem beim Einloggen. Bitte versuche dich erneut einzuloggen.")),
                stringWriter.toString());
    }

    @Test
    @DisplayName("Verify that the ExceptionHandlerFilter catches Exception at Runtime correctly")
    public void doFilterInternal_shouldHandleGeneralExceptions() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(res.getWriter()).thenReturn(writer);
        doThrow(new RuntimeException("Other exception")).when(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));

        exceptionHandlerFilter.doFilter(req, res, chain);

        verify(res).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(res).setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertEquals(objectMapper.writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED)), stringWriter.toString());
    }

}
