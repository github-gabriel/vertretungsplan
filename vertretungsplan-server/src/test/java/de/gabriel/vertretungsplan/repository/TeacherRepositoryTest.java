package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher chemistryTeacherExpected;

    private Teacher mathTeacherExpected;

    @BeforeAll
    void init() {
        chemistryTeacherExpected = new Teacher("chemistryteacher", "LCH", "chemistryteacher@gmail.com", "chemistryteacher", Attendance.ANWESEND);
        mathTeacherExpected = new Teacher("mathteacher", "LMA", "mathteacher@gmail.com", "mathteacher", Attendance.ABWESEND);

        teacherRepository.saveAll(List.of(
                chemistryTeacherExpected, mathTeacherExpected
        ));
    }

    @AfterAll
    void tearDown() {
        teacherRepository.deleteAll();
    }

    @Test
    @DisplayName("Retrieve Teacher by Attendance")
    void findByAttendance_shouldReturnTeacherByAttendance() {
        List<Teacher> anwesend = teacherRepository.findByAttendance(Attendance.ANWESEND);
        List<Teacher> abwesend = teacherRepository.findByAttendance(Attendance.ABWESEND);

        assertThat(anwesend).isNotNull();
        assertThat(abwesend).isNotNull();

        assertEquals(chemistryTeacherExpected.hashCode(), anwesend.get(0).hashCode());
        assertEquals(mathTeacherExpected.hashCode(), abwesend.get(0).hashCode());
    }

    @Test
    @DisplayName("Retrieve Teacher by Email")
    void findByEmail_shouldReturnTeacherByEmail() {
        Optional<Teacher> chemistryTeacher = teacherRepository.findByEmail(chemistryTeacherExpected.getEmail());

        assertTrue(chemistryTeacher.isPresent());
        assertEquals(chemistryTeacherExpected.hashCode(), chemistryTeacher.get().hashCode());
    }

    @Test
    @DisplayName("Verify Unique Constraints for Teacher")
    void testUniqueConstraints() {
        // Email
        Teacher chemistryTeacher = new Teacher("chemistryteacher", "LCH", "chemistryteacher@gmail.com", "chemistryteacher", Attendance.ANWESEND);
        Teacher chemistryTeacher1 = new Teacher("chemistryteacher", "LCH1", "chemistryteacher@gmail.com", "chemistryteacher", Attendance.ANWESEND);

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> teacherRepository.saveAll(List.of(chemistryTeacher, chemistryTeacher1)));

        // Initials
        Teacher germanTeacher = new Teacher("germanteacher", "LDE", "germanteacher@gmail.com", "germanteacher", Attendance.ANWESEND);
        Teacher germanTeacher1 = new Teacher("germanteacher", "LDE", "germanteacher1@gmail.com", "germanteacher", Attendance.ANWESEND);

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> teacherRepository.saveAll(List.of(germanTeacher, germanTeacher1)));
    }

}