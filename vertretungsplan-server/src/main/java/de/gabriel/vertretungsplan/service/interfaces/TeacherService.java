package de.gabriel.vertretungsplan.service.interfaces;

import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;

import java.util.List;

public interface TeacherService {

    Integer getTotalWorkHours(Teacher teacher);

    SubstitutionPlanEntry getSubstitutionPlanEntryForTimetableEntry(Teacher absentTeacher, TimetableEntry timetableEntryAbsentTeacher, Teacher substitute);

    Teacher getBestSubstitute(List<Teacher> moeglicheVertreter, TimetableEntry timetableEntryAbsentTeacher);

    List<SubstitutionPlanEntry> getSubstitutionPlanEntries(List<Teacher> abwesendeTeacher);


}
