package de.gabriel.vertretungsplan.utils.logging.user;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogUserSave {
    String message() default "Successfully saved user(s) with encrypted password";
}
