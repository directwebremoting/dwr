package org.directwebremoting.guice.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * @author Tim Peierls
 */
class ExceptionLoggingCloseableHandler extends AbstractContextCloseHandler<Closeable>
{
    ExceptionLoggingCloseableHandler(List<Exception> exceptions)
    {
        super(Closeable.class);
        this.exceptions = exceptions;
    }

    @Override
    public void close(Closeable closeable) throws IOException
    {
        try
        {
            closeable.close();
        }
        catch (IOException e)
        {
            exceptions.add(e);
            throw e;
        }
    }

    private final List<Exception> exceptions;
}
