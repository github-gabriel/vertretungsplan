package de.gabriel.vertretungsplan.model.enums;

import de.gabriel.vertretungsplan.exception.exceptions.data.ResourceNotFoundException;

public enum Role {

    PREFIX("ROLE_"),

    STUDENT("STUDENT"),

    TEACHER("TEACHER"),

    ADMINISTRATOR("ADMINISTRATOR");

    final String value;

    Role(String value) {
        this.value = value;
    }

    public static String getRoleWithPrefix(Role role) {
        return PREFIX.value + role.value;
    }

    public static String getRoleAsString(Role role) {
        return role.value;
    }

    public static Role getRoleFromStringWithPrefix(String roleWithPrefix) {
        try {
            return Role.valueOf(roleWithPrefix.substring(PREFIX.value.length()));
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("A enum with the name " + roleWithPrefix + " doesn't exist!");
        }
    }

}
