package de.gabriel.vertretungsplan.service.interfaces;

import de.gabriel.vertretungsplan.model.dto.*;
import de.gabriel.vertretungsplan.model.enums.Typ;

import java.util.List;

public interface ResourceService {

    List<ClassResponse> getAllCourses();

    List<TimetableEntryResponse> getTimetableEntriesByTeacher(String jwt);

    List<TimetableEntryResponse> getTimetableEntriesByCourse(String course);

    List<StudentResponse> getAllStudents();

    List<TeacherResponse> getAllTeachers();

    AttendanceStatus getTeacherAttendanceStatus(String jwt);

    void setTeacherAttendanceStatus(String jwt, SetTeacherAttendanceRequest setTeacherAttendanceRequest);

    List<AdministrationResponse> getAllAdministrators();

    List<SubstitutionPlanEntryResponse> getSubstitutionPlan(Typ typ);

    List<SubstitutionPlanEntryResponse> getSubstitutionPlan();

    List<SubstitutionPlanEntryResponse> getSubstitutionPlanEntriesForTeacher(String jwt);

    CreateSubstitutionPlanEntryResponse createSubstitutionPlanEntry(CreateSubstitutionPlanEntryRequest request);

    void deleteSubstitutionPlanEntry(Integer id);

    UserRole getCurrentUserRole(String jwt);

    CurrentDay getCurrentDay();

    EntityIdMaps getEntitiesWithAccordingIds();

}
