package de.gabriel.vertretungsplan;

import de.gabriel.vertretungsplan.models.MyUserDetails;
import de.gabriel.vertretungsplan.models.Schueler;
import de.gabriel.vertretungsplan.models.User;
import de.gabriel.vertretungsplan.services.MySchuelerDetailsService;
import de.gabriel.vertretungsplan.services.MyStundenplanService;
import de.gabriel.vertretungsplan.services.MyUserDetailsService;
import de.gabriel.vertretungsplan.services.MyVertretungsplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeResource {

    private int id;
    private MyUserDetailsService myUserDetailsService;
    private MySchuelerDetailsService mySchuelerDetailsService;
    private MyStundenplanService myStundenplanService;
    private MyVertretungsplanService myVertretungsplanService;

    @Autowired
    public HomeResource(MyUserDetailsService myUserDetailsService, MySchuelerDetailsService mySchuelerDetailsService, MyStundenplanService myStundenplanService, MyVertretungsplanService myVertretungsplanService) {
        this.myUserDetailsService = myUserDetailsService;
        this.mySchuelerDetailsService = mySchuelerDetailsService;
        this.myStundenplanService = myStundenplanService;
        this.myVertretungsplanService = myVertretungsplanService;
    }

    public HomeResource() {
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("vertretungsplan_eintraege", myVertretungsplanService.getVertretungsplanEintraege());
        return "view/vertretungsplan";
    }

    @GetMapping("/lehrer")
    public String lehrer(@AuthenticationPrincipal MyUserDetails userDetails,
                         Model model) {
        String userName = userDetails.getUsername();
        MyUserDetails user = (MyUserDetails) myUserDetailsService.loadUserByUsername(userName);
        model.addAttribute("user", myUserDetailsService.getUserById(user.getId()));
        return "view/teacher";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", myUserDetailsService.getUsers());
        model.addAttribute("students", mySchuelerDetailsService.getStudents());
        model.addAttribute("stundenplaene", myStundenplanService.getStundenplanEintraege());
        return "view/admin";
    }

    /* Lehrer */

    @GetMapping("/admin/lehrer/new")
    public String createTeacher(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "edit/create_teacher";
    }

    @PostMapping("/admin/lehrer")
    public String saveTeacher(@ModelAttribute("user") User user) {
        user.setAnwesend(true);
        myUserDetailsService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/lehrer/edit/{id}")
    public String editTeacherForm(@PathVariable Integer id, Model model) {
        model.addAttribute("user", myUserDetailsService.getUserById(id));
        return "edit/edit_teacher";
    }

    @PostMapping("/admin/lehrer/{id}")
    public String updateTeacher(@PathVariable Integer id,
                                @ModelAttribute("user") User user,
                                Model model) {

        User existingUser = myUserDetailsService.getUserById(id);
        existingUser.setId(id);
        existingUser.setRoles(user.getRoles());
        existingUser.setActive(user.isActive());

        System.out.println("Passwort: " + existingUser.getPassword());

        myUserDetailsService.updateUser(existingUser);
        return "redirect:/admin";

    }

    @GetMapping("/admin/lehrer/{id}")
    public String deleteUser(@PathVariable Integer id) {
        myUserDetailsService.deleteUser(id);
        return "redirect:/admin";
    }

    /* Schueler */

    @GetMapping("/admin/schueler/new")
    public String createStudent(Model model) {
        Schueler schueler = new Schueler();
        model.addAttribute("student", schueler);
        return "edit/create_student";
    }

    @PostMapping("/admin/schueler")
    public String saveStudent(@ModelAttribute("student") Schueler schueler) {
        mySchuelerDetailsService.saveStudent(schueler);
        return "redirect:/admin";
    }

    @GetMapping("/admin/schueler/edit/{id}")
    public String editStudentForm(@PathVariable Integer id, Model model) {
        model.addAttribute("student", mySchuelerDetailsService.getStudentById(id));
        return "edit/edit_student";
    }

    @PostMapping("/admin/schueler/{id}")
    public String updateStudent(@PathVariable Integer id,
                                @ModelAttribute("student") Schueler schueler,
                                Model model) {

        Schueler existingStudent = mySchuelerDetailsService.getStudentById(id);
        existingStudent.setId(id);
        existingStudent.setRoles(schueler.getRoles());
        existingStudent.setActive(schueler.isActive());
        existingStudent.setKlasse(schueler.getKlasse());

        System.out.println("Passwort: " + existingStudent.getPassword());

        mySchuelerDetailsService.updateStudent(existingStudent);
        return "redirect:/admin";

    }

    @GetMapping("/admin/schueler/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        mySchuelerDetailsService.deleteStudent(id);
        return "redirect:/admin";
    }

    /* Lehrer Form */

    @GetMapping("/lehrer/krankschreiben/{id}")
    public String form(@PathVariable Integer id, Model model) {
        model.addAttribute("user", myUserDetailsService.getUserById(id));
        return "edit/teacher_form";
    }

    @PostMapping("/lehrer/krankschreiben/{id}")
    public String anwesenheitUpdaten(@PathVariable Integer id,
                                     @ModelAttribute("user") User user,
                                     Model model) {

        User existingUser = myUserDetailsService.getUserById(id);
        existingUser.setAnwesend(user.isAnwesend());

        System.out.println(existingUser.isAnwesend());
        System.out.println("Passwort: " + existingUser.getPassword());

        myUserDetailsService.updateUser(existingUser);
        return "redirect:/lehrer";

    }

    @GetMapping("/lehrer/edit/{id}")
    public String editTeacher(@PathVariable Integer id, Model model) {
        model.addAttribute("user", myUserDetailsService.getUserById(id));
        return "edit/edit_teacher_settings";
    }

    @PostMapping("/lehrer/edit/{id}")
    public String updateTeacherSettings(@PathVariable Integer id,
                                        @ModelAttribute("user") User user,
                                        Model model) {

        User existingUser = myUserDetailsService.getUserById(id);
        existingUser.setId(id);
        existingUser.setUserName(user.getUserName());
        existingUser.setPassword(user.getPassword());

        myUserDetailsService.updateUser(existingUser);
        return "redirect:/lehrer";

    }

}