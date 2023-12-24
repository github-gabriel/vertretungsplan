package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    private Course courseExpected;

    @BeforeAll
    void init() {
        courseExpected = new Course("5a");

        courseRepository.save(courseExpected);
    }

    @Test
    @DisplayName("Retrieve Course by Name")
    void findByName_shouldReturnCourseByName() {
        Course course = courseRepository.findByName("5a");

        assertEquals(courseExpected, course);
    }
}
