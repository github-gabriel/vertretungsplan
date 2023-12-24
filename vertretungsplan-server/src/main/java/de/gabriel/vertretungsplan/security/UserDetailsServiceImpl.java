package de.gabriel.vertretungsplan.security;

import de.gabriel.vertretungsplan.exception.exceptions.data.UserNotFoundException;
import de.gabriel.vertretungsplan.model.entities.users.User;
import de.gabriel.vertretungsplan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("The user was not found!"));
        return new UserDetailsImpl(user);
    }

}
