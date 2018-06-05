package org.directwebremoting.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class gives some lifecycle to an OutputStream.
 * There may be times (particularly with downloading files to browsers) when it
 * is not clear if the data will ever be read.
 * A call to {@link #load(OutputStream)} is a signal that we really to want data
 * <strong>now</strong> and that if any processing is needed, if should be done.
 * A call to {@link #close()} is a signal that whether or not getInputStream was
 * called, the data is no longer required.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface OutputStreamLoader
{
    /**
     * Write all the data to the given output stream.
     */
    public void load(OutputStream out) throws IOException;

    /**
     * This method indicates that whether or not this resource has been read,
     * it is not longer required.
     * Must be called by whatever calls {@link #load(OutputStream)} when it has
     * finished reading from the stream, or when it has decided that it never
     * will call {@link #load(OutputStream)}.
     */
    public void close() throws IOException;
}
