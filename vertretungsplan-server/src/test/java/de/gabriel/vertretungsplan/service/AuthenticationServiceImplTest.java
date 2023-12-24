package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.dto.AuthenticationRequest;
import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.User;
import de.gabriel.vertretungsplan.repository.UserRepository;
import de.gabriel.vertretungsplan.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    @DisplayName("Authenticate User with Email and Password and return JWT")
    public void authenticate_shouldAuthenticateGivenUsernameAndPassword() {
        User user = new Administrator(
                "administrator",
                "administrator@gmail.com",
                "administrator"
        );
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                user.getEmail(),
                user.getPassword()
        );

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertEquals(jwtService.generateToken(new UserDetailsImpl(user)), authenticationService.authenticate(authenticationRequest));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, atMost(2)).generateToken(any(UserDetailsImpl.class));
    }

}
