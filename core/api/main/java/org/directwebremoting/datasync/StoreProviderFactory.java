package org.directwebremoting.datasync;

import javax.servlet.http.HttpSession;

import org.directwebremoting.ScriptSession;

/**
 * The PerXStoreProviders (like {@link PerHttpSessionStoreProvider} and
 * {@link PerScriptSessionStoreProvider}) need a way to create
 * {@link StoreProvider}s for a given Y (in the above example Y is either
 * {@link HttpSession} or {@link ScriptSession}).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface StoreProviderFactory<X, Y>
{
    /**
     * Create a new StoreProvider for a given Y.
     * For container classes like HttpSession etc, this method does NOT need
     * to store the created object. That is done by the PerXStoreProvider
     * implementation.
     */
    StoreProvider<X> create(Y y);
}
