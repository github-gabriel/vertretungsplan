package de.gabriel.vertretungsplan.model.entities.users;

import de.gabriel.vertretungsplan.model.entities.Subject;
import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "teacher")
public class Teacher extends User {

    @Column(unique = true)
    private String initials;

    @Enumerated(EnumType.STRING)
    private Attendance attendance;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;

    @OneToMany(mappedBy = "teacher")
    private Set<TimetableEntry> timetableEntries;

    @OneToMany(mappedBy = "teacher")
    @Column(name = "substitution_plan_entry")
    private Set<SubstitutionPlanEntry> substitutionPlanEntriesTeacher;

    @OneToMany(mappedBy = "substitute")
    @Column(name = "substitution_plan_entry")
    private Set<SubstitutionPlanEntry> substitutionPlanEntriesSubstitute;

    public Teacher(String name, String initials, String email, String password, Attendance attendance) {
        super(name, email, password, Role.getRoleWithPrefix(Role.TEACHER));
        this.initials = initials;
        this.attendance = attendance;
    }

}
