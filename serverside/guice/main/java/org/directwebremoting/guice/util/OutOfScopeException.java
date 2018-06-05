package org.directwebremoting.guice.util;

import com.google.inject.Key;
import com.google.inject.Scope;

/**
 * Thrown by Providers returned by
 * {@link com.google.inject.Scope#scope scope(Key, Provider)}
 * when they cannot locate a resource needed to resolve a key.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class OutOfScopeException extends RuntimeException
{
    public OutOfScopeException(Scope scope, Key<?> key, Throwable cause)
    {
        super(String.format(
            "Not in scope %s for key %s: caused by %s",
            scope, key, cause
        ), cause);
    }
}
