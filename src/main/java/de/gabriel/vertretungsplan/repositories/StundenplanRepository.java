package de.gabriel.vertretungsplan.repositories;

import de.gabriel.vertretungsplan.models.Stundenplan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StundenplanRepository extends JpaRepository<Stundenplan, Integer> {

    // Faecher
    @Query("SELECT s.fach_montag FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFaecherMontag(@Param("name") String name);

    @Query("SELECT s.fach_dienstag FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFaecherDienstag(@Param("name") String name);

    @Query("SELECT s.fach_mittwoch FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFaecherMittwoch(@Param("name") String name);

    @Query("SELECT s.fach_donnerstag FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFaecherDonnerstag(@Param("name") String name);

    @Query("SELECT s.fach_freitag FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFaecherFreitag(@Param("name") String name);

    // Lehrer

    @Query("SELECT s.fachlehrer_montag FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFachlehrerMontag(@Param("name") String name);

    @Query("SELECT s.fachlehrer_dienstag FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFachlehrerDienstag(@Param("name") String name);

    @Query("SELECT s.fachlehrer_mittwoch FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFachlehrerMittwoch(@Param("name") String name);

    @Query("SELECT s.fachlehrer_donnerstag FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFachlehrerDonnerstag(@Param("name") String name);

    @Query("SELECT s.fachlehrer_freitag FROM Stundenplan s WHERE s.name=:#{#name}")
    public List<String> getFachlehrerFreitag(@Param("name") String name);

}


