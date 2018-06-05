package org.directwebremoting.guice.util;

import java.io.Closeable;
import java.util.List;

/**
 * @author Tim Peierls
 */
public class ContextCloseHandlers
{
    public static ContextCloseHandler<Closeable> newExceptionLoggingCloseableHandler(List<Exception> exceptions)
    {
        return new ExceptionLoggingCloseableHandler(exceptions);
    }

    private ContextCloseHandlers()
    {
        throw new AssertionError("uninstantiable");
    }
}
