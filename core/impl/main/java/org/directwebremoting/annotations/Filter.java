package org.directwebremoting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.directwebremoting.AjaxFilter;

/**
 * Filter remote accesses to a class.
 * <p>To apply multiple filters, use the {@link Filters} annotation.</p>
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter
{
    /**
     * The filter implementation to use.
     */
    Class<? extends AjaxFilter> type();

    /**
     * Parameters for the filter.
     */
    Param[] params() default {};
}
