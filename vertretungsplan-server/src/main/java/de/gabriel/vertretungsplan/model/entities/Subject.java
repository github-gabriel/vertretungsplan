package de.gabriel.vertretungsplan.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @NaturalId
    private String name;

    @NaturalId
    private String abbreviation;

    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "subjects")
    @JsonBackReference
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.MERGE)
    private Set<TimetableEntry> timetableEntries;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.MERGE)
    @Column(name = "substitution_plan_entry")
    private Set<SubstitutionPlanEntry> substitutionPlanEntries;

    public Subject(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return name.equals(subject.name) && abbreviation.equals(subject.abbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, abbreviation);
    }

}
