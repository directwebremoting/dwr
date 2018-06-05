package org.directwebremoting.guice.util;

/**
 * Called for each instance in a context when the context is closed.
 * @author Tim Peierls [tim at peierls dot net]
 */
public interface ContextCloseHandler<T>
{
    /**
     * Action to take when the context containing {@code object} is closed.
     */
    void close(T object) throws Exception;

    /**
     * The type of objects handled by this handler.
     */
    Class<T> type();
}
