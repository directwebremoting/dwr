package org.directwebremoting.guice.util;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Analogous to Guice's Named annotation, with long instead of String value.
 * @author Tim Peierls [tim at peierls dot net]
 */
@Retention(RUNTIME)
@Target({TYPE})
@BindingAnnotation
public @interface Numbered
{
    long value();
}
