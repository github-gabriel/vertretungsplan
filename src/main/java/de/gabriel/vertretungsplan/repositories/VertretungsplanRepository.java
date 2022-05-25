package de.gabriel.vertretungsplan.repositories;

import de.gabriel.vertretungsplan.models.Vertretungsplan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VertretungsplanRepository extends JpaRepository<Vertretungsplan, Integer> {

    // Delete
    @Transactional
    @Modifying
    @Query("DELETE FROM Vertretungsplan v WHERE v.type=:#{#type}")
    void deleteAllWhere(@Param("type") String type);

}
