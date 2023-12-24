package de.gabriel.vertretungsplan.controller;

import de.gabriel.vertretungsplan.model.dto.AuthenticationRequest;
import de.gabriel.vertretungsplan.service.interfaces.AuthenticationService;
import de.gabriel.vertretungsplan.utils.logging.request.LogRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static de.gabriel.vertretungsplan.config.ApplicationConfig.API_BASE;

@Tag(name = "Authentication Controller", description = "Provides endpoints for authenticating and logging out users")
@RequiredArgsConstructor
@RestController
@RequestMapping(API_BASE + "/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Authenticates a user")
    @LogRequest(message = "User successfully authenticated")
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request) {
        ResponseCookie cookie = ResponseCookie.from("jwt", authenticationService.authenticate(authenticationRequest))
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @Operation(summary = "Logs out a user")
    @LogRequest(message = "User successfully logged out")
    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();

        ResponseCookie cookie = ResponseCookie.from("jwt", null)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

}
