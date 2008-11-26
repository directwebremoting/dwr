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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.InitializingBean;
import org.directwebremoting.io.FileTransfer;
import org.directwebremoting.io.InputStreamFactory;
import org.directwebremoting.io.OutputStreamLoader;
import org.directwebremoting.util.LocalUtil;

/**
 * A {@link DownloadManager} that stores the files on disk.
 * This implementation has the advantage that does not require large amounts of
 * memory, however some writable disk must be available, and
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FileStoreDownloadManager extends PurgingDownloadManager implements DownloadManager, InitializingBean
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.InitializingBean#afterContainerSetup(org.directwebremoting.Container)
     */
    public void afterContainerSetup(Container container)
    {
        if (downloadFileCache == null)
        {
            File tempFile = null;
            OutputStream out = null;

            try
            {
                tempFile = File.createTempFile("dwr-test", ".tmp");
                out = new FileOutputStream(tempFile);
                out.write("test".getBytes());

                // Get the temp directory from the location of the temp file
                downloadFileCache = tempFile.getParentFile();
                if (downloadFileCache == null)
                {
                    throw new IllegalArgumentException("Temp files written to null directory");
                }
            }
            catch (IOException ex)
            {
                throw new IllegalArgumentException("Temp directory provided by the JVM is not writable. See downloadFileCacheDir to customize.");
            }
            finally
            {
                LocalUtil.close(out);
                if (tempFile != null)
                {
                    tempFile.delete();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#putFileTransfer(java.lang.String, org.directwebremoting.io.FileTransfer)
     */
    @Override
    protected void putFileTransfer(String id, FileTransfer transfer)
    {
        String filename = FILE_PREFIX
                        + PART_SEPARATOR
                        + id
                        + PART_SEPARATOR
                        + transfer.getMimeType().replace("/", ".")
                        + PART_SEPARATOR
                        + transfer.getFilename();

        OutputStream out = null;
        OutputStreamLoader loader = null;
        try
        {
            out = new FileOutputStream(filename);
            loader = transfer.getOutputStreamLoader();
            loader.load(out);
        }
        catch (IOException ex)
        {
            log.error("Failed to write file to cache", ex);
        }
        finally
        {
            LocalUtil.close(loader);
            LocalUtil.close(out);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#getFileTransfer(java.lang.String)
     */
    public FileTransfer getFileTransfer(String id) throws FileNotFoundException
    {
        final String prefix = FILE_PREFIX + PART_SEPARATOR + id + PART_SEPARATOR;

        final File[] match = downloadFileCache.listFiles(new FileFilter()
        {
            /* (non-Javadoc)
             * @see java.io.FileFilter#accept(java.io.File, java.lang.String)
             */
            public boolean accept(File file)
            {
                return file.getName().startsWith(prefix);
            }
        });

        if (match.length == 0)
        {
            return null;
        }

        if (match.length > 1)
        {
            log.warn("More than 1 match for prefix: " + prefix + ". Using first.");
        }

        final File matched = match[0];
        String[] parts = matched.getName().split(PART_SEPARATOR, 4);

        // Parts 0 and 1 are the prefix and id. We know they're right
        String mimeType = parts[2].replace(".", "/");
        String filename = parts[3];
        final InputStream in = new FileInputStream(matched);

        return new FileTransfer(filename, mimeType, -1, new InputStreamFactory()
        {
            public InputStream getInputStream() throws IOException
            {
                return in;
            }

            public void close() throws IOException
            {
                LocalUtil.close(in);

                // Some download managers cause multiple downloads. Since
                // space is less likely to be a problem on a disk, we wait
                // until the purge process to run rather than deleting now.
                //matched.delete();
            }
        });
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#purge()
     */
    @Override
    protected void purge()
    {
        final long now = System.currentTimeMillis();

        final File[] match = downloadFileCache.listFiles(new FileFilter()
        {
            /* (non-Javadoc)
             * @see java.io.FileFilter#accept(java.io.File)
             */
            public boolean accept(File file)
            {
                boolean nameMatch = file.getName().startsWith(FILE_PREFIX);
                boolean oldEnough = now > file.lastModified() + purgeDownloadsAfter;
                return nameMatch && oldEnough;
            }
        });

        for (File file : match)
        {
            file.delete();
        }
    }

    /**
     * Set the directory to which we write the downloaded file cache
     * @param downloadFileCacheDir the downloadFileCache to set
     */
    public void setDownloadFileCacheDir(String downloadFileCacheDir)
    {
        downloadFileCache = new File(downloadFileCacheDir);

        if (!downloadFileCache.exists())
        {
            throw new IllegalArgumentException("Download cache does not exist: " + downloadFileCacheDir);
        }

        if (!downloadFileCache.isDirectory())
        {
            throw new IllegalArgumentException("Download cache is not a directory: " + downloadFileCacheDir);
        }
    }

    /**
     * The prefix for all temp files that we save
     */
    private static final String FILE_PREFIX = "dwr";

    /**
     * The separator to distinguish the prefix, from the id, the mime-type and
     * the filename
     */
    private static final String PART_SEPARATOR = "-";

    /**
     * The lock which you must hold to read or write from the list of
     * {@link FileTransfer}s.
     */
    protected Object contentsLock = new Object();

    /**
     * The directory in which we store temp files.
     */
    protected File downloadFileCache = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(FileStoreDownloadManager.class);
}
