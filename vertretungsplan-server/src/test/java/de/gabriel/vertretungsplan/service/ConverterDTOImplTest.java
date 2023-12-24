package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.dto.*;
import de.gabriel.vertretungsplan.model.entities.*;
import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Art;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Day;
import de.gabriel.vertretungsplan.model.enums.Typ;
import de.gabriel.vertretungsplan.repository.CourseRepository;
import de.gabriel.vertretungsplan.repository.SubjectRepository;
import de.gabriel.vertretungsplan.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConverterDTOImplTest {

    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private ConverterDTOImpl converterDTO;

    private static Course course;
    private static Teacher teacher;
    private static Administrator administrator;
    private static Student student;
    private static TimetableEntry timetableEntry;
    private static SubstitutionPlanEntry substitutionPlanEntry;
    private static Subject subject;

    @BeforeAll
    static void init() {
        subject = new Subject(
                "Deutsch",
                "DE"
        );
        course = new Course(
                "6a"
        );
        DayEntity montag = new DayEntity(
                Day.MONDAY
        );
        teacher = new Teacher(
                "germanteacher",
                "LDE",
                "germanteacher@gmail.com",
                "germanteacher",
                Attendance.ANWESEND
        );
        teacher.setSubjects(List.of(subject));
        Teacher vertreter = new Teacher(
                "germanteacher2",
                "LDE2",
                "germanteacher2@gmail.com",
                "germanteacher2",
                Attendance.ANWESEND
        );
        vertreter.setSubjects(List.of(subject));
        administrator = new Administrator(
                "Administrator",
                "administrator@gmail.com",
                "administrator"
        );
        student = new Student(
                "student",
                "student@gmail.com",
                "student",
                course
        );
        course.setStudents(Set.of(student));
        timetableEntry = new TimetableEntry(
                1,
                course,
                subject,
                teacher,
                montag
        );
        substitutionPlanEntry = new SubstitutionPlanEntry(
                course,
                1,
                Art.ENTFALL,
                teacher,
                subject,
                Typ.MANUELL
        );
    }

    @Test
    @DisplayName("Convert Substitution Plan Entry to DTO")
    public void convertSubstitutionPlanEntryToDto_shouldConvertSubstitutionPlanEntryToDTO() {
        SubstitutionPlanEntryResponse substitutionPlanEntryResponse = converterDTO.convertSubstitutionPlanEntryToDto(substitutionPlanEntry);

        assertEquals(substitutionPlanEntry.getCourse().getName(), substitutionPlanEntryResponse.getCourse());
        assertEquals(substitutionPlanEntry.getHour(), substitutionPlanEntryResponse.getHour());
        assertEquals(substitutionPlanEntry.getArt(), substitutionPlanEntryResponse.getArt());
        assertEquals(substitutionPlanEntry.getTeacher().getInitials(), substitutionPlanEntryResponse.getTeacher());
        assertEquals(substitutionPlanEntry.getSubject().getName(), substitutionPlanEntryResponse.getSubject());
        assertEquals(substitutionPlanEntry.getTyp(), substitutionPlanEntryResponse.getTyp());
    }

    @Test
    @DisplayName("Convert Substitution Plan Entry DTO to Entity")
    public void convertSubstitutionPlanEntryDtoToEntity_shouldConvertSubstitutionPlanEntryDTOToEntity() {
        when(courseRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(course));
        when(teacherRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(teacher));
        when(subjectRepository.findById(any(Integer.class))).thenReturn(Optional.ofNullable(subject));

        SubstitutionPlanEntry substitutionPlanEntryTest = converterDTO.convertSubstitutionPlanEntryDtoToEntity(new CreateSubstitutionPlanEntryRequest(
                course.getId(),
                1,
                Art.ENTFALL,
                Typ.MANUELL,
                teacher.getId(),
                subject.getId()
        ));

        assertEquals(substitutionPlanEntry.hashCode(), substitutionPlanEntryTest.hashCode());
    }

    @Test
    @DisplayName("Convert Course to DTO")
    public void convertCourseToDto_shouldConvertClassToDTO() {
        ClassResponse classResponse = converterDTO.convertCourseToDto(course);

        assertEquals(course.getName(), classResponse.getName());
    }

    @Test
    @DisplayName("Convert Timetable Entry to DTO")
    public void convertTimetableEntryToDto_shouldConvertTimetableEntryToDTO() {
        TimetableEntryResponse timetableEntryResponse = converterDTO.convertTimetableEntryToDto(timetableEntry);

        assertEquals(timetableEntry.getHour(), timetableEntryResponse.getHour());
        assertEquals(timetableEntry.getCourse().getName(), timetableEntryResponse.getCourse());
        assertEquals(timetableEntry.getSubject().getAbbreviation(), timetableEntryResponse.getSubject());
        assertEquals(timetableEntry.getTeacher().getInitials(), timetableEntryResponse.getTeacher());
        assertEquals(timetableEntry.getDayEntity().getDayName().name(), timetableEntryResponse.getDay());
    }

    @Test
    @DisplayName("Convert Student to DTO")
    public void convertStudentToDto_shouldConvertStudentToDTO() {
        StudentResponse studentResponse = converterDTO.convertStudentToDto(student);

        assertEquals(student.getUsername(), studentResponse.getName());
        assertEquals(student.getEmail(), studentResponse.getEmail());
        assertEquals(student.getCourse().getName(), studentResponse.getCourse());
    }

    @Test
    @DisplayName("Convert Teacher to DTO")
    public void convertTeacherToDto_shouldConvertTeacherToDTO() {
        TeacherResponse teacherResponse = converterDTO.convertTeacherToDto(teacher);

        assertEquals(teacher.getUsername(), teacherResponse.getName());
        assertEquals(teacher.getEmail(), teacherResponse.getEmail());
        assertEquals(teacher.getAttendance().name(), teacherResponse.getAttendance());
        assertEquals(teacher.getRole(), teacherResponse.getRole());
    }

    @Test
    @DisplayName("Convert Administrator to DTO")
    public void convertAdministratorToDto_shouldConvertAdministratorToDTO() {
        AdministrationResponse administrationResponse = converterDTO.convertAdministratorToDto(administrator);

        assertEquals(administrator.getUsername(), administrationResponse.getName());
        assertEquals(administrator.getEmail(), administrationResponse.getEmail());
        assertEquals(administrator.getRole(), administrationResponse.getRole());
    }

    @Test
    @DisplayName("Retrieve Entities DTO containing Entities and their according IDs")
    public void initEntitiesDto_shouldReturnEntitiesDTO() {
        subject.setId(1);
        course.setId(1);
        teacher.setId(1);

        when(subjectRepository.findAll()).thenReturn(List.of(subject));
        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        EntityIdMaps entityIdMaps = converterDTO.initEntitiesDto();

        assertEquals(new EntityIdMaps(
                new HashMap<>(Map.of(course.getName(), course.getId())),
                new HashMap<>(Map.of(teacher.getInitials(), teacher.getId())),
                new HashMap<>(Map.of(subject.getName(), subject.getId()))
        ), entityIdMaps);
    }

}
