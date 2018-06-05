package org.directwebremoting.guice;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Marks request, response, and session types that should
 * be injected via DWR request, response, and session scopes.
 * @author Tim Peierls [tim at peierls dot net]
 */
@Retention(RUNTIME)
@Target({TYPE})
@BindingAnnotation
@interface Dwr
{
}
