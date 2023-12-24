package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.enums.Day;
import de.gabriel.vertretungsplan.repository.DayRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.env.MockEnvironment;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class DayEntityServiceImplTest {

    @Mock
    private MockEnvironment environment;

    @Mock
    private DayRepository dayRepository;

    @InjectMocks
    private DayServiceImpl dayServiceImpl;

    @Test
    @DisplayName("Get Current Day of the Week for dev Profile")
    void getCurrentDay_shouldReturnMondayForDev() {
        environment.setActiveProfiles("dev");

        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
        when(dayRepository.findByName(Day.MONDAY)).thenReturn(new DayEntity(Day.MONDAY));

        dayServiceImpl = new DayServiceImpl(environment, dayRepository);

        DayEntity dayEntity = dayServiceImpl.getCurrentDay();

        assertEquals(new DayEntity(Day.MONDAY).hashCode(), dayEntity.hashCode());
    }

    @Test
    @DisplayName("Get Current Day of the Week for prod Profile")
    void getCurrentDay_shouldReturnMondayForProd() {
        Calendar c = mock(Calendar.class);
        when(c.get(Calendar.DAY_OF_WEEK)).thenReturn(Calendar.FRIDAY);

        when(dayRepository.findByName(Day.FRIDAY)).thenReturn(new DayEntity(Day.FRIDAY));

        environment.setActiveProfiles("prod");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});

        dayServiceImpl = new DayServiceImpl(environment, dayRepository);

        dayServiceImpl.setCalendar(c);

        DayEntity dayEntity = dayServiceImpl.getCurrentDay();

        assertEquals(new DayEntity(Day.FRIDAY).hashCode(), dayEntity.hashCode());
    }

}
