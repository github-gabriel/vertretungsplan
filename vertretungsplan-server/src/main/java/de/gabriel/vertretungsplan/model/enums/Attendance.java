package de.gabriel.vertretungsplan.model.enums;

import de.gabriel.vertretungsplan.exception.exceptions.data.ResourceNotFoundException;

public enum Attendance {

    ANWESEND(),

    ABWESEND();

    Attendance() {
    }

    public static Attendance getAttendanceFromString(String attendance) {
        try {
            return Attendance.valueOf(attendance.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("A enum with the name " + attendance + " doesn't exist!");
        }
    }

}
