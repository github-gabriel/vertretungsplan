package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.exception.exceptions.authentication.SpringAuthenticationException;
import de.gabriel.vertretungsplan.exception.exceptions.data.UserNotFoundException;
import de.gabriel.vertretungsplan.model.dto.AuthenticationRequest;
import de.gabriel.vertretungsplan.repository.UserRepository;
import de.gabriel.vertretungsplan.security.UserDetailsImpl;
import de.gabriel.vertretungsplan.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
            log.debug("User successfully authenticated; {[Email={}], [Password={}]}",
                    authenticationRequest.getEmail(), "*".repeat(authenticationRequest.getPassword().length()));
        } catch (AuthenticationException e) {
            throw new SpringAuthenticationException("The user was not found!");
        }
        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("The user was not found!"));

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        return jwtService.generateToken(userDetails);
    }

}
