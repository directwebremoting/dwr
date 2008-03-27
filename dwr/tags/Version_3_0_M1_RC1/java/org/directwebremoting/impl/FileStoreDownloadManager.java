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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.directwebremoting.Container;
import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.FileGenerator;
import org.directwebremoting.extend.InitializingBean;
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
     * @see org.directwebremoting.impl.PurgingDownloadManager#putFileGenerator(java.lang.String, org.directwebremoting.extend.DownloadManager.FileGenerator)
     */
    @Override
    protected void putFileGenerator(String id, FileGenerator generator)
    {
        OutputStream out = null;
        String mimeType = generator.getMimeType();
        String filename = "dwr-cache-" + id + "-" + mimeType.replace("/", ".");

        try
        {
            out = new FileOutputStream(filename);
            generator.generateFile(out);
            out.close();
        }
        catch (IOException ex)
        {
            log.error("Failed to write file to cache", ex);
        }
        finally
        {
            LocalUtil.close(out);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#getFileGenerator(java.lang.String)
     */
    @Override
    protected FileGenerator getFileGenerator(String id)
    {
        final String prefix = "dwr-cache-" + id + "-";
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

        return new FileGenerator()
        {
            /* (non-Javadoc)
             * @see org.directwebremoting.extend.DownloadManager.FileGenerator#getMimeType()
             */
            public String getMimeType()
            {
                String leafname = match[0].getName();
                String suffix = leafname.substring(prefix.length());
                String mimeType = suffix.replace(".", "/");
                return mimeType;
            }
        
            /* (non-Javadoc)
             * @see org.directwebremoting.extend.DownloadManager.FileGenerator#generateFile(java.io.OutputStream)
             */
            public void generateFile(OutputStream out) throws IOException
            {
                InputStream in = null;
                try
                {
                    in = new FileInputStream(match[0]);
                    byte[] buffer = new byte[1024];

                    while (true)
                    {
                        int len = in.read(buffer);
                        if (len == 0)
                        {
                            break;
                        }
                        out.write(buffer, 0, len);
                    }
                }
                finally
                {
                    LocalUtil.close(in);
                    match[0].delete();
                }
            }
        };
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PurgingDownloadManager#purge()
     */
    @Override
    protected void purge()
    {
        final long now = System.currentTimeMillis();

        final String prefix = "dwr-cache-";
        final File[] match = downloadFileCache.listFiles(new FileFilter()
        {
            /* (non-Javadoc)
             * @see java.io.FileFilter#accept(java.io.File)
             */
            public boolean accept(File file)
            {
                boolean nameMatch = file.getName().startsWith(prefix);
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
     * The lock which you must hold to read or write from the list of
     * {@link FileGenerator}s.
     */
    protected Object contentsLock = new Object();

    /**
     * The directory in which we store temp files.
     */
    protected File downloadFileCache = null;
}
