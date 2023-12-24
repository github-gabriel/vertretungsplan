package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
}
