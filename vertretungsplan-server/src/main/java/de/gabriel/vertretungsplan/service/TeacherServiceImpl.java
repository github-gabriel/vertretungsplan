package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.entities.Subject;
import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Art;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Day;
import de.gabriel.vertretungsplan.model.enums.Typ;
import de.gabriel.vertretungsplan.repository.SubstitutionPlanEntryRepository;
import de.gabriel.vertretungsplan.repository.TeacherRepository;
import de.gabriel.vertretungsplan.repository.TimetableEntryRepository;
import de.gabriel.vertretungsplan.service.interfaces.DayService;
import de.gabriel.vertretungsplan.service.interfaces.TeacherService;
import de.gabriel.vertretungsplan.utils.WorkingHoursCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final DayService tagService;
    private final TeacherRepository teacherRepository;
    private final TimetableEntryRepository timetableEntryRepository;
    private final SubstitutionPlanEntryRepository substitutionPlanEntryRepository;
    private final WorkingHoursCache workingHoursCache = new WorkingHoursCache();
    public final static int MAX_HOURS_PER_WEEK = 40;

    @Override
    public Integer getTotalWorkHours(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher can not be null!");
        }
        int totalHoursTimetableEntries = timetableEntryRepository.findByTeacher(teacher).size();
        int totalHoursSubstitutionPlanEntries = substitutionPlanEntryRepository.findByTeacher(teacher).size();
        int additionalWorkHoursCache = workingHoursCache.getWorkHours(teacher);

        int totalWorkHours = totalHoursTimetableEntries + totalHoursSubstitutionPlanEntries + additionalWorkHoursCache;

        log.trace("Retrieving total work hours of teacher; {[Email={}], [Hours={}]}",
                teacher.getEmail(), totalWorkHours);

        return totalWorkHours;
    }

    @Override
    public SubstitutionPlanEntry getSubstitutionPlanEntryForTimetableEntry(Teacher absentTeacher, TimetableEntry timetableEntryAbsentTeacher, Teacher substitute) {
        if (substitute != null && getTotalWorkHours(substitute) < MAX_HOURS_PER_WEEK) { // Wenn der Vertreter weniger als 40 Stunden hat
            /*
            Hinzufügen von 1 ArbeitssubstitutionPlanEntries, da der Vertreter nun 1 Stunde mehr arbeitet und dies nicht der DB entnommen werden kann,
            da die Daten erst später gespeichert werden
             */
            workingHoursCache.addWorkHours(substitute, 1);
            log.info("Creating a new substitution plan with substitute teacher; {[Substitute={}], [Teacher={}], [Type={}]}",
                    substitute.getEmail(), absentTeacher.getEmail(), Art.VERTRETUNG);
            return new SubstitutionPlanEntry(
                    timetableEntryAbsentTeacher.getCourse(),
                    timetableEntryAbsentTeacher.getHour(), Art.VERTRETUNG, absentTeacher, substitute,
                    timetableEntryAbsentTeacher.getSubject(), Typ.AUTO
            );
        } else {
            log.info("Creating a new substitution plan; {[Teacher={}], [Type={}]}",
                    absentTeacher.getEmail(), Art.ENTFALL);
            return new SubstitutionPlanEntry(
                    timetableEntryAbsentTeacher.getCourse(),
                    timetableEntryAbsentTeacher.getHour(), Art.ENTFALL, absentTeacher,
                    timetableEntryAbsentTeacher.getSubject(), Typ.AUTO
            );
        }

    }

    @Override
    public List<SubstitutionPlanEntry> getSubstitutionPlanEntries(List<Teacher> absentTeachers) {
        DayEntity dayEntity = tagService.getCurrentDay(); // Heutiger Tag
        if (!dayEntity.getDayName().equals(Day.WEEKEND)) {
            List<SubstitutionPlanEntry> substitutionPlanEntries = new ArrayList<>();
            List<Teacher> possibleSubstitutes = teacherRepository.findByAttendance(Attendance.ANWESEND);

            for (Teacher absentTeacher : absentTeachers) { // Für jeden abwesenden Teacher
                List<TimetableEntry> timetableEntriesAbsentTeachers = timetableEntryRepository.findByTeacherAndDayEntity(absentTeacher, dayEntity); // Alle StundenplanEinträge des abwesenden Teachers
                for (TimetableEntry timetableEntryAbsentTeacher : timetableEntriesAbsentTeachers) { // Für jeden StundenplanEintrag des abwesenden Teachers
                    Teacher substitute = getBestSubstitute(possibleSubstitutes, timetableEntryAbsentTeacher); // Alle möglichen Vertreter nach Stunden sortiert

                    log.trace("Retrieving substitution plan for teacher; {[Teacher={}], [Substitute={}], [TeacherSubjects={}], [SubstituteSubjects={}]}",
                            absentTeacher.getEmail(), substitute != null ? substitute.getEmail() : "", absentTeacher.getSubjects().stream().map(Subject::getName).collect(Collectors.toList()),
                            substitute != null ? substitute.getSubjects().stream().map(Subject::getName).collect(Collectors.toList()) : "");

                    substitutionPlanEntries.add(getSubstitutionPlanEntryForTimetableEntry(absentTeacher, timetableEntryAbsentTeacher, substitute));
                }
            }
            workingHoursCache.clearCache();
            return substitutionPlanEntries;
        }
        return new ArrayList<>();
    }

    @Override
    public Teacher getBestSubstitute(List<Teacher> possibleSubstitutes, TimetableEntry timetableEntryAbsentTeacher) {
        List<Teacher> substitute = new ArrayList<>();
        for (Teacher possibleSubstitute : possibleSubstitutes) {
            List<TimetableEntry> timetableEntriesPossibleSubstitute = timetableEntryRepository.findByTeacherAndDayEntity(possibleSubstitute, timetableEntryAbsentTeacher.getDayEntity());
            for (TimetableEntry timetableEntryPossibleSubstitute : timetableEntriesPossibleSubstitute) {
                if (possibleSubstitute.getSubjects().contains(timetableEntryAbsentTeacher.getSubject())
                        && timetableEntryPossibleSubstitute.getHour() != timetableEntryAbsentTeacher.getHour()) {

                    log.trace("Found a new substitute; {[Teacher={}], [Substitute={}], [Subject={}], [SubstituteSubjects={}], [Day={}], [TeacherTimetableHour={}], [SubstituteTimetableHour={}]}",
                            timetableEntryAbsentTeacher.getTeacher().getEmail(), possibleSubstitute.getEmail(), timetableEntryAbsentTeacher.getSubject().getName(),
                            possibleSubstitute.getSubjects().stream().map(Subject::getName).collect(Collectors.toList()), timetableEntryAbsentTeacher.getDayEntity().getDayName(),
                            timetableEntryAbsentTeacher.getHour(), timetableEntryPossibleSubstitute.getHour());

                    substitute.add(possibleSubstitute);
                }
            }
        }

        Map<Teacher, Integer> unsortierteVertreter = new HashMap<>();
        Map<Teacher, Integer> sortierteVertreter;

        for (Teacher substitute_ : substitute) {
            unsortierteVertreter.put(substitute_, getTotalWorkHours(substitute_));
        }

        sortierteVertreter = unsortierteVertreter.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new)); // Nach Stunden sortierte Vertreter

        log.trace("Sorted substitutes by work hours; {[Substitutes={}]}",
                sortierteVertreter.entrySet().stream()
                        .map(entry -> String.format("{Email=%s, Arbeitsstunden=%d}", entry.getKey().getEmail(), entry.getValue()))
                        .collect(Collectors.toList()));

        return sortierteVertreter.keySet().stream().findFirst().isPresent() ? sortierteVertreter.keySet().stream().findFirst().get() : null;
    }

}
