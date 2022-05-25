package de.gabriel.vertretungsplan.services;

import de.gabriel.vertretungsplan.models.MyUserDetails;
import de.gabriel.vertretungsplan.models.User;
import de.gabriel.vertretungsplan.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);

        user.orElseThrow(() -> new UsernameNotFoundException("Username " + userName + " not found!"));

        return user.map(MyUserDetails::new).get();
    }

    public String getFaecherFromUser(User user) {
        return user.getFaecher();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).get();
    }

    public void deleteUser(Integer userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("Student with the ID " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getMissingTeachers() {
        return userRepository.findAllMissingTeacher();
    }

    public List<User> getPresentTeachers() {
        return userRepository.findAllPresentTeacher();
    }

    public List<String> getMissingTeachersNames() {
        return userRepository.findAllMissingTeacherNames();
    }

    public List<String> getPresentTeachersNames() {
        return userRepository.findAllPresentTeacherNames();
    }

    public String getVertreter(String fach) {
        try {
            return userRepository.getVertreter(fach).get(0).getUserName();
        } catch (IndexOutOfBoundsException e) {
            return "N/A";
        }
    }

}
