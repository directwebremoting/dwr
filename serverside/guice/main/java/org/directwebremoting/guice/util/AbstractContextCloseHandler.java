package org.directwebremoting.guice.util;

/**
 * A partially implemented handler for objects contained in contexts that
 * are closing. Concrete extensions of this class only have to define
 * {@code close} and have a constructor that calls {@code super(T.class)}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractContextCloseHandler<T> implements ContextCloseHandler<T>
{
    protected AbstractContextCloseHandler(Class<T> type)
    {
        this.type = type;
    }

    public abstract void close(T object) throws Exception;

    public Class<T> type()
    {
        return type;
    }

    private final Class<T> type;
}
