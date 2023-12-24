package de.gabriel.vertretungsplan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gabriel.vertretungsplan.model.dto.AuthenticationRequest;
import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.security.UserDetailsImpl;
import de.gabriel.vertretungsplan.service.AuthenticationServiceImpl;
import de.gabriel.vertretungsplan.service.JwtService;
import de.gabriel.vertretungsplan.service.interfaces.AuthenticationService;
import de.gabriel.vertretungsplan.utils.CookieUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {AuthenticationServiceImpl.class, AuthenticationController.class})
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Authenticate User and Return Token in Cookie")
    public void authenticate_shouldAuthenticateUserAndReturnToken() throws Exception {
        String email = "administrator@gmail.com";
        String password = "administrator";

        JwtService jwtService = new JwtService();

        String token = jwtService.generateToken(new UserDetailsImpl(
                new Administrator(
                        "administrator",
                        email,
                        password
                )
        ));

        when(authenticationService.authenticate(Mockito.any(AuthenticationRequest.class)))
                .thenReturn(token);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .content(objectMapper.writeValueAsString(new AuthenticationRequest(
                                email,
                                password)
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String cookie = Objects.requireNonNull(result.getResponse().getHeader(HttpHeaders.SET_COOKIE));

        assertNotNull(cookie);

        assertEquals(token, CookieUtils.getCookieValue(cookie, "jwt"));
        assertEquals(60 * 60 * 24 * 7, CookieUtils.getMaxAge(cookie));
        assertEquals("/", CookieUtils.getPath(cookie));
        assertTrue(CookieUtils.isHttpOnly(cookie));

        verify(authenticationService, times(1)).authenticate(Mockito.any(AuthenticationRequest.class));
    }

    @Test
    @DisplayName("Logout User and Delete Token")
    public void logout_shouldLogoutUserAndDeleteToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        String cookie = Objects.requireNonNull(result.getResponse().getHeader(HttpHeaders.SET_COOKIE));

        assertNotNull(cookie);

        assertEquals("", CookieUtils.getCookieValue(cookie, "jwt"));
        assertEquals(0, CookieUtils.getMaxAge(cookie));
        assertTrue(CookieUtils.isHttpOnly(cookie));
    }

}
