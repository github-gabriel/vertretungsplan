package de.gabriel.vertretungsplan.repositories;

import de.gabriel.vertretungsplan.models.Vertretungsplan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VertretungsplanRepository extends JpaRepository<Vertretungsplan, Integer> {

}
