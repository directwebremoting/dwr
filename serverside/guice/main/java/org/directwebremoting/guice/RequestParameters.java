package org.directwebremoting.guice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

import static java.lang.annotation.RetentionPolicy.*;

/**
 * Marks fields or parameters of type {@code Map<String, String[]>}
 * that are to be injected with the parameter map of the current
 * HTTP request.
 * @author Tim Peierls [tim at peierls dot net]
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
public @interface RequestParameters
{
}
