package de.gabriel.vertretungsplan.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "schueler", schema = "public")
public class Schueler implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schueler_generator")
    @SequenceGenerator(name = "schueler_generator", sequenceName = "schueler_seq")
    private int id;
    private String userName;
    private String password;
    private String klasse;
    private boolean active;
    private String roles;

    public Schueler() {

    }

    public Schueler(String userName, String password, String klasse, boolean active, String roles) {
        this.userName = userName;
        this.password = password;
        this.klasse = klasse;
        this.active = active;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKlasse() {
        return klasse;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}