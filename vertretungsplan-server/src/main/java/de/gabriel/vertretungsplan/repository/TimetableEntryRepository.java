package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Integer> {

    List<TimetableEntry> findByTeacherAndDayEntity(Teacher teacher, DayEntity dayEntity);

    List<TimetableEntry> findByTeacher(Teacher teacher);

    List<TimetableEntry> findByCourse(Course course);

}
