package de.gabriel.vertretungsplan;

import de.gabriel.vertretungsplan.repositories.VertretungsplanRepository;
import de.gabriel.vertretungsplan.services.MyVertretungsplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.SQLOutput;

@EnableScheduling
@Configuration
public class Updater {

    @Autowired
    MyVertretungsplanService myVertretungsplanService;

    @Autowired
    VertretungsplanRepository vertretungsplanRepository;

    @Scheduled(fixedDelay = 3000)
    public void update() {
        vertretungsplanRepository.deleteAll();
        myVertretungsplanService.updateVertretungsplan();
        System.out.println("[Updater] Vertretungsplan is up to date!");
    }

}