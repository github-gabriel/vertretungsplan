package de.gabriel.vertretungsplan.repositories;

import de.gabriel.vertretungsplan.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.anwesend = false")
    List<User> findAllMissingTeacher();

    @Query("SELECT u.userName FROM User u WHERE u.anwesend = false")
    List<String> findAllMissingTeacherNames();

    @Query("SELECT u FROM User u WHERE u.anwesend = true")
    List<User> findAllPresentTeacher();

    @Query("SELECT u.userName FROM User u WHERE u.anwesend = true")
    List<String> findAllPresentTeacherNames();

    @Query("SELECT u FROM User u WHERE u.anwesend = true AND u.faecher LIKE %:#{#fach}%")
    List<User> getVertreter(@Param("fach") String fach);

}
