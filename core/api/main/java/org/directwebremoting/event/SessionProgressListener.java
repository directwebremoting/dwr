package org.directwebremoting.event;

import java.io.Serializable;

import org.apache.commons.fileupload.ProgressListener;

/**
 * Progress listener that stores results in the user session.
 * @author Jose Noheda
 */
public class SessionProgressListener implements ProgressListener, Serializable
{
    /**
     * Accessor for the number of bytes read, as passed in by
     * {@link ProgressListener#update(long, long, int)}
     */
    public long getBytesRead()
    {
        return bytesRead;
    }

    /**
     * The total number of bytes, which are being read. May be -1, if this
     * number is unknown. Passed in by
     * {@link ProgressListener#update(long, long, int)}
     */
    public long getContentLength()
    {
        return contentLength;
    }

    /**
     * The number of the field, which is currently being read. (0 = no item so
     * far, 1 = first item is being read, ...)
     */
    public long getItem()
    {
        return item;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.fileupload.ProgressListener#update(long, long, int)
     */
    public void update(long newBytesRead, long newContentLength, int items)
    {
        if (cancelled)
        {
            this.bytesRead = this.contentLength + 1;
            throw new RuntimeException("User cancelled the upload.");
        }
        this.bytesRead = newBytesRead;
        this.contentLength = newContentLength;
        this.item = items;
    }

    /**
     * Cancels the associated file upload.
     */
    public void cancel()
    {
        cancelled = true;
    }

    /**
     * Have we been cancelled?
     */
    private volatile boolean cancelled = false;

    /**
     * @see SessionProgressListener#getBytesRead()
     */
    private volatile long bytesRead = 0L;

    /**
     * @see #getContentLength()
     */
    private volatile long contentLength = 0L;

    /**
     * @see #getItem()
     */
    private volatile long item = 0L;
}
