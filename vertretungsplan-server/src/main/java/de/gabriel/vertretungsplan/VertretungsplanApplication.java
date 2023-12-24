package de.gabriel.vertretungsplan;

import de.gabriel.vertretungsplan.model.entities.Course;
import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.entities.Subject;
import de.gabriel.vertretungsplan.model.entities.TimetableEntry;
import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.Student;
import de.gabriel.vertretungsplan.model.entities.users.Teacher;
import de.gabriel.vertretungsplan.model.enums.Attendance;
import de.gabriel.vertretungsplan.model.enums.Day;
import de.gabriel.vertretungsplan.repository.CourseRepository;
import de.gabriel.vertretungsplan.repository.DayRepository;
import de.gabriel.vertretungsplan.repository.SubjectRepository;
import de.gabriel.vertretungsplan.repository.TimetableEntryRepository;
import de.gabriel.vertretungsplan.service.interfaces.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.gabriel.vertretungsplan.config.ApplicationConfig.DEFAULT_PASSWORD;

@SpringBootApplication
public class VertretungsplanApplication {

    public static void main(String[] args) {
        SpringApplication.run(VertretungsplanApplication.class, args);
    }

    @Bean
    @Profile("!test")
    CommandLineRunner commandLineRunner(SubjectRepository subjectRepository, CourseRepository courseRepository,
                                        TimetableEntryRepository timetableEntryRepository, DayRepository dayRepository,
                                        UserService userService) {
        return args -> {
            // Subjects

            Subject de = new Subject("Deutsch", "DE");
            Subject ma = new Subject("Mathe", "MA");
            Subject en = new Subject("Englisch", "EN");
            Subject la = new Subject("Latein", "LA");
            Subject bio = new Subject("Biologie", "BI");
            Subject re = new Subject("Religion", "RE");
            Subject phy = new Subject("Physik", "PHY");

            subjectRepository.saveAll(List.of(de, ma, en, la, bio, re, phy));

            // Teachers

            Teacher lde = new Teacher("Juliane Köppen", "JK", "juliane.köppen@gmail.com", DEFAULT_PASSWORD, Attendance.ABWESEND);
            lde.setSubjects(List.of(de, la));
            Teacher lma = new Teacher("Martin Brauren", "MB", "martin.brauren@gmail.com", DEFAULT_PASSWORD, Attendance.ABWESEND);
            lma.setSubjects(List.of(ma, en));
            Teacher lbio = new Teacher("Yannick Herbrich", "YH", "yannick.herbrich@gmail.com", DEFAULT_PASSWORD, Attendance.ANWESEND);
            lbio.setSubjects(List.of(bio, phy));
            Teacher lla = new Teacher("Karla Kurzawa", "KK", "karla.kurzawa@gmail.com", DEFAULT_PASSWORD, Attendance.ANWESEND);
            lla.setSubjects(List.of(la, de));
            Teacher len = new Teacher("Hildegard Bätz", "HB", "hildegard.bätz@gmail.com", DEFAULT_PASSWORD, Attendance.ANWESEND);
            len.setSubjects(List.of(en, de));
            Teacher lre = new Teacher("Till Redl", "TR", "till.redl@gmail.com", DEFAULT_PASSWORD, Attendance.ANWESEND);
            lre.setSubjects(List.of(re, de));

            userService.saveTeachers(List.of(lma, lbio, lla, len, lre, lde));

            // Days

            DayEntity montag = new DayEntity(Day.MONDAY);
            DayEntity dienstag = new DayEntity(Day.TUESDAY);
            DayEntity mittwoch = new DayEntity(Day.WEDNESDAY);
            DayEntity donnerstag = new DayEntity(Day.THURSDAY);
            DayEntity freitag = new DayEntity(Day.FRIDAY);
            DayEntity wochenende = new DayEntity(Day.WEEKEND);

            dayRepository.saveAll(List.of(montag, dienstag, mittwoch, donnerstag, freitag, wochenende));

            // Courses

            Course kl = new Course("KL");
            Course kl1 = new Course("KL1");

            // Students

            List<Student> studentList = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                int course = i % 2;
                Student student = new Student("student" + i, "student" + i + "@gmail.com", DEFAULT_PASSWORD, course == 0 ? kl : kl1);
                studentList.add(student);
            }

            userService.saveStudents(studentList);

            Set<Student> studentSet = new HashSet<>(studentList);

            kl.setStudents(studentSet);

            courseRepository.saveAll(List.of(kl, kl1));

            // Administrator

            Administrator administrator = new Administrator("administrator", "administrator@gmail.com", DEFAULT_PASSWORD);

            userService.saveUsers(List.of(administrator));

            // Timetable Entries for KL

            TimetableEntry timetableEntryKL_1 = new TimetableEntry(1, kl, de, lde, montag);
            TimetableEntry timetableEntryKL_2 = new TimetableEntry(2, kl, de, lde, montag);
            TimetableEntry timetableEntryKL_3 = new TimetableEntry(3, kl, ma, lma, montag);
            TimetableEntry timetableEntryKL_4 = new TimetableEntry(4, kl, en, len, montag);
            TimetableEntry timetableEntryKL_5 = new TimetableEntry(5, kl, la, lla, montag);
            TimetableEntry timetableEntryKL_6 = new TimetableEntry(6, kl, la, lla, montag);

            // Timetable Entries for KL1

            TimetableEntry timetableEntryKL1_1 = new TimetableEntry(1, kl1, re, lre, montag);
            TimetableEntry timetableEntryKL1_2 = new TimetableEntry(1, kl1, bio, lbio, montag);

            timetableEntryRepository.saveAll(List.of(timetableEntryKL_1, timetableEntryKL_2, timetableEntryKL_3, timetableEntryKL_4, timetableEntryKL_5, timetableEntryKL_6,
                    timetableEntryKL1_1, timetableEntryKL1_2));
        };
    }
}
