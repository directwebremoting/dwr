package org.directwebremoting.guice.util;

import java.util.Collection;
import java.util.List;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * A scope that looks up providers in a current context, using itself as a
 * provider for the context.
 * @author Tim Peierls [tim at peierls dot net]
 */
public interface ContextScope<C> extends Scope, Provider<C>
{
    /**
     * Returns a provider that finds the instance registry corresponding
     * to the current context and returns the object registered with
     * the given key, creating it if it doesn't exist in the registry.
     */
    <T> Provider<T> scope(final Key<T> key, final Provider<T> creator);

    /**
     * The context identifier used to look up an instance registry.
     * The value returned is a function of the current context.
     */
    C get();

    /**
     * The type of object used as a context identifier.
     */
    Class<C> type();

    /**
     * The keys bound in this scope.
     */
    List<Key<?>> getKeysInScope();

    /**
     * The context identifiers of all open contexts that this
     * scope knows about.
     */
    Collection<C> getOpenContexts();

    /**
     * Closes the given context.
     */
    void close(C context, ContextCloseHandler<?>... closeHandlers);

    /**
     * Closes all open contexts.
     */
    void closeAll(ContextCloseHandler<?>... closeHandlers);
}
