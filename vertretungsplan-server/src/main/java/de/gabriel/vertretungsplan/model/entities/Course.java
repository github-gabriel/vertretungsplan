package de.gabriel.vertretungsplan.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @NaturalId
    private String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Student> students;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private Set<TimetableEntry> timetableEntries;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    @Column(name = "substitution_plan_entry")
    private Set<SubstitutionPlanEntry> substitutionPlanEntries;

    public Course(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return name.equals(course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
