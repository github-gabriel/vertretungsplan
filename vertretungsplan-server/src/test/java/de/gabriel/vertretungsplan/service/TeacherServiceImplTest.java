package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.*;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Art;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Day;
import de.gabriel.vertretungsplan.model.enums.Typ;
import de.gabriel.vertretungsplan.repository.SubstitutionPlanEntryRepository;
import de.gabriel.vertretungsplan.repository.TeacherRepository;
import de.gabriel.vertretungsplan.repository.TimetableEntryRepository;
import de.gabriel.vertretungsplan.service.interfaces.DayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

    @Mock
    private DayService tagService;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private TimetableEntryRepository timetableEntryRepository;
    @Mock
    private SubstitutionPlanEntryRepository substitutionPlanEntryRepository;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private TimetableEntry timetableEntryKL_1, timetableEntryKL_2, timetableEntryKL_3, timetableEntryKL_4,
            timetableEntryKL_5, timetableEntryKL_6, timetableEntryKL1_1;

    private DayEntity montag;

    private Course kl;

    private Teacher lde, lma, lla, len, lre;

    private Subject de, ma;

    @BeforeEach
    void init() {
        montag = new DayEntity(Day.MONDAY);

        de = new Subject("Deutsch", "DE");
        ma = new Subject("Mathe", "MA");
        Subject en = new Subject("Englisch", "EN");
        Subject la = new Subject("Latein", "LA");
        Subject re = new Subject("Religion", "RE");

        lde = new Teacher("german teacher", "LDE", "germanTeacher@gmail.com", "germanTeacher", Attendance.ABWESEND);
        lde.setSubjects(List.of(de, la));
        lma = new Teacher("math teacher", "LMA", "mathteacher@gmail.com", "mathteacher", Attendance.ABWESEND);
        lma.setSubjects(List.of(ma, en));
        lla = new Teacher("latin teacher", "LLA", "latinteacher@gmail.com", "latinteacher", Attendance.ANWESEND);
        lla.setSubjects(List.of(la, de));
        len = new Teacher("english teacher", "LEN", "englishteacher@gmail.com", "englishteacher", Attendance.ANWESEND);
        len.setSubjects(List.of(en, de));
        lre = new Teacher("religion teacher", "LRE", "religionteacher@gmail.com", "religionteacher", Attendance.ANWESEND);
        lre.setSubjects(List.of(re, de));

        kl = new Course("KL");
        Course kl1 = new Course("KL1");

        timetableEntryKL_1 = new TimetableEntry(1, kl, de, lde, montag);
        timetableEntryKL_2 = new TimetableEntry(2, kl, de, lde, montag);
        timetableEntryKL_3 = new TimetableEntry(3, kl, ma, lma, montag);
        timetableEntryKL_4 = new TimetableEntry(4, kl, en, len, montag);
        timetableEntryKL_5 = new TimetableEntry(5, kl, la, lla, montag);
        timetableEntryKL_6 = new TimetableEntry(6, kl, la, lla, montag);

        // Stundenplan Eintraege KL1

        timetableEntryKL1_1 = new TimetableEntry(1, kl1, re, lre, montag);
    }

    @Test
    @DisplayName("Retrieve Total Work Hours for a Teacher")
    public void getTotalWorkHours_shouldReturnTotalWorkHours() {
        List<TimetableEntry> timetableEntries = mock(ArrayList.class);
        List<SubstitutionPlanEntry> substitutionPlanEntries = mock(ArrayList.class);

        when(timetableEntries.size()).thenReturn(24);
        when(substitutionPlanEntries.size()).thenReturn(6);

        when(timetableEntryRepository.findByTeacher(len)).thenReturn(timetableEntries);
        when(substitutionPlanEntryRepository.findByTeacher(len)).thenReturn(substitutionPlanEntries);

        teacherService.getSubstitutionPlanEntryForTimetableEntry(lde, timetableEntryKL_1, len); // Funktionalität des ArbeitsstundenCaches testen (+1 Stunde für Vertreter)

        assertEquals(31, teacherService.getTotalWorkHours(len));
    }

    @Test
    @DisplayName("Determine Optimal Substitute Teacher for a Given Teacher and Timetable Entry")
    public void getBestSubstitute_shouldReturnSubstitutionTeacher() {
        // StundenplanEintraege der möglichen Vertretungen
        when(timetableEntryRepository.findByTeacherAndDayEntity(lla, montag)).thenReturn(List.of(timetableEntryKL_5, timetableEntryKL_6));
        when(timetableEntryRepository.findByTeacher(lla)).thenReturn(List.of(timetableEntryKL_5, timetableEntryKL_6));
        when(timetableEntryRepository.findByTeacherAndDayEntity(len, montag)).thenReturn(List.of(timetableEntryKL_4));
        when(timetableEntryRepository.findByTeacher(len)).thenReturn(List.of(timetableEntryKL_4));
        when(timetableEntryRepository.findByTeacherAndDayEntity(lre, montag)).thenReturn(List.of(timetableEntryKL1_1));

        Teacher vertreter = teacherService.getBestSubstitute(List.of(lla, len, lre), timetableEntryKL_1);

        assertEquals(len.hashCode(), vertreter.hashCode()); // Vertreter mit wenigsten Stunden (am besten geeignet)
    }

    @Test
    @DisplayName("Generate Substitution Plan Entries for Every Hour of Each Absent Teacher")
    public void getSubstitutionPlanEntries_shouldReturnSubstitutionPlanEntries() {
        when(tagService.getCurrentDay()).thenReturn(montag);
        when(teacherRepository.findByAttendance(Attendance.ANWESEND)).thenReturn(List.of(len, lre, lla));

        when(timetableEntryRepository.findByTeacherAndDayEntity(len, montag)).thenReturn(List.of(timetableEntryKL_4));
        when(timetableEntryRepository.findByTeacherAndDayEntity(lre, montag)).thenReturn(List.of(timetableEntryKL1_1));
        when(timetableEntryRepository.findByTeacherAndDayEntity(lla, montag)).thenReturn(List.of(timetableEntryKL_5, timetableEntryKL_6));

        when(timetableEntryRepository.findByTeacherAndDayEntity(lde, montag)).thenReturn(List.of(timetableEntryKL_1, timetableEntryKL_2));
        when(timetableEntryRepository.findByTeacherAndDayEntity(lma, montag)).thenReturn(List.of(timetableEntryKL_3));

        List<SubstitutionPlanEntry> substitutionPlanEntries = teacherService.getSubstitutionPlanEntries(List.of(lde, lma));

        assertEquals(List.of(
                new SubstitutionPlanEntry(kl, 3, Art.ENTFALL, lma, ma, Typ.AUTO),
                new SubstitutionPlanEntry(kl, 1, Art.VERTRETUNG, len, de, Typ.AUTO),
                new SubstitutionPlanEntry(kl, 2, Art.VERTRETUNG, lre, de, Typ.AUTO)
        ), substitutionPlanEntries);
    }

    @Test
    @DisplayName("Retrieve Substitution Plan Entry for a Specific Timetable Entry")
    public void getSubstitutionPlanEntryForTimetableEntry_shouldReturnSubstitutionPlanEntriesForGivenTimetableEntry() {
        SubstitutionPlanEntry substitutionPlanEntry = teacherService.getSubstitutionPlanEntryForTimetableEntry(lde, timetableEntryKL_1, len);

        assertEquals(new SubstitutionPlanEntry(kl, 1, Art.VERTRETUNG, lde, len, de, Typ.AUTO), substitutionPlanEntry);
    }

}
