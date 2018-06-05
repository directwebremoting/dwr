package org.directwebremoting.datasync;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;

/**
 * A StoreProvider that allows us to have a number of StoreProviders for each
 * HttpSession. New {@link StoreProvider}s are created by a
 * {@link StoreProviderFactory} and stored in the HttpSession under a given
 * attribute name.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PerHttpSessionStoreProvider<T> extends AbstractPerXStoreProvider<T>
{
    public PerHttpSessionStoreProvider(StoreProviderFactory<T, HttpSession> factory, String attributeName)
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
        HttpSession session = WebContextFactory.get().getSession(true);

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
     * the HttpSession
     */
    private final String attributeName;

    /**
     * The factory to enable us to create factories per HttpSession
     */
    private final StoreProviderFactory<T, HttpSession> factory;
}
