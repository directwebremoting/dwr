/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.impl;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.FileGenerator;
import org.directwebremoting.util.IdGenerator;

/**
 * A {@link DownloadManager} that simply stores downloads in memory until they
 * are requested and then removes them.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class PurgingDownloadManager implements DownloadManager
{
    /**
     * Schedule cache purges
     */
    public PurgingDownloadManager()
    {
        timer.scheduleWithFixedDelay(downloadPurge, queueSleepTime, queueSleepTime, TimeUnit.MILLISECONDS);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.DownloadManager#addFile(org.directwebremoting.extend.DownloadManager.FileGenerator)
     */
    public String addFile(FileGenerator generator) throws IOException
    {
        String id = idGenerator.generateId(16);
        putFileGenerator(id, generator);

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        return "'" + request.getContextPath() + request.getServletPath() + downloadHandlerUrl + id + "'";
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.DownloadManager#getFile(java.lang.String)
     */
    public FileGenerator getFile(String id)
    {
        return getFileGenerator(id);
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
     * Store a {@link FileGenerator} against a given id for
     * later retrieval.
     * @param id The id of the given FileGenerator
     * @param generator The FileGenerator to store against the id
     */
    protected abstract void putFileGenerator(String id, FileGenerator generator);

    /**
     * Retrieve a FileGenerator given the id that it was stored under
     * @param id The id to lookup
     * @return The matching FileGenerator or null if no match was found
     */
    protected abstract FileGenerator getFileGenerator(String id);

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
    protected static ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);

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
    protected IdGenerator idGenerator = new IdGenerator();

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(PurgingDownloadManager.class);
}
