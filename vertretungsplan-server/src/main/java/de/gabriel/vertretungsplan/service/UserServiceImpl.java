package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.entities.users.User;
import de.gabriel.vertretungsplan.repository.AdministratorRepository;
import de.gabriel.vertretungsplan.repository.StudentRepository;
import de.gabriel.vertretungsplan.repository.TeacherRepository;
import de.gabriel.vertretungsplan.repository.UserRepository;
import de.gabriel.vertretungsplan.service.interfaces.UserService;
import de.gabriel.vertretungsplan.utils.logging.user.LogUserSave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final AdministratorRepository administratorRepository;

    @Override
    @LogUserSave
    public void saveUser(User user) {
        if (user == null || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Invalid User data!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @LogUserSave
    public void saveUsers(List<User> users) {
        if (users.isEmpty()) {
            throw new IllegalArgumentException("The Users List is empty!");
        }

        users.forEach(user -> {
            if (user == null || user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Invalid User data!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        });
        userRepository.saveAll(users);
    }

    @Override
    @LogUserSave
    public void saveTeacher(Teacher teacher) {
        if (teacher == null || teacher.getPassword() == null || teacher.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Invalid Teacher data!");
        }

        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepository.save(teacher);
    }

    @Override
    @LogUserSave
    public void saveTeachers(List<Teacher> teacher) {
        if (teacher.isEmpty()) {
            throw new IllegalArgumentException("The Teacher List is empty!");
        }

        teacher.forEach(user -> {
            if (user == null || user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Invalid Teacher data!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        });
        teacherRepository.saveAll(teacher);
    }

    @Override
    @LogUserSave
    public void saveStudent(Student student) {
        if (student == null || student.getPassword() == null || student.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Invalid Student data!");
        }

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepository.save(student);
    }

    @Override
    @LogUserSave
    public void saveStudents(List<Student> student) {
        if (student.isEmpty()) {
            throw new IllegalArgumentException("The Students List is empty!");
        }

        student.forEach(user -> {
            if (user == null || user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Invalid Student data!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        });
        studentRepository.saveAll(student);
    }

    @Override
    @LogUserSave
    public void saveAdministrator(Administrator administrator) {
        if (administrator == null || administrator.getPassword() == null || administrator.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Invalid Administrator data!");
        }

        administrator.setPassword(passwordEncoder.encode(administrator.getPassword()));
        administratorRepository.save(administrator);
    }

    @Override
    @LogUserSave
    public void saveAdministrators(List<Administrator> administrator) {
        if (administrator.isEmpty()) {
            throw new IllegalArgumentException("The Administrators List is empty!");
        }

        administrator.forEach(user -> {
            if (user == null || user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Invalid Administrator data!");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        });
        administratorRepository.saveAll(administrator);
    }

}
