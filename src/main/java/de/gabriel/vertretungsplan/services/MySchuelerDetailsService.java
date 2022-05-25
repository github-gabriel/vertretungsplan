package de.gabriel.vertretungsplan.services;

import de.gabriel.vertretungsplan.models.MySchuelerDetails;
import de.gabriel.vertretungsplan.models.MyUserDetails;
import de.gabriel.vertretungsplan.models.Schueler;
import de.gabriel.vertretungsplan.models.User;
import de.gabriel.vertretungsplan.repositories.SchuelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MySchuelerDetailsService implements UserDetailsService {

    @Autowired
    SchuelerRepository schuelerRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Schueler> schueler = schuelerRepository.findByUserName(userName);

        schueler.orElseThrow(() -> new UsernameNotFoundException("Username " + userName + " not found!"));

        return schueler.map(MySchuelerDetails::new).get();
    }

    public Schueler saveStudent(Schueler schueler){
        return schuelerRepository.save(schueler);
    }

    public List<Schueler> getStudents() {
        return schuelerRepository.findAll();
    }

    public Schueler getStudentById(Integer id) {
        return schuelerRepository.findById(id).get();
    }

    public void deleteStudent(Integer schuelerId) {
        boolean exists = schuelerRepository.existsById(schuelerId);
        if (!exists) {
            throw new IllegalStateException("Student with the ID " + schuelerId + " does not exist");
        }
        schuelerRepository.deleteById(schuelerId);
    }

    public Schueler updateStudent(Schueler schueler) {
        return schuelerRepository.save(schueler);
    }

}
