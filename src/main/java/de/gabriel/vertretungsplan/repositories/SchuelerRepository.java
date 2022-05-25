package de.gabriel.vertretungsplan.repositories;

import de.gabriel.vertretungsplan.models.Schueler;
import de.gabriel.vertretungsplan.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SchuelerRepository extends JpaRepository<Schueler, Integer> {

    Optional<Schueler> findByUserName(String userName);

}
