package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.model.entities.users.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User userExpected;

    @BeforeAll
    void init() {
        userExpected = new Administrator("admin", "admin@gmail.com", "admin");

        userRepository.saveAll(List.of(
                userExpected
        ));
    }

    @AfterAll
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Confirm Correct User Retrieval Given Valid Email")
    void findByEmail_shouldReturnUserByEmail() {
        Optional<User> user = userRepository.findByEmail(userExpected.getEmail());

        assertTrue(user.isPresent());
        assertEquals(userExpected.hashCode(), user.get().hashCode());
    }

}
