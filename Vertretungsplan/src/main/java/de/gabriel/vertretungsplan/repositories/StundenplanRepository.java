package de.gabriel.vertretungsplan.repositories;

import de.gabriel.vertretungsplan.models.Stundenplan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StundenplanRepository extends JpaRepository<Stundenplan, Integer> {

    // Faecher
    @Query("SELECT s.fach_montag FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFaecherMontagKlasse5();

    @Query("SELECT s.fach_dienstag FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFaecherDienstagKlasse5();

    @Query("SELECT s.fach_mittwoch FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFaecherMittwochKlasse5();

    @Query("SELECT s.fach_donnerstag FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFaecherDonnerstagKlasse5();

    @Query("SELECT s.fach_freitag FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFaecherFreitagKlasse5();

    // Lehrer

    @Query("SELECT s.fachlehrer_montag FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFachlehrerMontagKlasse5();

    @Query("SELECT s.fachlehrer_dienstag FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFachlehrerDienstagKlasse5();

    @Query("SELECT s.fachlehrer_mittwoch FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFachlehrerMittwochKlasse5();

    @Query("SELECT s.fachlehrer_donnerstag FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFachlehrerDonnerstagKlasse5();

    @Query("SELECT s.fachlehrer_freitag FROM Stundenplan s WHERE s.name='Stundenplan Klasse 5'")
    public List<String> getFachlehrerFreitagKlasse5();

}


