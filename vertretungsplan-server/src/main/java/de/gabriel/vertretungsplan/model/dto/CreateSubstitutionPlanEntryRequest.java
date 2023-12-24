package de.gabriel.vertretungsplan.model.dto;

import de.gabriel.vertretungsplan.model.enums.Art;
import de.gabriel.vertretungsplan.model.enums.Typ;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class CreateSubstitutionPlanEntryRequest {


    private int courseId;

    private int hour;

    private Art art;

    private Typ typ;

    private int teacherId;

    private int substituteId;

    private int subjectId;

    public CreateSubstitutionPlanEntryRequest() {
    }

    public CreateSubstitutionPlanEntryRequest(int courseId, int hour, Art art, Typ typ, int teacherId, int subjectId) {
        this.courseId = courseId;
        this.hour = hour;
        this.art = art;
        this.typ = typ;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    public CreateSubstitutionPlanEntryRequest(int courseId, int hour, Art art, Typ typ, int teacherId, int substituteId, int subjectId) {
        this.courseId = courseId;
        this.hour = hour;
        this.art = art;
        this.typ = typ;
        this.teacherId = teacherId;
        this.substituteId = substituteId;
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateSubstitutionPlanEntryRequest that = (CreateSubstitutionPlanEntryRequest) o;
        return courseId == that.courseId && hour == that.hour && teacherId == that.teacherId && substituteId == that.substituteId && subjectId == that.subjectId && art == that.art && typ == that.typ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, hour, art, typ, teacherId, substituteId, subjectId);
    }
}
