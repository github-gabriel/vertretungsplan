package de.gabriel.vertretungsplan.service.interfaces;

import de.gabriel.vertretungsplan.model.dto.*;
import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;

public interface ConverterDTO {

    SubstitutionPlanEntryResponse convertSubstitutionPlanEntryToDto(SubstitutionPlanEntry substitutionPlanEntry);

    SubstitutionPlanEntry convertSubstitutionPlanEntryDtoToEntity(CreateSubstitutionPlanEntryRequest createSubstitutionPlanEntryRequest);

    ClassResponse convertCourseToDto(Course course);

    TimetableEntryResponse convertTimetableEntryToDto(TimetableEntry timetableEntry);

    StudentResponse convertStudentToDto(Student student);

    TeacherResponse convertTeacherToDto(Teacher teacher);

    AdministrationResponse convertAdministratorToDto(Administrator administrator);

    EntityIdMaps initEntitiesDto();

}
