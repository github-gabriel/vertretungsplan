package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.enums.Day;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DayEntityRepositoryTest {

    @Autowired
    private DayRepository dayRepository;

    private DayEntity dayEntityExpected;

    @BeforeAll
    void init() {
        dayEntityExpected = new DayEntity(Day.MONDAY);

        dayRepository.save(dayEntityExpected);
    }

    @AfterAll
    void tearDown() {
        dayRepository.deleteAll();
    }

    @Test
    @DisplayName("Verify Correct Day Retrieval by Name")
    void findByName_shouldReturnDayByName() {
        DayEntity dayEntity = dayRepository.findByName(Day.MONDAY);

        assertEquals(dayEntityExpected.hashCode(), dayEntity.hashCode());
    }

}
