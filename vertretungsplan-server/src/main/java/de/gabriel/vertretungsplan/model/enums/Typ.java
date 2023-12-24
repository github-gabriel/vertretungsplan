package de.gabriel.vertretungsplan.model.enums;

import de.gabriel.vertretungsplan.exception.exceptions.data.ResourceNotFoundException;

public enum Typ {

    AUTO,
    MANUELL;

    public static Typ getTypFromString(String typ) {
        try {
            return valueOf(typ.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("A enum with the name " + typ + " doesn't exist!");
        }
    }

}
