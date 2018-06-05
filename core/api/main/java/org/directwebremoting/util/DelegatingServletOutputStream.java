package org.directwebremoting.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

/**
 * Delegating implementation of ServletOutputStream.
 */
public class DelegatingServletOutputStream extends ServletOutputStream
{
    /**
     * Create a new DelegatingServletOutputStream.
     * @param proxy the target OutputStream
     */
    public DelegatingServletOutputStream(OutputStream proxy)
    {
        this.proxy = proxy;
    }

    /**
     * Accessor for the stream that we are proxying to
     * @return The stream we proxy to
     */
    public OutputStream getTargetStream()
    {
        return proxy;
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException
    {
        proxy.write(b);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException
    {
        super.flush();
        proxy.flush();
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        super.close();
        proxy.close();
    }

    private final OutputStream proxy;
}
