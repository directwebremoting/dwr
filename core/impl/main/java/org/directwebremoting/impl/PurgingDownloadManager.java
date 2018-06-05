package org.directwebremoting.impl;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.IdGenerator;
import org.directwebremoting.io.FileTransfer;

/**
 * A {@link DownloadManager} that simply stores downloads in memory until they
 * are requested and then removes them.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class PurgingDownloadManager implements DownloadManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.DownloadManager#addFile(org.directwebremoting.extend.DownloadManager.FileGenerator)
     */
    public String addFileTransfer(FileTransfer generator) throws IOException
    {
        String id = idGenerator.generate();
        putFileTransfer(id, generator);

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        return "'" + request.getContextPath() + request.getServletPath() + downloadHandlerUrl + id + "'";
    }

    /**
     * The URL part which we attach to the downloads.
     * @param downloadHandlerUrl The URL for this Handler.
     */
    public void setDownloadHandlerUrl(String downloadHandlerUrl)
    {
        this.downloadHandlerUrl = downloadHandlerUrl;
    }

    /**
     * @param purgeDownloadsAfter the purgeDownloadsAfter to set
     */
    public void setPurgeDownloadsAfter(long purgeDownloadsAfter)
    {
        this.purgeDownloadsAfter = purgeDownloadsAfter;
    }

    /**
     * @param executor The DWR provided shutdown system
     */
    public void setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor executor)
    {
        this.executor = executor;
        this.executor.scheduleWithFixedDelay(downloadPurge, queueSleepTime, queueSleepTime, TimeUnit.MILLISECONDS);
    }

    /**
     * @param idGenerator The configured ID generator
     */
    public void setIdGenerator(IdGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }

    /**
     * Store a {@link FileTransfer} against a given id for later retrieval.
     * @param id The id of the given FileGenerator
     * @param generator The FileGenerator to store against the id
     */
    protected abstract void putFileTransfer(String id, FileTransfer generator);

    /**
     * Remove all the stale entries from the cache
     */
    protected abstract void purge();

    /**
     * Loop over the known downloads removing the ones that are out of date
     */
    public class DownloadPurge implements Runnable
    {
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
            purge();
        }
    }

    /**
     * The cron system
     */
    protected ScheduledThreadPoolExecutor executor = null;

    /**
     * Check that the cache of files does not contain out-of-date items
     */
    protected DownloadPurge downloadPurge = new DownloadPurge();

    /**
     * How often do we run purges on the data?
     */
    protected int queueSleepTime = 15000;

    /**
     * After some time we will give up waiting for a file to be downloaded.
     * By default this is 2 minutes. After this time we expect that the user
     * is no longer waiting and has gone away.
     */
    protected long purgeDownloadsAfter = 120000;

    /**
     * The URL part which we attach to the downloads.
     */
    protected String downloadHandlerUrl;

    /**
     * Unique id so we can retrieve downloads when asked
     */
    protected IdGenerator idGenerator;
}
