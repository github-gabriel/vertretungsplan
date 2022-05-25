package de.gabriel.vertretungsplan.services;

import de.gabriel.vertretungsplan.VertretungsplanApplication;
import de.gabriel.vertretungsplan.models.User;
import de.gabriel.vertretungsplan.models.Vertretungsplan;
import de.gabriel.vertretungsplan.repositories.UserRepository;
import de.gabriel.vertretungsplan.repositories.VertretungsplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class MyVertretungsplanService {

    @Autowired
    VertretungsplanRepository vertretungsplanRepository;

    @Autowired
    UserRepository userRepository;
    private MyUserDetailsService myUserDetailsService;
    private MySchuelerDetailsService mySchuelerDetailsService;
    private MyStundenplanService myStundenplanService;
    private List<String> lehrer;
    private List<User> lehrerUsers;

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

    public void updateVertretungsplan() {
        myStundenplanService.getFaecher(VertretungsplanApplication.TAG, "Stundenplan Klasse 5");
        lehrer = myStundenplanService.getLehrer(VertretungsplanApplication.TAG, "Stundenplan Klasse 5"); // Lehrer die an diesem Tag[VertretungsplanApplication.TAG] eine Stunde haben
        lehrerUsers = lehrerNamesToUser(lehrer);
        checkIfLehrerIsNeeded(lehrerUsers);
    }

    private void checkIfLehrerIsNeeded(List<User> lehrer) {
        int i = 0;
        String fach = "";
        for (User user : lehrer) {
            if (!user.isAnwesend()) {
                fach = myStundenplanService.getFaecher(VertretungsplanApplication.TAG, "Stundenplan Klasse 5").get(i);
                Vertretungsplan vertretungsplan = new Vertretungsplan(
                        myUserDetailsService.getVertreter(fach).equals("N/A") ? "Entfall" : "Vertretung", // Art
                        "5", // Klasse
                        i + 1, // Stunde
                        myUserDetailsService.getVertreter(fach), // Vertreter
                        fach, // Fach
                        "auto" // Type
                );
                vertretungsplanRepository.save(vertretungsplan);
            }
            i++;
        }
    }

    private List<User> lehrerNamesToUser(List<String> lehrer) {
        List<User> userList = new ArrayList<>();

        for (String name : lehrer) {
            User user = getTeacherFromName(name);
            userList.add(user);
        }

        return userList;
    }

    private User getTeacherFromName(String name) {
        for (User user : myUserDetailsService.getUsers()) {
            if (user.getUserName().equals(name)) {
                return user;
            }
        }
        return null;
    }
    
}
