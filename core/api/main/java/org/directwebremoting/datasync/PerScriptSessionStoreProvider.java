package org.directwebremoting.datasync;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;

/**
 * A StoreProvider that allows us to have a number of StoreProviders for each
 * {@link ScriptSession}. New {@link StoreProvider}s are created by a
 * {@link StoreProviderFactory} and stored in the ScriptSession under a given
 * attribute name.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PerScriptSessionStoreProvider<T> extends AbstractPerXStoreProvider<T>
{
    public PerScriptSessionStoreProvider(StoreProviderFactory<T, ScriptSession> factory, String attributeName)
    {
        this.factory = factory;
        this.attributeName = attributeName;
    }

    /**
     * Internal method to find or create a StoreProvider for a given user.
     */
    @Override
    protected StoreProvider<T> getStoreProvider()
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();

        @SuppressWarnings("unchecked")
        StoreProvider<T> storeProvider = (StoreProvider<T>) session.getAttribute(attributeName);

        if (storeProvider == null)
        {
            storeProvider = factory.create(session);
            session.setAttribute(attributeName, storeProvider);
        }

        return storeProvider;
    }

    /**
     * The attribute name that we are storing created StoreProviders under in
     * the ScriptSession
     */
    private final String attributeName;

    /**
     * The factory to enable us to create factories per ScriptSession
     */
    private final StoreProviderFactory<T, ScriptSession> factory;
}
