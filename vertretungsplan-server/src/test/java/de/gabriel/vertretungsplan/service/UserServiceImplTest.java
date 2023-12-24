package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.repository.AdministratorRepository;
import de.gabriel.vertretungsplan.repository.StudentRepository;
import de.gabriel.vertretungsplan.repository.TeacherRepository;
import de.gabriel.vertretungsplan.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private AdministratorRepository administratorRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Save User with Encoded Passwords")
    public void saveUser_shouldSaveUserWithEncodedPassword() {
        Administrator administrator = new Administrator(
                "administrator",
                "administrator@gmail.com",
                "administrator"
        );

        userService.saveUser(administrator);

        assertEquals(passwordEncoder.encode("administrator"), administrator.getPassword());
        verify(userRepository, times(1)).save(administrator);
    }

    @Test
    @DisplayName("Save Users with Encoded Passwords")
    public void saveUsers_shouldSaveUsersWithEncodedPasswords() {
        Administrator administrator = new Administrator(
                "administrator",
                "administrator@gmail.com",
                "administrator"
        );

        userService.saveUsers(List.of(administrator));

        assertEquals(passwordEncoder.encode("administrator"), administrator.getPassword());
        verify(userRepository, times(1)).saveAll(List.of(administrator));
    }

    @Test
    @DisplayName("Save Teacher with Encoded Passwords")
    public void saveTeacher_shouldSaveTeacherWithEncodedPassword() {
        Teacher teacher = new Teacher(
                "teacher",
                "L",
                "teacher@gmail.com",
                "teacher",
                Attendance.ANWESEND
        );


        userService.saveTeacher(teacher);

        assertEquals(passwordEncoder.encode("teacher"), teacher.getPassword());
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    @DisplayName("Save Teachers with Encoded Passwords")
    public void saveTeachers_shouldSaveTeachersWithEncodedPasswords() {
        Teacher teacher = new Teacher(
                "teacher",
                "L",
                "teacher@gmail.com",
                "teacher",
                Attendance.ANWESEND
        );

        userService.saveTeachers(List.of(teacher));

        assertEquals(passwordEncoder.encode("teacher"), teacher.getPassword());
        verify(teacherRepository, times(1)).saveAll(List.of(teacher));
    }

    @Test
    @DisplayName("Save Student with Encoded Passwords")
    public void saveStudent_shouldSaveStudentWithEncodedPassword() {
        Student student = new Student(
                "student",
                "student@gmail.com",
                "student",
                new Course("5a")
        );

        userService.saveStudent(student);

        assertEquals(passwordEncoder.encode("student"), student.getPassword());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    @DisplayName("Save Students with Encoded Passwords")
    public void saveStudents_shouldSaveStudentsWithEncodedPasswords() {
        Student student = new Student(
                "student",
                "student@gmail.com",
                "student",
                new Course("5a")
        );

        userService.saveStudents(List.of(student));

        assertEquals(passwordEncoder.encode("student"), student.getPassword());
        verify(studentRepository, times(1)).saveAll(List.of(student));
    }

    @Test
    @DisplayName("Save Administrator with Encoded Passwords")
    public void saveAdministrator_shouldSaveAdministratorWithEncodedPassword() {
        Administrator administrator = new Administrator(
                "administrator",
                "administrator@gmail.com",
                "administrator"
        );

        userService.saveAdministrator(administrator);

        assertEquals(passwordEncoder.encode("administrator"), administrator.getPassword());
        verify(administratorRepository, times(1)).save(administrator);
    }

    @Test
    @DisplayName("Save Administrators with Encoded Passwords")
    public void saveAdministrators_shouldSaveAdministratorWithEncodedPasswords() {
        Administrator administrator = new Administrator(
                "administrator",
                "administrator@gmail.com",
                "administrator"
        );

        userService.saveAdministrators(List.of(administrator));

        assertEquals(passwordEncoder.encode("administrator"), administrator.getPassword());
        verify(administratorRepository, times(1)).saveAll(List.of(administrator));
    }

}
