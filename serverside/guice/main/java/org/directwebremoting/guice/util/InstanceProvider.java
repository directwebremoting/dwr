package org.directwebremoting.guice.util;

import com.google.inject.Provider;

/**
 * Marker interface for {@link ContextRegistry} implementations to recognize
 * registered instance providers.
 * @author Tim Peierls [tim at peierls dot net]
 */
public interface InstanceProvider<T> extends Provider<T>, Runnable
{
    void run();
}
