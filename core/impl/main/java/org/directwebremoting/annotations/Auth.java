package org.directwebremoting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restrict remote access to a method to users authorized under a certain J2EE role.
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @noinspection AnnotationNamingConvention
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth
{
    /**
     * Name of the J2EE role to restrict access to.
     */
    String[] role();
}
