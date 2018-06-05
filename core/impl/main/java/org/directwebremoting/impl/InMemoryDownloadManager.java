package org.directwebremoting.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.io.FileTransfer;

/**
 * A {@link DownloadManager} that simply stores the links in-memory.
 * This implementation has the advantage that it is simple - no disk storage is
 * required, however in anything but a lightly used system it could cause
 * significant memory usage.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class InMemoryDownloadManager extends PurgingDownloadManager implements DownloadManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#putFileTransfer(java.lang.String, org.directwebremoting.extend.DownloadManager.FileTransfer)
     */
    @Override
    protected void putFileTransfer(String id, FileTransfer transfer)
    {
        synchronized (contentsLock)
        {
            contents.put(id, new TimedFileTransfer(transfer));
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#getFileTransfer(java.lang.String)
     */
    public FileTransfer getFileTransfer(String id)
    {
        synchronized (contentsLock)
        {
            TimedFileTransfer transfer = contents.get(id);
            if (transfer == null)
            {
                return null;
            }
            transfer.downloadRequests++;
            if (transfer.downloadRequests >= this.downloadRequestsBeforeRemove)
            {
                contents.remove(id);
            }
            return transfer.fileTransfer;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#purge()
     */
    @Override
    protected void purge()
    {
        long now = System.currentTimeMillis();

        synchronized (contentsLock)
        {
            for (Iterator<Map.Entry<String, TimedFileTransfer>> it = contents.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry<String, TimedFileTransfer> entry = it.next();

                try
                {
                    if (now > entry.getValue().timeInserted + purgeDownloadsAfter)
                    {
                        it.remove();
                    }
                }
                catch (Exception ex)
                {
                    log.warn("Deletion queue processing error: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * A union of a FileTransfer and the time it was inserted into this manager
     */
    protected static class TimedFileTransfer
    {
        protected TimedFileTransfer(FileTransfer fileTransfer)
        {
            this.fileTransfer = fileTransfer;
            this.timeInserted = System.currentTimeMillis();
        }

        protected final FileTransfer fileTransfer;
        protected final long timeInserted;
        protected int downloadRequests = 0;
    }

    /**
     * Some download managers cause multiple downloads, so we count the number
     * of downloads before the delete is done.
     */
    public void setDownloadRequestsBeforeRemove(int downloadRequestsBeforeRemove)
    {
        this.downloadRequestsBeforeRemove = downloadRequestsBeforeRemove;
    }

    /**
     * @see #setDownloadRequestsBeforeRemove(int)
     */
    protected int downloadRequestsBeforeRemove = 1;

    /**
     * The lock which you must hold to read or write from the list of
     * {@link FileTransfer}s.
     */
    protected final Object contentsLock = new Object();

    /**
     * The list of files in the system
     */
    protected final Map<String, TimedFileTransfer> contents = Collections.synchronizedMap(new HashMap<String, TimedFileTransfer>());

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(InMemoryDownloadManager.class);
}
