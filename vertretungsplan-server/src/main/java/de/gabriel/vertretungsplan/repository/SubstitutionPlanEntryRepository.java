package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Typ;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubstitutionPlanEntryRepository extends JpaRepository<SubstitutionPlanEntry, Integer> {

    @Transactional
    void deleteAllByTyp(Typ typ);

    List<SubstitutionPlanEntry> findByTeacher(Teacher teacher);

    List<SubstitutionPlanEntry> findBySubstitute(Teacher teacher);

    List<SubstitutionPlanEntry> findByCourse(Course course);

}
