package org.directwebremoting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A parameter consisting of a name/value pair.
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param
{
    /**
     * The parameter's name.
     */
    String name();

    /**
     * The parameter's value.
     */
    String value();
}
