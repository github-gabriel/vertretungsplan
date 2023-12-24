package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.SubstitutionPlanEntry;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Typ;
import de.gabriel.vertretungsplan.repository.SubstitutionPlanEntryRepository;
import de.gabriel.vertretungsplan.repository.TeacherRepository;
import de.gabriel.vertretungsplan.service.interfaces.SubstitutionPlanEntryService;
import de.gabriel.vertretungsplan.service.interfaces.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SubstitutionPlanEntryServiceImpl implements SubstitutionPlanEntryService {

    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final SubstitutionPlanEntryRepository substitutionPlanEntryRepository;

    private List<SubstitutionPlanEntry> createSubstitutionPlanEntries() {
        List<Teacher> absentTeachers = teacherRepository.findByAttendance(Attendance.ABWESEND);
        List<SubstitutionPlanEntry> substitutionPlanEntries = new ArrayList<>();
        if (!absentTeachers.isEmpty()) {
            substitutionPlanEntries.addAll(teacherService.getSubstitutionPlanEntries(absentTeachers));
        }
        return substitutionPlanEntries;
    }

    @Override
    public List<SubstitutionPlanEntry> getSubstitutionPlanEntries() {
        substitutionPlanEntryRepository.deleteAllByTyp(Typ.AUTO);
        substitutionPlanEntryRepository.saveAll(createSubstitutionPlanEntries());
        log.info("Updating substitute plan entries; {[TotalEntries={}]}", substitutionPlanEntryRepository.count());
        return substitutionPlanEntryRepository.findAll();
    }

}
