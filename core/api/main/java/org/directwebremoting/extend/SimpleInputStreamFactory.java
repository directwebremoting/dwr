package org.directwebremoting.extend;

import java.io.IOException;
import java.io.InputStream;

import org.directwebremoting.io.InputStreamFactory;

/**
 * SimpleInputStreamFactory is just an InputStreamFactory that holds an
 * {@link InputStream} and can close it at the right time (if required)
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SimpleInputStreamFactory implements InputStreamFactory
{
    /**
     *
     */
    public SimpleInputStreamFactory(InputStream in)
    {
        this.in = in;
        this.autoClose = true;
    }

    /**
     *
     */
    public SimpleInputStreamFactory(InputStream in, boolean autoClose)
    {
        this.in = in;
        this.autoClose = autoClose;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.InputStreamFactory#getInputStream()
     */
    public InputStream getInputStream() throws IOException
    {
        return in;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.InputStreamFactory#readFinished()
     */
    public void close() throws IOException
    {
        if (autoClose)
        {
            in.close();
        }
    }

    /**
     * Should we close the stream when {@link #close()} is called?
     */
    private final boolean autoClose;

    /**
     * The stream that we provide when {@link #getInputStream()} is called
     */
    private final InputStream in;
}
