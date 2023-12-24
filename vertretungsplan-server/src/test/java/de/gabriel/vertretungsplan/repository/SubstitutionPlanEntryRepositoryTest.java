package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.entities.Subject;
import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Art;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Day;
import de.gabriel.vertretungsplan.model.enums.Typ;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SubstitutionPlanEntryRepositoryTest {

    @Autowired
    private SubstitutionPlanEntryRepository substitutionPlanEntryRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private DayRepository dayRepository;

    private SubstitutionPlanEntry substitutionPlanEntryExpected;
    private Teacher germanTeacher, vertreter;
    private Course course6A;

    @BeforeAll
    void init() {
        course6A = new Course("6a");
        Course course7A = new Course("7a");

        courseRepository.saveAll(List.of(
                course6A, course7A
        ));

        Subject german = new Subject("Deutsch", "DE");
        Subject mathe = new Subject("Mathe", "DE");

        subjectRepository.saveAll(List.of(
                german, mathe
        ));

        germanTeacher = new Teacher("germanTeacher", "LDE", "germanteacher@gmail.com", "germanTeacher", Attendance.ANWESEND);
        germanTeacher.setSubjects(List.of(german));

        vertreter = new Teacher("vertreter", "LVE", "vertreter@gmail.com", "vertreter", Attendance.ANWESEND);
        vertreter.setSubjects(List.of(german));

        Teacher mathTeacher = new Teacher("mathTeacher", "LMA", "mathteacher@gmail.com", "mathTeacher", Attendance.ANWESEND);
        mathTeacher.setSubjects(List.of(mathe));

        teacherRepository.saveAll(List.of(
                germanTeacher, mathTeacher, vertreter
        ));

        DayEntity montag = new DayEntity(Day.MONDAY);

        dayRepository.saveAll(List.of(
                montag
        ));

        substitutionPlanEntryExpected = new SubstitutionPlanEntry(
                course6A,
                1,
                Art.VERTRETUNG,
                germanTeacher,
                vertreter,
                german,
                Typ.MANUELL
        );

        substitutionPlanEntryRepository.save(substitutionPlanEntryExpected);
    }

    @AfterAll
    void tearDown() {
        substitutionPlanEntryRepository.deleteAll();
        dayRepository.deleteAll();
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    @Test
    @DisplayName("Verify Deletion of All Substitution Plan Entries by Type")
    void deleteAllByTyp_shouldDeleteAllByType() {
        substitutionPlanEntryRepository.deleteAllByTyp(Typ.AUTO);

        List<SubstitutionPlanEntry> substitutionPlanEntries = substitutionPlanEntryRepository.findAll();

        assertEquals(substitutionPlanEntryExpected.hashCode(), substitutionPlanEntries.get(0).hashCode());
        assertEquals(1, substitutionPlanEntries.size());
    }

    @Test
    @DisplayName("Validate Retrieval of All Substitution Plan Entries for a Specific Teacher")
    void findByTeacher_shouldReturnSubstitutionPlanEntriesGivenTeacher() {
        List<SubstitutionPlanEntry> substitutionPlanEntries = substitutionPlanEntryRepository.findByTeacher(germanTeacher);

        assertEquals(substitutionPlanEntryExpected.hashCode(), substitutionPlanEntries.get(0).hashCode());
        assertEquals(1, substitutionPlanEntries.size());
    }

    @Test
    @DisplayName("Validate Retrieval of All Substitution Plan Entries for a Specific Substitute")
    void findBySubstitute_shouldReturnSubstitutionPlanEntriesGivenSubstitute() {
        List<SubstitutionPlanEntry> substitutionPlanEntries = substitutionPlanEntryRepository.findBySubstitute(vertreter);

        assertEquals(substitutionPlanEntryExpected.hashCode(), substitutionPlanEntries.get(0).hashCode());
        assertEquals(1, substitutionPlanEntries.size());
    }

    @Test
    @DisplayName("Validate Retrieval of All Substitution Plan Entries for a Given Class")
    void findByCourse_shouldReturnSubstitutionPlanEntriesGivenClass() {
        List<SubstitutionPlanEntry> substitutionPlanEntries = substitutionPlanEntryRepository.findByCourse(course6A);

        assertEquals(substitutionPlanEntryExpected.hashCode(), substitutionPlanEntries.get(0).hashCode());
        assertEquals(1, substitutionPlanEntries.size());
    }

}
