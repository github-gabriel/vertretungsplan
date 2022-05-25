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

    public List<String> getFaecher(String tag, String klasse) {
        List<String> faecher = null;
        tag = tag.toLowerCase();
        switch (tag) {
            case "montag" -> faecher = stundenplanRepository.getFaecherMontag(klasse);
            case "dienstag" -> faecher = stundenplanRepository.getFaecherDienstag(klasse);
            case "mittwoch" -> faecher = stundenplanRepository.getFaecherMittwoch(klasse);
            case "donnerstag" -> faecher = stundenplanRepository.getFaecherDonnerstag(klasse);
            case "freitag" -> faecher = stundenplanRepository.getFaecherFreitag(klasse);
            default -> {
                throw new RuntimeException("Error! " + tag + " existiert nicht!");
            }
        }
        return faecher;
    }

    public List<String> getLehrer(String tag, String klasse) {
        List<String> lehrer = null;
        tag = tag.toLowerCase();
        switch (tag) {
            case "montag" -> lehrer = stundenplanRepository.getFachlehrerMontag(klasse);
            case "dienstag" -> lehrer = stundenplanRepository.getFachlehrerDienstag(klasse);
            case "mittwoch" -> lehrer = stundenplanRepository.getFachlehrerMittwoch(klasse);
            case "donnerstag" -> lehrer = stundenplanRepository.getFachlehrerDonnerstag(klasse);
            case "freitag" -> lehrer = stundenplanRepository.getFachlehrerFreitag(klasse);
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
