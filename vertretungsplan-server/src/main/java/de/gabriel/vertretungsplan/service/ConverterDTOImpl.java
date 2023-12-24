package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.dto.*;
import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.Subject;
import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.repository.CourseRepository;
import de.gabriel.vertretungsplan.repository.SubjectRepository;
import de.gabriel.vertretungsplan.repository.TeacherRepository;
import de.gabriel.vertretungsplan.service.interfaces.ConverterDTO;
import de.gabriel.vertretungsplan.utils.logging.dto.DtoConversion;
import de.gabriel.vertretungsplan.utils.logging.dto.LogDtoConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ConverterDTOImpl implements ConverterDTO {

    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    @Override
    @LogDtoConversion(message = DtoConversion.ENTITY_TO_DTO, dto = SubstitutionPlanEntryResponse.class, entity = SubstitutionPlanEntry.class)
    public SubstitutionPlanEntryResponse convertSubstitutionPlanEntryToDto(SubstitutionPlanEntry substitutionPlanEntry) {
        if (substitutionPlanEntry.getSubstitute() == null) {
            return new SubstitutionPlanEntryResponse(
                    substitutionPlanEntry.getId(),
                    substitutionPlanEntry.getCourse().getName(),
                    substitutionPlanEntry.getHour(),
                    substitutionPlanEntry.getArt(),
                    substitutionPlanEntry.getTyp(),
                    substitutionPlanEntry.getTeacher().getInitials(),
                    substitutionPlanEntry.getSubject().getName()
            );
        } else {
            return new SubstitutionPlanEntryResponse(
                    substitutionPlanEntry.getId(),
                    substitutionPlanEntry.getCourse().getName(),
                    substitutionPlanEntry.getHour(),
                    substitutionPlanEntry.getArt(),
                    substitutionPlanEntry.getTyp(),
                    substitutionPlanEntry.getTeacher().getInitials(),
                    substitutionPlanEntry.getSubstitute().getInitials(),
                    substitutionPlanEntry.getSubject().getName()
            );
        }
    }

    @Override
    @LogDtoConversion(message = DtoConversion.DTO_TO_ENTITY, dto = SubstitutionPlanEntryResponse.class, entity = SubstitutionPlanEntry.class)
    public SubstitutionPlanEntry convertSubstitutionPlanEntryDtoToEntity(CreateSubstitutionPlanEntryRequest createSubstitutionPlanEntryRequest) {
        Course course = courseRepository.findById(createSubstitutionPlanEntryRequest.getCourseId()).orElse(null);
        Teacher teacher = teacherRepository.findById(createSubstitutionPlanEntryRequest.getTeacherId()).orElse(null);
        Subject subject = subjectRepository.findById(createSubstitutionPlanEntryRequest.getSubjectId()).orElse(null);

        if (course != null && teacher != null && subject != null) {
            if (teacherRepository.findById(createSubstitutionPlanEntryRequest.getSubstituteId()).isEmpty()) {
                return new SubstitutionPlanEntry(
                        course,
                        createSubstitutionPlanEntryRequest.getHour(),
                        createSubstitutionPlanEntryRequest.getArt(),
                        teacher,
                        subject,
                        createSubstitutionPlanEntryRequest.getTyp()
                );
            } else {
                Teacher substitute = teacherRepository.findById(createSubstitutionPlanEntryRequest.getSubstituteId()).orElse(null);
                if (substitute != null) {
                    return new SubstitutionPlanEntry(
                            course,
                            createSubstitutionPlanEntryRequest.getHour(),
                            createSubstitutionPlanEntryRequest.getArt(),
                            teacher,
                            substitute,
                            subject,
                            createSubstitutionPlanEntryRequest.getTyp()
                    );
                }
            }
        }
        return null;
    }

    @Override
    @LogDtoConversion(message = DtoConversion.ENTITY_TO_DTO, dto = ClassResponse.class, entity = Course.class)
    public ClassResponse convertCourseToDto(Course course) {
        Set<String> student = course.getStudents().stream().map(Student::getUsername).limit(5).collect(Collectors.toCollection(HashSet::new));
        return new ClassResponse(course.getName(), student, course.getStudents().size());
    }

    @Override
    @LogDtoConversion(message = DtoConversion.ENTITY_TO_DTO, dto = TimetableEntryResponse.class, entity = TimetableEntry.class)
    public TimetableEntryResponse convertTimetableEntryToDto(TimetableEntry timetableEntry) {
        return new TimetableEntryResponse(
                timetableEntry.getHour(),
                timetableEntry.getCourse().getName(),
                timetableEntry.getSubject().getAbbreviation(),
                timetableEntry.getTeacher().getInitials(),
                timetableEntry.getDayEntity().getDayName().name()
        );
    }

    @Override
    @LogDtoConversion(message = DtoConversion.ENTITY_TO_DTO, dto = StudentResponse.class, entity = Student.class)
    public StudentResponse convertStudentToDto(Student student) {
        return new StudentResponse(student.getUsername(), student.getEmail(), student.getCourse().getName(), student.getRole());
    }

    @Override
    @LogDtoConversion(message = DtoConversion.ENTITY_TO_DTO, dto = TeacherResponse.class, entity = Teacher.class)
    public TeacherResponse convertTeacherToDto(Teacher teacher) {
        return new TeacherResponse(
                teacher.getUsername(),
                teacher.getInitials(),
                teacher.getEmail(),
                teacher.getRole(),
                teacher.getAttendance().toString(),
                teacher.getSubjects().stream().map(Subject::getName).collect(Collectors.toList())
        );
    }

    @Override
    @LogDtoConversion(message = DtoConversion.ENTITY_TO_DTO, dto = AdministrationResponse.class, entity = Administrator.class)
    public AdministrationResponse convertAdministratorToDto(Administrator administrator) {
        return new AdministrationResponse(administrator.getUsername(), administrator.getEmail(), administrator.getRole());
    }

    @Override
    public EntityIdMaps initEntitiesDto() {
        EntityIdMaps entityIdMaps = new EntityIdMaps();

        HashMap<String, Integer> subjectMap = new HashMap<>();
        for (Subject subject : subjectRepository.findAll()) {
            subjectMap.put(subject.getName(), subject.getId());
        }
        entityIdMaps.setSubjects(subjectMap);


        HashMap<String, Integer> courseMap = new HashMap<>();
        for (Course course : courseRepository.findAll()) {
            courseMap.put(course.getName(), course.getId());
        }
        entityIdMaps.setCourses(courseMap);

        HashMap<String, Integer> teacherMap = new HashMap<>();
        for (Teacher teacher_ : teacherRepository.findAll()) {
            teacherMap.put(teacher_.getInitials(), teacher_.getId());
        }
        entityIdMaps.setTeachers(teacherMap);

        return entityIdMaps;
    }
}
