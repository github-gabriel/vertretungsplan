package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
