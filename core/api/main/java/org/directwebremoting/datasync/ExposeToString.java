package org.directwebremoting.datasync;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A marker interface to indicate that {@link Object#toString()} does not
 * have any information in it that you don't want to be exposed to the
 * Internet.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeToString
{
}
