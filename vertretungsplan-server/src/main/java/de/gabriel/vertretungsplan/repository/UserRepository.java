package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
