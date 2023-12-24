package de.gabriel.vertretungsplan.controller;

import de.gabriel.vertretungsplan.model.dto.*;
import de.gabriel.vertretungsplan.model.enums.Typ;
import de.gabriel.vertretungsplan.service.interfaces.ResourceService;
import de.gabriel.vertretungsplan.utils.logging.request.LogRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.gabriel.vertretungsplan.config.ApplicationConfig.*;

@Tag(name = "Resource Controller", description = "Provides endpoints for fetching and modifying resources")
@RequiredArgsConstructor
@RestController
@RequestMapping(API_BASE + "/resources")
@Slf4j
public class ResourceController {

    private final ResourceService resourceService;

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SubstitutionPlanEntryResponse.class)))
    })
    @Operation(summary = "Fetches all substitution plan entries")
    @LogRequest(message = "Fetch substitution plan entries with optional 'typ' parameter")
    @GetMapping({"/substitution-plan/{typ}", "/substitution-plan"})
    public List<SubstitutionPlanEntryResponse> getSubstitutionPlan(@PathVariable(required = false) String typ, HttpServletRequest request) {
        if (typ != null) {
            return resourceService.getSubstitutionPlan(Typ.getTypFromString(typ));
        }
        return resourceService.getSubstitutionPlan();
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = SubstitutionPlanEntryResponse.class)))
    })
    @Operation(summary = "Fetches all substitution plan entries for a teacher")
    @LogRequest(message = "Fetch substitution plan entries for a teacher")
    @GetMapping(TEACHER_ENDPOINT + "/substitution-plan")
    public List<SubstitutionPlanEntryResponse> getSubstitutionPlanEntriesForTeacher(@CookieValue("jwt") String jwt, HttpServletRequest request) {
        return resourceService.getSubstitutionPlanEntriesForTeacher(jwt);
    }

    @Operation(summary = "Deletes a substitution plan entry")
    @LogRequest(message = "Delete a substitution plan entry with the specified ID")
    @DeleteMapping(ADMINISTRATOR_ENDPOINT + "/substitution-plan/{id}")
    public void deleteSubstitutionPlanEntry(@PathVariable Integer id, HttpServletRequest request) {
        resourceService.deleteSubstitutionPlanEntry(id);
    }

    @Operation(summary = "Creates a new substitution plan entry")
    @LogRequest(message = "Create a new substitution plan entry based on the provided JSON request, mapping from DTO to Entity")
    @PostMapping(value = ADMINISTRATOR_ENDPOINT + "/substitution-plan", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateSubstitutionPlanEntryResponse createSubstitutionPlanEntry(@RequestBody CreateSubstitutionPlanEntryRequest createSubstitutionPlanEntryRequest, HttpServletRequest request) {
        return resourceService.createSubstitutionPlanEntry(createSubstitutionPlanEntryRequest);
    }

    @Operation(summary = "Sets the attendance status of a teacher")
    @LogRequest(message = "Set the attendance status of a teacher based on the provided JSON request")
    @PostMapping(value = TEACHER_ENDPOINT + "/attendance-status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setTeacherAttendanceStatus(@CookieValue("jwt") String jwt, @RequestBody SetTeacherAttendanceRequest setTeacherAttendanceRequest, HttpServletRequest request) {
        resourceService.setTeacherAttendanceStatus(jwt, setTeacherAttendanceRequest);
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AttendanceStatus.class))
    })
    @Operation(summary = "Fetches the attendance status of a teacher")
    @LogRequest(message = "Retrieve the attendance status of a teacher")
    @GetMapping(value = TEACHER_ENDPOINT + "/attendance-status")
    public AttendanceStatus getTeacherAttendanceStatus(@CookieValue("jwt") String jwt, HttpServletRequest request) {
        return resourceService.getTeacherAttendanceStatus(jwt);
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TeacherResponse.class)))
    })
    @Operation(summary = "Fetches all teachers")
    @LogRequest(message = "Retrieve information for all teachers as a collection of DTOs")
    @GetMapping(ADMINISTRATOR_ENDPOINT + "/teachers")
    public List<TeacherResponse> getAllTeachers(HttpServletRequest request) {
        return resourceService.getAllTeachers();
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StudentResponse.class)))
    })
    @Operation(summary = "Fetches all students")
    @LogRequest(message = "Retrieve information for all students as a collection of DTOs")
    @GetMapping(ADMINISTRATOR_ENDPOINT + "/students")
    public List<StudentResponse> getAllStudents(HttpServletRequest request) {
        return resourceService.getAllStudents();
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AdministrationResponse.class)))
    })
    @Operation(summary = "Fetches all administrators")
    @LogRequest(message = "Retrieve information for all administrators as a collection of DTOs")
    @GetMapping(ADMINISTRATOR_ENDPOINT + "/administrators")
    public List<AdministrationResponse> getAllAdministrators(HttpServletRequest request) {
        return resourceService.getAllAdministrators();
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TimetableEntryResponse.class)))
    })
    @Operation(summary = "Fetches all timetable entries for a teacher")
    @LogRequest(message = "Retrieve information for all timetable entries for a teacher as a collection of DTOs")
    @GetMapping(TEACHER_ENDPOINT + "/timetable-entries")
    public List<TimetableEntryResponse> getTimetableEntriesByTeacher(@CookieValue("jwt") String jwt, HttpServletRequest request) {
        return resourceService.getTimetableEntriesByTeacher(jwt);
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TimetableEntryResponse.class)))
    })
    @Operation(summary = "Fetches all timetable entries for a class")
    @LogRequest(message = "Retrieve information for all timetable entries for a class as a collection of DTOs")
    @GetMapping(ADMINISTRATOR_ENDPOINT + "/timetable-entries/{course}")
    public List<TimetableEntryResponse> getTimetableEntriesByCourse(@PathVariable String course, HttpServletRequest request) {
        return resourceService.getTimetableEntriesByCourse(course);
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ClassResponse.class)))
    })
    @Operation(summary = "Fetches all classes")
    @LogRequest(message = "Retrieve information for all classes as a collection of DTOs")
    @GetMapping(ADMINISTRATOR_ENDPOINT + "/courses")
    public List<ClassResponse> getAllCourses(HttpServletRequest request) {
        return resourceService.getAllCourses();
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRole.class))
    })
    @Operation(summary = "Fetches current user's role")
    @LogRequest(message = "Retrieves information for the currently logged-in user as a DTO")
    @GetMapping(USER_ENDPOINT + "/role")
    public UserRole getCurrentUserRole(@CookieValue("jwt") String jwt, HttpServletRequest request) {
        return resourceService.getCurrentUserRole(jwt);
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CurrentDay.class))
    })
    @Operation(summary = "Fetches the current day")
    @LogRequest(message = "Retrieve information about the current day as a DTO")
    @GetMapping("/current-day")
    public CurrentDay getCurrentDay(HttpServletRequest request) {
        return resourceService.getCurrentDay();
    }

    @ApiResponse(responseCode = "200", content = {
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EntityIdMaps.class))
    })
    @Operation(summary = "Fetches DTOs with according IDs certain entities")
    @LogRequest(message = "Retrieve entities with their IDs and return them as an EntitiesDTO")
    @GetMapping(ADMINISTRATOR_ENDPOINT + "/entities")
    public EntityIdMaps getEntitiesWithAccordingIds(HttpServletRequest request) {
        return resourceService.getEntitiesWithAccordingIds();
    }

}
