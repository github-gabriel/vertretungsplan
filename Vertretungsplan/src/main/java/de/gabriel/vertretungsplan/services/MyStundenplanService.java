package de.gabriel.vertretungsplan.services;

import de.gabriel.vertretungsplan.models.Stundenplan;
import de.gabriel.vertretungsplan.models.User;
import de.gabriel.vertretungsplan.repositories.StundenplanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyStundenplanService {

    @Autowired
    StundenplanRepository stundenplanRepository;
    @Autowired
    MyUserDetailsService myUserDetailsService;

    public List<Stundenplan> getStundenplanEintraege() {
        return stundenplanRepository.findAll();
    }

    public List<String> getFaecherKlasse5(String tag) {
        List<String> faecher = null;
        tag = tag.toLowerCase();
        switch (tag) {
            case "montag" -> faecher = stundenplanRepository.getFaecherMontagKlasse5();
            case "dienstag" -> faecher = stundenplanRepository.getFaecherDienstagKlasse5();
            case "mittwoch" -> faecher = stundenplanRepository.getFaecherMittwochKlasse5();
            case "donnerstag" -> faecher = stundenplanRepository.getFaecherDonnerstagKlasse5();
            case "freitag" -> faecher = stundenplanRepository.getFaecherFreitagKlasse5();
            default -> {
                throw new RuntimeException("Error! " + tag + " existiert nicht!");
            }
        }
        return faecher;
    }

    public List<String> getLehrerKlasse5(String tag) {
        List<String> lehrer = null;
        tag = tag.toLowerCase();
        switch (tag) {
            case "montag" -> lehrer = stundenplanRepository.getFachlehrerMontagKlasse5();
            case "dienstag" -> lehrer = stundenplanRepository.getFachlehrerDienstagKlasse5();
            case "mittwoch" -> lehrer = stundenplanRepository.getFachlehrerMittwochKlasse5();
            case "donnerstag" -> lehrer = stundenplanRepository.getFachlehrerDonnerstagKlasse5();
            case "freitag" -> lehrer = stundenplanRepository.getFachlehrerFreitagKlasse5();
            default -> {
                throw new RuntimeException("Error! " + tag + " existiert nicht!");
            }
        }
        return lehrer;
    }

    public String getVertreter(String fach) {
        User vertreter = null;
        for (User user : myUserDetailsService.getPresentTeachers()) {
            if (user.getFaecher().contains(fach) && user.isActive()) {
                vertreter = user;
                System.out.println("[Stundenplan Service] Match als Vertreter!");
                return vertreter.getUserName();
            }
        }
        return "N/A";
    }

}
