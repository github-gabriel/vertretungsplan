package de.gabriel.vertretungsplan.repository;

import de.gabriel.vertretungsplan.model.entities.DayEntity;
import de.gabriel.vertretungsplan.model.enums.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<DayEntity, Integer> {

    @Query("SELECT t FROM DayEntity t WHERE t.dayName = ?1")
    DayEntity findByName(Day day);

}
