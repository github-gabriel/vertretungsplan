package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.User;
import de.gabriel.vertretungsplan.repository.UserRepository;
import de.gabriel.vertretungsplan.security.UserDetailsImpl;
import de.gabriel.vertretungsplan.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Load User Details by Email")
    public void loadUserByUsername_shouldReturnUserDetailsGivenEmail() {
        User user = mock(Administrator.class);

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername("test@gmail.com");

        assertEquals(new UserDetailsImpl(user).hashCode(), userDetails.hashCode());
    }

}
