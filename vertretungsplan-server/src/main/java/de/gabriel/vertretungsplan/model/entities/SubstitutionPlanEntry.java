package de.gabriel.vertretungsplan.model.entities;

import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Art;
import de.gabriel.vertretungsplan.model.enums.Typ;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "substitution_plan_entry")
public class SubstitutionPlanEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "entry_hour")
    private int hour;

    @Enumerated(EnumType.STRING)
    private Art art;

    @Enumerated(EnumType.STRING)
    private Typ typ;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "substitute_id")
    private Teacher substitute;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public SubstitutionPlanEntry(Course course, int hour, Art art, Teacher teacher, Subject subject, Typ typ) {
        this.course = course;
        this.hour = hour;
        this.art = art;
        this.teacher = teacher;
        this.subject = subject;
        this.typ = typ;
    }

    public SubstitutionPlanEntry(Course course, int hour, Art art, Teacher teacher, Teacher substitute, Subject subject, Typ typ) {
        this.course = course;
        this.hour = hour;
        this.art = art;
        this.teacher = teacher;
        this.substitute = substitute;
        this.subject = subject;
        this.typ = typ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubstitutionPlanEntry that = (SubstitutionPlanEntry) o;
        return Id == that.Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

}
