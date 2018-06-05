package org.directwebremoting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Filter remote accesses to a class.
 * <p>This allows you to specify multiple filters.
 * To apply only one filter, you may use the {@link Filter} annotation directly.</p>
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Filters
{
    /** The filters to apply. */
    Filter[] value() default {};
}
