package de.gabriel.vertretungsplan.utils.logging.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogDtoConversion {

    String message();

    Class<?> dto();

    Class<?> entity();

}
