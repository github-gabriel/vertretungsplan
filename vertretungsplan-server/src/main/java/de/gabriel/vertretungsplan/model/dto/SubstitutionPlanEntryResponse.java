package de.gabriel.vertretungsplan.model.dto;

import de.gabriel.vertretungsplan.model.enums.Art;
import de.gabriel.vertretungsplan.model.enums.Typ;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SubstitutionPlanEntryResponse {

    private int id;

    private String course;

    private int hour;

    private Art art;

    private Typ typ;

    private String teacher;

    private String substitute;

    private String subject;

    public SubstitutionPlanEntryResponse() {

    }

    public SubstitutionPlanEntryResponse(int id, String course, int hour, Art art, Typ typ, String teacher, String subject) { // GET
        this.course = course;
        this.hour = hour;
        this.art = art;
        this.typ = typ;
        this.teacher = teacher;
        this.subject = subject;
        this.id = id;
    }

    public SubstitutionPlanEntryResponse(int id, String course, int hour, Art art, Typ typ, String teacher, String substitute, String subject) { // GET
        this.course = course;
        this.hour = hour;
        this.art = art;
        this.typ = typ;
        this.teacher = teacher;
        this.substitute = substitute;
        this.subject = subject;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubstitutionPlanEntryResponse that = (SubstitutionPlanEntryResponse) o;
        return hour == that.hour && course.equals(that.course) && art == that.art && typ == that.typ && teacher.equals(that.teacher) && Objects.equals(substitute, that.substitute) && subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, hour, art, typ, teacher, substitute, subject);
    }
}
