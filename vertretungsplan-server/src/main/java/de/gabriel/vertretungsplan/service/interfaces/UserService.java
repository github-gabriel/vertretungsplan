package de.gabriel.vertretungsplan.service.interfaces;

import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.entities.users.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);

    void saveUsers(List<User> users);

    void saveTeacher(Teacher teacher);

    void saveTeachers(List<Teacher> teacher);

    void saveStudent(Student student);

    void saveStudents(List<Student> student);

    void saveAdministrator(Administrator administrator);

    void saveAdministrators(List<Administrator> administrator);

}
