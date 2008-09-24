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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.FileGenerator;

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
     * @see org.directwebremoting.impl.PurgingDownloadManager#putFileGenerator(java.lang.String, org.directwebremoting.extend.DownloadManager.FileGenerator)
     */
    @Override
    protected void putFileGenerator(String id, FileGenerator generator)
    {
        synchronized (contentsLock)
        {
            contents.put(id, new TimedFileGenerator(generator));
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#getFileGenerator(java.lang.String)
     */
    @Override
    protected FileGenerator getFileGenerator(String id)
    {
        synchronized (contentsLock)
        {
            TimedFileGenerator generator = contents.get(id);
            if (generator == null)
            {
                return null;
            }
            generator.downloadRequests++;
            if (generator.downloadRequests >= this.downloadRequestsBeforeRemove)
            {
                contents.remove(id);
            }
            return generator.fileGenerator;
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
            for (Iterator<Map.Entry<String, TimedFileGenerator>> it = contents.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry<String, TimedFileGenerator> entry = it.next();

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
     * A union of a FileGenerator and the time it was inserted into this manager
     */
    protected static class TimedFileGenerator
    {
        protected TimedFileGenerator(FileGenerator fileGenerator)
        {
            this.fileGenerator = fileGenerator;
            this.timeInserted = System.currentTimeMillis();
        }

        protected final FileGenerator fileGenerator;
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
     * {@link FileGenerator}s.
     */
    protected final Object contentsLock = new Object();

    /**
     * The list of files in the system
     */
    protected final Map<String, TimedFileGenerator> contents = Collections.synchronizedMap(new HashMap<String, TimedFileGenerator>());
}
