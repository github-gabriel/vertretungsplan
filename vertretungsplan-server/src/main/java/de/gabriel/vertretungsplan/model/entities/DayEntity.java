package de.gabriel.vertretungsplan.model.entities;

import de.gabriel.vertretungsplan.model.enums.Day;
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
@Table(name = "days")
public class DayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "day_name")
    private Day dayName;

    @OneToMany(mappedBy = "dayEntity")
    private Set<TimetableEntry> timetableEntries;

    public DayEntity(Day dayName) {
        this.dayName = dayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayEntity dayEntity = (DayEntity) o;
        return dayName == dayEntity.dayName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayName);
    }
}
