package de.gabriel.vertretungsplan.services;

import de.gabriel.vertretungsplan.VertretungsplanApplication;
import de.gabriel.vertretungsplan.models.User;
import de.gabriel.vertretungsplan.models.Vertretungsplan;
import de.gabriel.vertretungsplan.repositories.VertretungsplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MyVertretungsplanService {

    @Autowired
    VertretungsplanRepository vertretungsplanRepository;
    private MyUserDetailsService myUserDetailsService;
    private MySchuelerDetailsService mySchuelerDetailsService;
    private MyStundenplanService myStundenplanService;


    @Autowired
    public MyVertretungsplanService(MyUserDetailsService myUserDetailsService, MySchuelerDetailsService mySchuelerDetailsService
            , MyStundenplanService myStundenplanService) {
        this.myUserDetailsService = myUserDetailsService;
        this.mySchuelerDetailsService = mySchuelerDetailsService;
        this.myStundenplanService = myStundenplanService;
    }

    public List<Vertretungsplan> getVertretungsplanEintraege() {
        return vertretungsplanRepository.findAll();
    }

    public List<Vertretungsplan> updateVertretungsplan() {
        // TODO: Wenn Lehrer nicht anwesend -> Gucken welcher Tag es ist; Für Stundenpläne gucken ob Lehrer gebraucht wird -> Wenn ja, Stunde entfallen Eintrag
        myStundenplanService.getFaecherKlasse5(VertretungsplanApplication.TAG);
        List<String> lehrer = myStundenplanService.getLehrerKlasse5(VertretungsplanApplication.TAG); // Lehrer die an diesem Tag[VertretungsplanApplication.TAG] eine Stunde haben
        List<User> lehrerUsers = lehrerNamesToUser(lehrer);
        checkIfLehrerIsNeeded(lehrerUsers);
        return null;
    }

    private void checkIfLehrerIsNeeded(List<User> lehrer) {
        // TODO: Wenn Lehrer aus der Liste von "findAllMissingTeacher()" auftaucht -> Neuer Entfall Eintrag
        for (User user : lehrer) {
            System.out.println("[Vertretungsplan] User: " + user.getUserName());
            if (myUserDetailsService.getMissingTeachersNames().contains(user.getUserName())) {
                System.out.println("[Vertretungsplan] Neuer Eintrag!");
                // TODO: Infos über die Stunde herausfinden; Vertreter finden
                Vertretungsplan vertretungsplan = new Vertretungsplan(
                        Objects.equals(myStundenplanService.getVertreter(user.getFaecher()), "N/A") ? "Entfall" : "Vertretung",
                        "5",
                        -1,
                        Objects.equals(myStundenplanService.getVertreter(user.getFaecher()), "N/A") ? "N/A" : myStundenplanService.getVertreter(user.getFaecher()),
                        "-"
                );
                vertretungsplanRepository.save(vertretungsplan);
            } else {
                System.out.println("[Vertretungsplan] Missing Teachers doesn't contain " + user.getUserName());
            }
        }
    }

    private List<User> lehrerNamesToUser(List<String> lehrer) {
        List<User> user = new ArrayList<>();
        for (User userForEach : myUserDetailsService.getUsers()) {
            if (lehrer.contains(userForEach.getUserName())) {
                user.add(userForEach);
            }
        }
        return user;
    }

}
