package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Course findByName(String name);

}
