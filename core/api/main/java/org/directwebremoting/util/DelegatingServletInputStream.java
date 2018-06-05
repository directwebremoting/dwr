package org.directwebremoting.util;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

/**
 * Delegating implementation of ServletInputStream.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DelegatingServletInputStream extends ServletInputStream
{
    /**
     * Create a new DelegatingServletInputStream.
     * @param proxy the sourceStream InputStream
     */
    public DelegatingServletInputStream(InputStream proxy)
    {
        this.proxy = proxy;
    }

    /**
     * Accessor for the stream that we are proxying to
     * @return The stream we proxy to
     */
    public InputStream getTargetStream()
    {
        return proxy;
    }

    /**
     * @return The stream that we proxy to
     */
    public InputStream getSourceStream()
    {
        return proxy;
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        return proxy.read();
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        super.close();
        proxy.close();
    }

    private final InputStream proxy;
}
