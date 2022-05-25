package de.gabriel.vertretungsplan;

import de.gabriel.vertretungsplan.models.Schueler;
import de.gabriel.vertretungsplan.models.Stundenplan;
import de.gabriel.vertretungsplan.models.User;
import de.gabriel.vertretungsplan.models.Vertretungsplan;
import de.gabriel.vertretungsplan.repositories.SchuelerRepository;
import de.gabriel.vertretungsplan.repositories.StundenplanRepository;
import de.gabriel.vertretungsplan.repositories.UserRepository;
import de.gabriel.vertretungsplan.repositories.VertretungsplanRepository;
import de.gabriel.vertretungsplan.services.MySchuelerDetailsService;
import de.gabriel.vertretungsplan.services.MyStundenplanService;
import de.gabriel.vertretungsplan.services.MyUserDetailsService;
import de.gabriel.vertretungsplan.services.MyVertretungsplanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, SchuelerRepository.class})
public class VertretungsplanApplication {

    public static final String TAG = "MONTAG";

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, SchuelerRepository schuelerRepository
            , StundenplanRepository stundenplanRepository, VertretungsplanRepository vertretungsplanRepository, MySchuelerDetailsService mySchuelerDetailsService,
                                        MyStundenplanService myStundenplanService, MyUserDetailsService myUserDetailsService, MyVertretungsplanService myVertretungsplanService) {
        return args -> {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            final String[] FAECHER = {"", "LA,GE", "DE,EK", "DE,SA", "DE,KU", "EN,GE", "EN,DE", "MA,RE", "DE,SA", "SP,PW", "EN,SP"
                    , "MA,PHY", "MA,SA", "EN,GE", "MA,PW", "EN,DE", "DE,GE", "MA,PHY", "BIO,CH", "DE,FR", "DE,WN"
                    , "BIO,EK", "GE,RE", "EN,SA", "MA,SP", "DE,GE,WN", "DE,MU", "CH,WN", "PW,RE", "SP,BIO", "EN,DE"
                    , "MA,SP", "DE,SP", "EN,SP", "MA,PHY", "DE,KU", "BIO,EK", "MA,WN", "MU,FR", "PW,SA", "FR,GE"
                    , "DE,RE", "WN,SA", "EK,PW", "DE,KU", "MU,RE", "EN", "SA,BIO", "DE,RE,LA", "EN,DE", "FR,SP"
                    , "BIO,CH", "MA,PHY", "SP,DE", "EN,BIO", "KU", "FR,LA", "KU,GE", "MA,GE", "EN,MA", "EN,BIO"
                    , "DE,EK", "BIO,CH", "RE,PW", "MA,DE", "MA,PHY", "CH,DE", "DE,LA", "DE,BIO", "DE,KU", "EN,GE"
                    , "MA,CH", "MU", "EN,KU", "DE,EK"};

            User admin = new User(
                    "admin",
                    "$2a$12$q8Wfa/U0lPaXSlUike8ZHeqJIOqrr5vpkoalnMhjY8Y28wgwklsUS",
                    "ROLE_ADMIN",
                    "-",
                    true,
                    true
            );

            for (int i = 1; i < 75; i++) {
                User lehrer = new User(
                        "lehrer" + i,
                        encoder.encode("lehrer" + i),
                        "ROLE_LEHRER",
                        FAECHER[i],
                        i < 39,
                        true

                );
                userRepository.save(lehrer);
            }
            userRepository.save(admin);

            for (int i = 1; i < 51; i++) {
                Schueler schueler = new Schueler(
                        "schueler" + i,
                        encoder.encode("schueler" + i),
                        "8b",
                        true,
                        "ROLE_SCHUELER"
                );
                schuelerRepository.save(schueler);
            }

            Stundenplan stundenplan5_1 = new Stundenplan(
                    "Stundenplan Klasse 5",
                    "SP",
                    "lehrer9",
                    "DE",
                    "lehrer74",
                    "EN",
                    "lehrer59",
                    "N/A",
                    "N/A",
                    "EN",
                    "lehrer59"
            );

            Stundenplan stundenplan5_2 = new Stundenplan(
                    "Stundenplan Klasse 5",
                    "SP",
                    "lehrer9",
                    "WN/RE",
                    "lehrer16/lehrer41",
                    "WN/RE",
                    "lehrer16/lehrer41",
                    "MA",
                    "lehrer37",
                    "GE",
                    "lehrer57"
            );

            Stundenplan stundenplan5_3 = new Stundenplan(
                    "Stundenplan Klasse 5",
                    "KU",
                    "lehrer44",
                    "CH",
                    "lehrer51",
                    "MA",
                    "lehrer37",
                    "DE",
                    "lehrer74",
                    "MA",
                    "lehrer37"
            );

            Stundenplan stundenplan5_4 = new Stundenplan(
                    "Stundenplan Klasse 5",
                    "KU",
                    "lehrer44",
                    "EK",
                    "lehrer43",
                    "MU",
                    "lehrer38",
                    "BI",
                    "lehrer47",
                    "DE",
                    "lehrer74"
            );

            Stundenplan stundenplan5_5 = new Stundenplan(
                    "Stundenplan Klasse 5",
                    "DE",
                    "lehrer74",
                    "GE",
                    "lehrer57",
                    "EK",
                    "lehrer43",
                    "EN",
                    "lehrer59",
                    "MU",
                    "lehrer38"
            );

            Stundenplan stundenplan5_6 = new Stundenplan(
                    "Stundenplan Klasse 5",
                    "EN",
                    "lehrer59",
                    "MA",
                    "lehrer37",
                    "VERF.",
                    "lehrer37",
                    "CH",
                    "lehrer51",
                    "BI",
                    "lehrer47"
            );

            stundenplanRepository.saveAll(List.of(stundenplan5_1, stundenplan5_2, stundenplan5_3
                    , stundenplan5_4, stundenplan5_5, stundenplan5_6));

//            Vertretungsplan vertretungsplan = new Vertretungsplan(
//                    "ENTFALL",
//                    "8b",
//                    4,
//                    "lehrer1",
//                    "MA"
//            );
//
//            vertretungsplanRepository.save(vertretungsplan);

            System.out.println("Missing Teachers: " + userRepository.findAllMissingTeacher().toString());
            System.out.println("Present Teachers: " + userRepository.findAllPresentTeacher().toString());

            System.out.println("Faecher Montag: " + stundenplanRepository.getFaecherMontagKlasse5());

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(VertretungsplanApplication.class, args);
    }

}
