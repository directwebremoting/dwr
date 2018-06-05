package org.directwebremoting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Make property available for remote access.
 * <p>This can be applied to fields and getter methods.
 * When applied to a field, there must be a getter method for that field.</p>
 * <p>This annotation is only useful when using {@link DataTransferObject} with the bean converter.</p>
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteProperty
{
}
