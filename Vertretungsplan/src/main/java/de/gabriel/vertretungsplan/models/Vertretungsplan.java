package de.gabriel.vertretungsplan.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "vertretungsplan", schema = "public")
public class Vertretungsplan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vertretungsplan_generator")
    @SequenceGenerator(name = "vertretungsplan_generator", sequenceName = "vertretungsplan_seq")
    private int id;

    private String aenderung_art;
    private String klasse;
    private int stunde;
    private String vertreter;
    private String fach;

    public Vertretungsplan(String aenderung_art, String klasse, int stunde, String vertreter, String fach) {
        this.aenderung_art = aenderung_art;
        this.klasse = klasse;
        this.stunde = stunde;
        this.vertreter = vertreter;
        this.fach = fach;
    }

    public Vertretungsplan() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAenderung_art() {
        return aenderung_art;
    }

    public void setAenderung_art(String aenderung_art) {
        this.aenderung_art = aenderung_art;
    }

    public String getKlasse() {
        return klasse;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public int getStunde() {
        return stunde;
    }

    public void setStunde(int stunde) {
        this.stunde = stunde;
    }

    public String getVertreter() {
        return vertreter;
    }

    public void setVertreter(String vertreter) {
        this.vertreter = vertreter;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

}
