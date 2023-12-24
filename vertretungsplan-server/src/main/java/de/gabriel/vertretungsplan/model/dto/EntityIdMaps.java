package de.gabriel.vertretungsplan.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EntityIdMaps {

    private HashMap<String, Integer> courses = new HashMap<>();

    private HashMap<String, Integer> teachers = new HashMap<>();

    private HashMap<String, Integer> subjects = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityIdMaps that = (EntityIdMaps) o;
        return courses.equals(that.courses) && teachers.equals(that.teachers) && subjects.equals(that.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courses, teachers, subjects);
    }
}
