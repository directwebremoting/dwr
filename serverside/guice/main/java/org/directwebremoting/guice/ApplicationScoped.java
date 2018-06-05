package org.directwebremoting.guice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.ScopeAnnotation;

/**
 * Marks classes for which there should be one instance per web application
 * (i.e., per servlet context) and these instances should be created eagerly
 * at servlet {@code init()} and closed (when they implement {@code Closeable})
 * at servlet {@code destroy()}.
 * @author Tim Peierls [tim at peierls dot net]
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface ApplicationScoped
{
}
