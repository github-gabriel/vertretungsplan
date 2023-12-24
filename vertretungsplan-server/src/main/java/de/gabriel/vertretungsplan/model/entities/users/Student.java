package de.gabriel.vertretungsplan.model.entities.users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student extends User {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    @JsonBackReference
    private Course course;

    public Student(String name, String email, String password, Course course) {
        super(name, email, password, Role.getRoleWithPrefix(Role.STUDENT));
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return course.equals(student.course);
    }

}
