package org.directwebremoting.io;

import java.io.IOException;
import java.io.InputStream;

import org.directwebremoting.extend.SimpleInputStreamFactory;

/**
 * This class gives some lifecycle to an InputStream.
 * There may be times (particularly with downloading files to browsers) when it
 * is not clear if the data will ever be read.
 * A call to {@link #getInputStream()} is a signal that we really to want data
 * <strong>now</strong> and that if any processing is needed, if should be done.
 * A call to {@link #close()} is a signal that whether or not getInputStream was
 * called, the data is no longer required.
 * {@link SimpleInputStreamFactory}.
 * @see SimpleInputStreamFactory
 * @author Lance Semmens [uklance at gmail dot com]
 */
public interface InputStreamFactory
{
    /**
     * Gets an input stream. This function should be called only once.
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Whether or not this resource has been read from, this should be called
     * to indicate that the resources are no longer required.
     */
    public void close() throws IOException;
}
