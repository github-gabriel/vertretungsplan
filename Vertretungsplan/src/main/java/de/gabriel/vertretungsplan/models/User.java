package de.gabriel.vertretungsplan.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "lehrer", schema = "public")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq")
    private int id;
    private String userName;
    private String password;
    private String roles;
    private String faecher;
    private boolean active;
    private boolean anwesend;

    public User() {

    }

    public User(String userName, String password, String roles, String faecher, boolean anwesend, boolean active) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.faecher = faecher;
        this.anwesend = anwesend;
        this.active = active;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getFaecher() {
        return faecher;
    }

    public void setFaecher(String fach) {
        this.faecher = fach;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAnwesend() {
        return anwesend;
    }

    public void setAnwesend(boolean anwesend) {
        this.anwesend = anwesend;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", faecher='" + faecher + '\'' +
                ", active=" + active +
                ", anwesend=" + anwesend +
                '}';
    }
}