package de.gabriel.vertretungsplan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gabriel.vertretungsplan.model.dto.*;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.*;
import de.gabriel.vertretungsplan.repository.TeacherRepository;
import de.gabriel.vertretungsplan.security.UserDetailsImpl;
import de.gabriel.vertretungsplan.service.JwtService;
import de.gabriel.vertretungsplan.service.ResourceServiceImpl;
import de.gabriel.vertretungsplan.service.interfaces.ResourceService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static de.gabriel.vertretungsplan.config.ApplicationConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ResourceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {ResourceServiceImpl.class, ResourceController.class})
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ResourceService resourceService;
    @Autowired
    private ObjectMapper objectMapper;

    private static JwtService jwtService;

    private static Teacher teacher;
    private static TeacherResponse teacherResponse;
    private static StudentResponse studentResponse;
    private static AdministrationResponse administrationResponse;
    private static TimetableEntryResponse timetableEntryResponse;
    private static ClassResponse classResponse;
    private static SubstitutionPlanEntryResponse substitutionPlanEntryResponse;

    @MockBean
    private TeacherRepository teacherRepository;

    @BeforeAll
    static void init() {
        jwtService = new JwtService();

        teacher = new Teacher(
                "teacher",
                "L",
                "teacher@gmail.com",
                "password",
                Attendance.ANWESEND
        );
        teacherResponse = new TeacherResponse(
                teacher.getUsername(),
                teacher.getInitials(),
                teacher.getEmail(),
                teacher.getRole(),
                teacher.getAttendance().toString(),
                null

        );
        studentResponse = new StudentResponse(
                "student",
                "student@gmail.com",
                "6a",
                Role.getRoleWithPrefix(Role.STUDENT)
        );
        administrationResponse = new AdministrationResponse(
                "administrator",
                "administrator@gmail.com",
                Role.getRoleWithPrefix(Role.ADMINISTRATOR)
        );
        timetableEntryResponse = new TimetableEntryResponse(
                1,
                "6a",
                "DE",
                "teacher",
                Day.MONDAY.toString()
        );
        classResponse = new ClassResponse(
                "6a",
                Set.of(studentResponse.getName()),
                0
        );
        substitutionPlanEntryResponse = new SubstitutionPlanEntryResponse(
                1,
                "6a",
                6,
                Art.ENTFALL,
                Typ.AUTO,
                "teacher",
                "DE"
        );
    }

    @Test
    @DisplayName("Retrieve all substitution plan entries as DTOs with optional 'typ' parameter")
    public void getSubstitutionPlan_shouldReturnAllSubstitutionPlanEntriesByOptionalType() throws Exception {
        when(resourceService.getSubstitutionPlan(Typ.AUTO)).thenReturn(List.of(substitutionPlanEntryResponse));

        mockMvc.perform(get(API_BASE + "/resources/substitution-plan/AUTO"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(substitutionPlanEntryResponse))))
                .andReturn();

        verify(resourceService, times(1)).getSubstitutionPlan(Typ.AUTO);
    }

    @Test
    @DisplayName("Retrieve all Substitution Plan Entries as DTOs")
    public void getSubstitutionPlan_shouldReturnAllSubstitutionPlanEntries() throws Exception {
        when(resourceService.getSubstitutionPlan()).thenReturn(List.of(substitutionPlanEntryResponse));

        mockMvc.perform(get(API_BASE + "/resources/substitution-plan"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(substitutionPlanEntryResponse))))
                .andReturn();

        verify(resourceService, times(1)).getSubstitutionPlan();
    }

    @Test
    @DisplayName("Retrieve all Substitution Plan Entries as DTOs for a Teacher")
    public void getSubstitutionPlanEntriesForTeacher_shouldReturnAllSubstitutionPlanEntriesForTeacher() throws Exception {
        String token = jwtService.generateToken(new UserDetailsImpl(
                teacher
        ));

        when(resourceService.getSubstitutionPlanEntriesForTeacher(token)).thenReturn(List.of(substitutionPlanEntryResponse));

        mockMvc.perform(get(API_BASE + "/resources" + TEACHER_ENDPOINT + "/substitution-plan")
                        .cookie(new Cookie("jwt", token)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(substitutionPlanEntryResponse))))
                .andReturn();

        verify(resourceService, times(1)).getSubstitutionPlanEntriesForTeacher(token);
    }

    @Test
    @DisplayName("Delete a substitution plan entry with the specified ID")
    public void deleteSubstitutionPlanEntry_shouldDeleteSubstitutionPlanWithSpecifiedId() throws Exception {
        mockMvc.perform(delete(API_BASE + "/resources" + ADMINISTRATOR_ENDPOINT + "/substitution-plan/1"))
                .andExpect(status().isOk())
                .andReturn();

        verify(resourceService, times(1)).deleteSubstitutionPlanEntry(1);
    }

    @Test
    @DisplayName("Create a new Substitution Plan Entry from Request")
    public void createSubstitutionPlanEntry_shouldCreateNewSubstitutionPlanEntryFromRequest() throws Exception {
        ArgumentCaptor<CreateSubstitutionPlanEntryRequest> captor =
                ArgumentCaptor.forClass(CreateSubstitutionPlanEntryRequest.class);

        CreateSubstitutionPlanEntryRequest createSubstitutionPlanEntryRequest = new CreateSubstitutionPlanEntryRequest(
                1,
                1,
                Art.ENTFALL,
                Typ.AUTO,
                1,
                1
        );

        mockMvc.perform(post(API_BASE + "/resources" + ADMINISTRATOR_ENDPOINT + "/substitution-plan")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createSubstitutionPlanEntryRequest)))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).createSubstitutionPlanEntry(captor.capture());
        assertEquals(createSubstitutionPlanEntryRequest, captor.getValue());
    }

    @Test
    @DisplayName("Set the Attendance Status of a Teacher")
    public void setTeacherAttendanceStatus_shouldSetAttendanceForTeacher() throws Exception {
        ArgumentCaptor<SetTeacherAttendanceRequest> captor =
                ArgumentCaptor.forClass(SetTeacherAttendanceRequest.class);

        when(teacherRepository.findByEmail("teacher@gmail.com")).thenReturn(Optional.of(teacher));

        SetTeacherAttendanceRequest setTeacherAttendanceRequest = new SetTeacherAttendanceRequest(
                Attendance.ABWESEND.toString()
        );

        mockMvc.perform(post(API_BASE + "/resources" + TEACHER_ENDPOINT + "/attendance-status")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(setTeacherAttendanceRequest))
                        .cookie(new Cookie("jwt", jwtService.generateToken(new UserDetailsImpl(
                                teacher
                        )))))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).setTeacherAttendanceStatus(anyString(), captor.capture());
        assertEquals(setTeacherAttendanceRequest, captor.getValue());
    }

    @Test
    @DisplayName("Retrieve Attendance Status of a Teacher")
    public void getTeacherAnwesenheit_shouldReturnTeachersAttendanceStatus() throws Exception {
        when(resourceService.getTeacherAttendanceStatus(jwtService.generateToken(new UserDetailsImpl(
                teacher
        )))).thenReturn(new AttendanceStatus(teacher.getAttendance().toString()));

        mockMvc.perform(get(API_BASE + "/resources" + TEACHER_ENDPOINT + "/attendance-status")
                        .cookie(new Cookie("jwt", jwtService.generateToken(new UserDetailsImpl(
                                teacher
                        )))))
                .andExpect(content().string(
                        objectMapper.writeValueAsString(new AttendanceStatus(teacher.getAttendance().toString()))
                ))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getTeacherAttendanceStatus(any(String.class));
    }

    @Test
    @DisplayName("Retrieve all Teachers as DTOs")
    public void getAllTeachers_shouldReturnAllTeachers() throws Exception {
        when(resourceService.getAllTeachers()).thenReturn(List.of(teacherResponse));

        mockMvc.perform(get(API_BASE + "/resources" + ADMINISTRATOR_ENDPOINT + "/teachers"))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(teacherResponse))))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getAllTeachers();
    }

    @Test
    @DisplayName("Retrieve all Students as DTOs")
    public void getAllStudents_shouldReturnAllStudents() throws Exception {
        when(resourceService.getAllStudents()).thenReturn(List.of(studentResponse));

        mockMvc.perform(get(API_BASE + "/resources" + ADMINISTRATOR_ENDPOINT + "/students"))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(studentResponse))))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("Retrieve all Administrators as DTOs")
    public void getAllAdministrators_shouldReturnAllAdministrators() throws Exception {
        when(resourceService.getAllAdministrators()).thenReturn(List.of(administrationResponse));

        mockMvc.perform(get(API_BASE + "/resources" + ADMINISTRATOR_ENDPOINT + "/administrators"))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(administrationResponse))))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getAllAdministrators();
    }

    @Test
    @DisplayName("Retrieve all Timetable Entries as DTOs for a Teacher")
    public void getTimetableEntriesByTeacher_shouldReturnAllTimetableEntriesForTeacher() throws Exception {
        String token = jwtService.generateToken(new UserDetailsImpl(teacher));

        when(resourceService.getTimetableEntriesByTeacher(token)).thenReturn(List.of(timetableEntryResponse));

        mockMvc.perform(get(API_BASE + "/resources" + TEACHER_ENDPOINT + "/timetable-entries")
                        .cookie(new Cookie("jwt", token
                        )))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(timetableEntryResponse))))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getTimetableEntriesByTeacher(token);
    }

    @Test
    @DisplayName("Retrieve all Timetable Entries as DTOs for a Course")
    public void getTimetableEntriesByCourse_shouldReturnAllTimetableEntriesForCourse() throws Exception {
        String course = "6a";

        when(resourceService.getTimetableEntriesByCourse(course)).thenReturn(List.of(timetableEntryResponse));

        mockMvc.perform(get(API_BASE + "/resources" + ADMINISTRATOR_ENDPOINT + "/timetable-entries/" + course))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(timetableEntryResponse))))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getTimetableEntriesByCourse(course);
    }


    @Test
    @DisplayName("Retrieve all Classes as DTOs")
    public void getAllCourses_shouldReturnAllCourses() throws Exception {
        when(resourceService.getAllCourses()).thenReturn(List.of(classResponse));

        mockMvc.perform(get(API_BASE + "/resources" + ADMINISTRATOR_ENDPOINT + "/courses"))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(classResponse))))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getAllCourses();
    }

    @Test
    @DisplayName("Extract User Roles from JWT Token")
    public void user_shouldReturnCurrentUsersInfo() throws Exception {
        String token = jwtService.generateToken(new UserDetailsImpl(
                teacher
        ));
        UserRole userRole = UserRole.builder().role(Role.getRoleFromStringWithPrefix(teacher.getRole())).build();

        when(resourceService.getCurrentUserRole(anyString())).thenReturn(userRole);

        mockMvc.perform(get(API_BASE + "/resources" + USER_ENDPOINT + "/role")
                        .cookie(new Cookie("jwt", token)))
                .andExpect(content().string(objectMapper.writeValueAsString(
                        userRole
                )))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getCurrentUserRole(token);
    }

    @Test
    @DisplayName("Retrieve the Current Day")
    public void day_shouldReturnCurrentDay() throws Exception {
        CurrentDay currentDay = new CurrentDay(Day.MONDAY, LocalDateTime.now());

        when(resourceService.getCurrentDay()).thenReturn(currentDay);

        mockMvc.perform(get(API_BASE + "/resources/current-day"))
                .andExpect(content().string(objectMapper.writeValueAsString(
                        currentDay
                )))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getCurrentDay();
    }

    @Test
    @DisplayName("Retrieve List of Classes, Teachers, and Subjects")
    public void getEntitiesWithId_shouldReturnEntities() throws Exception {
        EntityIdMaps entityIdMaps = new EntityIdMaps(
                new HashMap<>(Map.of("6a", 1)),
                new HashMap<>(Map.of("teacher", 1)),
                new HashMap<>(Map.of("DE", 1))
        );

        when(resourceService.getEntitiesWithAccordingIds()).thenReturn(entityIdMaps);

        mockMvc.perform(get(API_BASE + "/resources" + ADMINISTRATOR_ENDPOINT + "/entities"))
                .andExpect(content().string(objectMapper.writeValueAsString(
                        entityIdMaps
                )))
                .andExpect(status().isOk());

        verify(resourceService, times(1)).getEntitiesWithAccordingIds();
    }

}
