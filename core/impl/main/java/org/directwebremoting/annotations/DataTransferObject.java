package org.directwebremoting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.extend.Converter;

/**
 * Convert a class to JavaScript and back.
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTransferObject
{
    /**
     * Converter that converts instance of the class (default: bean converter).
     */
    Class<? extends Converter> converter() default BeanConverter.class;

    /**
     * Parameters for the converter.
     */
    Param[] params() default {};

    /**
     * Converter type
     * TODO: Just used by Spring configurator
     */
    String type() default "bean";

    /**
     * Javascript class mapping.
     * TODO: Just used by Spring configurator
     */
    String javascript() default "";

}
