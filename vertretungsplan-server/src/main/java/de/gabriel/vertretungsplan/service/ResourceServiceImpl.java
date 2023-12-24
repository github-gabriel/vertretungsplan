package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.exception.exceptions.authentication.InvalidJwtException;
import de.gabriel.vertretungsplan.exception.exceptions.data.ResourceNotFoundException;
import de.gabriel.vertretungsplan.exception.exceptions.data.UserNotFoundException;
import de.gabriel.vertretungsplan.model.dto.*;
import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.entities.users.User;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Role;
import de.gabriel.vertretungsplan.model.enums.Typ;
import de.gabriel.vertretungsplan.repository.*;
import de.gabriel.vertretungsplan.service.interfaces.ConverterDTO;
import de.gabriel.vertretungsplan.service.interfaces.DayService;
import de.gabriel.vertretungsplan.service.interfaces.ResourceService;
import de.gabriel.vertretungsplan.service.interfaces.SubstitutionPlanEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final CourseRepository courseRepository;
    private final TimetableEntryRepository timetableEntryRepository;
    private final ConverterDTO dtoConverter;
    private final SubstitutionPlanEntryService substitutionPlanEntryService;
    private final DayService tagService;
    private final JwtService jwtService;
    private final SubstitutionPlanEntryRepository substitutionPlanEntryRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdministratorRepository administratorRepository;
    private final UserRepository userRepository;

    @Override
    public List<ClassResponse> getAllCourses() {
        List<ClassResponse> classResponse = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            classResponse.add(dtoConverter.convertCourseToDto(course));
        }
        return classResponse;
    }

    @Override
    public List<TimetableEntryResponse> getTimetableEntriesByTeacher(String jwt) {
        List<TimetableEntryResponse> timetableEntryResponse = new ArrayList<>();
        if (jwt != null) {
            String username = jwtService.extractUsername(jwt);
            Teacher teacher = teacherRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("The teacher was not found!"));
            for (TimetableEntry timetableEntry : timetableEntryRepository.findByTeacher(teacher)) {
                timetableEntryResponse.add(dtoConverter.convertTimetableEntryToDto(timetableEntry));
            }
            return timetableEntryResponse;
        } else {
            throw new InvalidJwtException("The JWT Token is empty!");
        }
    }

    @Override
    public List<TimetableEntryResponse> getTimetableEntriesByCourse(String course) {
        List<TimetableEntryResponse> timetableEntryResponse = new ArrayList<>();
        Course courseEntity = courseRepository.findByName(course);
        if (courseEntity == null) {
            throw new ResourceNotFoundException("The class was not found!");
        }
        for (TimetableEntry timetableEntry : timetableEntryRepository.findByCourse(courseEntity)) {
            timetableEntryResponse.add(dtoConverter.convertTimetableEntryToDto(timetableEntry));
        }
        return timetableEntryResponse;
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        List<StudentResponse> studentResponse = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            studentResponse.add(dtoConverter.convertStudentToDto(student));
        }
        return studentResponse;
    }

    @Override
    public List<TeacherResponse> getAllTeachers() {
        List<TeacherResponse> teacherResponse = new ArrayList<>();
        for (Teacher teacher : teacherRepository.findAll()) {
            teacherResponse.add(dtoConverter.convertTeacherToDto(teacher));
        }
        return teacherResponse;
    }

    @Override
    public AttendanceStatus getTeacherAttendanceStatus(String jwt) {
        String attendance = teacherRepository.findByEmail(jwtService.extractUsername(jwt)).orElseThrow(() -> new UserNotFoundException("The teacher was not found!")).getAttendance().toString();
        return new AttendanceStatus(attendance);
    }

    @Override
    public void setTeacherAttendanceStatus(String jwt, SetTeacherAttendanceRequest setTeacherAttendanceRequest) {
        if (jwt != null && setTeacherAttendanceRequest.getAttendance() != null && setTeacherAttendanceRequest.getAttendance().equalsIgnoreCase(Attendance.ABWESEND.toString())
                || setTeacherAttendanceRequest.getAttendance() != null && setTeacherAttendanceRequest.getAttendance().equalsIgnoreCase(Attendance.ANWESEND.toString())) {
            String username = jwtService.extractUsername(jwt);
            Teacher teacher = teacherRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("The teacher was not found!"));
            teacher.setAttendance(Attendance.getAttendanceFromString(setTeacherAttendanceRequest.getAttendance()));
            teacherRepository.save(teacher);
        } else if (jwt == null) {
            throw new InvalidJwtException("The JWT Token is empty!");
        } else {
            throw new ResourceNotFoundException("The attendance status you provided was not found!");
        }
    }

    @Override
    public List<AdministrationResponse> getAllAdministrators() {
        List<AdministrationResponse> administrationResponse = new ArrayList<>();
        for (Administrator administrator : administratorRepository.findAll()) {
            administrationResponse.add(dtoConverter.convertAdministratorToDto(administrator));
        }
        return administrationResponse;
    }

    @Override
    public List<SubstitutionPlanEntryResponse> getSubstitutionPlan(Typ typ) {
        List<SubstitutionPlanEntryResponse> substitutionPlanEntriesResponse = new ArrayList<>();
        for (SubstitutionPlanEntry substitutionPlanEntry : substitutionPlanEntryService.getSubstitutionPlanEntries()) {
            if (substitutionPlanEntry.getTyp().equals(typ)) {
                substitutionPlanEntriesResponse.add(dtoConverter.convertSubstitutionPlanEntryToDto(substitutionPlanEntry));
            }
        }
        return substitutionPlanEntriesResponse;
    }

    @Override
    public List<SubstitutionPlanEntryResponse> getSubstitutionPlan() {
        List<SubstitutionPlanEntryResponse> substitutionPlanEntriesResponse = new ArrayList<>();
        for (SubstitutionPlanEntry substitutionPlanEntry : substitutionPlanEntryService.getSubstitutionPlanEntries()) {
            substitutionPlanEntriesResponse.add(dtoConverter.convertSubstitutionPlanEntryToDto(substitutionPlanEntry));
        }
        return substitutionPlanEntriesResponse;
    }


    @Override
    public List<SubstitutionPlanEntryResponse> getSubstitutionPlanEntriesForTeacher(String jwt) {
        List<SubstitutionPlanEntryResponse> substitutionPlanEntryResponse = new ArrayList<>();
        if (jwt != null) {
            String username = jwtService.extractUsername(jwt);
            Teacher teacher = teacherRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("The teacher was not found!"));
            for (SubstitutionPlanEntry substitutionPlanEntry : substitutionPlanEntryRepository.findBySubstitute(teacher)) {
                substitutionPlanEntryResponse.add(dtoConverter.convertSubstitutionPlanEntryToDto(substitutionPlanEntry));
            }
            return substitutionPlanEntryResponse;
        } else {
            throw new InvalidJwtException("The JWT Token is empty!");
        }
    }

    @Override
    public CreateSubstitutionPlanEntryResponse createSubstitutionPlanEntry(CreateSubstitutionPlanEntryRequest request) {
        SubstitutionPlanEntry substitutionPlanEntry = dtoConverter.convertSubstitutionPlanEntryDtoToEntity(request);
        substitutionPlanEntryRepository.save(substitutionPlanEntry);
        return CreateSubstitutionPlanEntryResponse.builder().id(substitutionPlanEntry.getId()).build();
    }

    @Override
    public void deleteSubstitutionPlanEntry(Integer id) {
        if (!substitutionPlanEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("The substitution plan entry was not found!");
        }
        substitutionPlanEntryRepository.deleteById(id);
    }

    @Override
    public UserRole getCurrentUserRole(String jwt) {
        if (jwt != null) {
            String username = jwtService.extractUsername(jwt);
            User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("The user was not found!"));
            return UserRole.builder().role(Role.getRoleFromStringWithPrefix(user.getRole())).build();
        } else {
            throw new InvalidJwtException("The JWT Token is empty!");
        }
    }

    @Override
    public CurrentDay getCurrentDay() {
        DayEntity dayEntity = tagService.getCurrentDay();
        LocalDateTime now = LocalDateTime.now();
        return CurrentDay.builder().currentDay(dayEntity.getDayName()).currentDateTime(now).build();
    }

    @Override
    public EntityIdMaps getEntitiesWithAccordingIds() {
        return dtoConverter.initEntitiesDto();
    }

}
