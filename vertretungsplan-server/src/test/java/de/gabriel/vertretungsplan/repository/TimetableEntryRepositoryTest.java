package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.entities.Subject;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Day;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimetableEntryRepositoryTest {

    @Autowired
    private TimetableEntryRepository timetableEntryRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private DayRepository dayRepository;

    private Teacher germanTeacher;
    private DayEntity montag;
    private TimetableEntry timetableEntryExpected;
    private Course course6A;

    @BeforeAll
    void init() {
        course6A = new Course("6a");

        courseRepository.saveAll(List.of(
                course6A
        ));

        Subject german = new Subject("Deutsch", "DE");

        subjectRepository.saveAll(List.of(
                german
        ));

        germanTeacher = new Teacher("germanTeacher", "LDE", "germanteacher@gmail.com", "germanTeacher", Attendance.ANWESEND);
        germanTeacher.setSubjects(List.of(german));

        teacherRepository.saveAll(List.of(
                germanTeacher
        ));

        montag = new DayEntity(Day.MONDAY);

        dayRepository.saveAll(List.of(
                montag
        ));

        timetableEntryExpected = new TimetableEntry(1, course6A, german, germanTeacher, montag);

        timetableEntryRepository.save(timetableEntryExpected);
    }

    @AfterAll
    void tearDown() {
        timetableEntryRepository.deleteAll();
        dayRepository.deleteAll();
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    @Test
    @DisplayName("Verify Timetable Entry Retrieval for Specific Teacher and Day")
    void findByTeacherAndDayEntity_shouldReturnTimetableEntriesGivenTeacherAndDay() {
        List<TimetableEntry> timetableEntry = timetableEntryRepository.findByTeacherAndDayEntity(germanTeacher, montag);

        assertEquals(timetableEntryExpected.hashCode(), timetableEntry.get(0).hashCode());
    }

    @Test
    @DisplayName("Verify Timetable Entry Retrieval for Specific Teacher")
    void findByTeacher_shouldReturnTimetableEntriesGivenTeacher() {
        List<TimetableEntry> timetableEntry = timetableEntryRepository.findByTeacher(germanTeacher);

        assertEquals(timetableEntryExpected.hashCode(), timetableEntry.get(0).hashCode());
    }

    @Test
    @DisplayName("Verify Timetable Entry Retrieval for Specific Course")
    void findByCourse_shouldReturnTimetableEntriesGivenCourse() {
        List<TimetableEntry> timetableEntry = timetableEntryRepository.findByCourse(course6A);

        assertEquals(timetableEntryExpected.hashCode(), timetableEntry.get(0).hashCode());
    }

}