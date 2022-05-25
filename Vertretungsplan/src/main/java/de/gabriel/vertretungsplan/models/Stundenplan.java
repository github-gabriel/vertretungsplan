package de.gabriel.vertretungsplan.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stundenplan", schema = "public")
public class Stundenplan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stundenplan_generator")
    @SequenceGenerator(name = "stundenplan_generator", sequenceName = "stundenplan_seq")
    private int id;

    // ID = Stunde
    private String name;
    private String fach_montag;
    private String fachlehrer_montag;
    private String fach_dienstag;
    private String fachlehrer_dienstag;
    private String fach_mittwoch;
    private String fachlehrer_mittwoch;
    private String fach_donnerstag;
    private String fachlehrer_donnerstag;
    private String fach_freitag;
    private String fachlehrer_freitag;

    public Stundenplan() {

    }

    public Stundenplan(String name, String fach_montag, String fachlehrer_montag, String fach_dienstag, String fachlehrer_dienstag, String fach_mittwoch, String fachlehrer_mittwoch, String fach_donnerstag, String fachlehrer_donnerstag, String fach_freitag, String fachlehrer_freitag) {
        this.name = name;
        this.fach_montag = fach_montag;
        this.fachlehrer_montag = fachlehrer_montag;
        this.fach_dienstag = fach_dienstag;
        this.fachlehrer_dienstag = fachlehrer_dienstag;
        this.fach_mittwoch = fach_mittwoch;
        this.fachlehrer_mittwoch = fachlehrer_mittwoch;
        this.fach_donnerstag = fach_donnerstag;
        this.fachlehrer_donnerstag = fachlehrer_donnerstag;
        this.fach_freitag = fach_freitag;
        this.fachlehrer_freitag = fachlehrer_freitag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFach_montag() {
        return fach_montag;
    }

    public void setFach_montag(String fach_tag1) {
        this.fach_montag = fach_tag1;
    }

    public String getFach_dienstag() {
        return fach_dienstag;
    }

    public void setFach_dienstag(String fach_tag2) {
        this.fach_dienstag = fach_tag2;
    }

    public String getFach_mittwoch() {
        return fach_mittwoch;
    }

    public void setFach_mittwoch(String fach_tag3) {
        this.fach_mittwoch = fach_tag3;
    }

    public String getFach_donnerstag() {
        return fach_donnerstag;
    }

    public void setFach_donnerstag(String fach_tag4) {
        this.fach_donnerstag = fach_tag4;
    }

    public String getFach_freitag() {
        return fach_freitag;
    }

    public void setFach_freitag(String fach_tag5) {
        this.fach_freitag = fach_tag5;
    }

    public String getFachlehrer_montag() {
        return fachlehrer_montag;
    }

    public void setFachlehrer_montag(String fachlehrer_montag) {
        this.fachlehrer_montag = fachlehrer_montag;
    }

    public String getFachlehrer_dienstag() {
        return fachlehrer_dienstag;
    }

    public void setFachlehrer_dienstag(String fachlehrer_dienstag) {
        this.fachlehrer_dienstag = fachlehrer_dienstag;
    }

    public String getFachlehrer_mittwoch() {
        return fachlehrer_mittwoch;
    }

    public void setFachlehrer_mittwoch(String fachlehrer_mittwoch) {
        this.fachlehrer_mittwoch = fachlehrer_mittwoch;
    }

    public String getFachlehrer_donnerstag() {
        return fachlehrer_donnerstag;
    }

    public void setFachlehrer_donnerstag(String fachlehrer_donnerstag) {
        this.fachlehrer_donnerstag = fachlehrer_donnerstag;
    }

    public String getFachlehrer_freitag() {
        return fachlehrer_freitag;
    }

    public void setFachlehrer_freitag(String fachlehrer_freitag) {
        this.fachlehrer_freitag = fachlehrer_freitag;
    }
}
