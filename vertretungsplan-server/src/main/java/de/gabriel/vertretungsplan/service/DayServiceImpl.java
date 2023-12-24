package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.enums.Day;
import de.gabriel.vertretungsplan.repository.DayRepository;
import de.gabriel.vertretungsplan.service.interfaces.DayService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;

@RequiredArgsConstructor
@Service
public class DayServiceImpl implements DayService {

    private final Environment env;
    private final DayRepository dayRepository;
    private Calendar calendar;

    @Override
    public DayEntity getCurrentDay() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            switch (day) {
                case Calendar.MONDAY -> {
                    return this.getDayEntityByDayName(Day.MONDAY);
                }
                case Calendar.TUESDAY -> {
                    return this.getDayEntityByDayName(Day.TUESDAY);
                }
                case Calendar.WEDNESDAY -> {
                    return this.getDayEntityByDayName(Day.WEDNESDAY);
                }
                case Calendar.THURSDAY -> {
                    return this.getDayEntityByDayName(Day.THURSDAY);
                }
                case Calendar.FRIDAY -> {
                    return this.getDayEntityByDayName(Day.FRIDAY);
                }
                default -> {
                    return new DayEntity(Day.WEEKEND);
                }
            }
        } else if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
            return this.getDayEntityByDayName(Day.MONDAY);
        }
        throw new RuntimeException("Active profile must contain either dev or prod!");
    }

    private DayEntity getDayEntityByDayName(Day day) {
        return dayRepository.findByName(day);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

}
