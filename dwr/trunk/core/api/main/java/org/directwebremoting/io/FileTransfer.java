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
package org.directwebremoting.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * One of the 2 ways you can receive uploaded files from a DWR enabled page is
 * to expose a method with a {@link FileTransfer} parameter.
 * The other is to expose a method with an {@link InputStream} parameter.
 * @author Lance Semmens [uklance at gmail dot com]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Niklas Johansson [niklas dot json at gmail dot com]
 */
public class FileTransfer
{
    /**
     * A ctor for the 3 things browsers tell us about the uploaded file
     * @param name The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param outputStreamLoader A means by which the data can be read
     */
    public FileTransfer(String name, String mimeType, OutputStreamLoader outputStreamLoader)
    {
        this.name = name;
        this.mimeType = mimeType;
        this.outputStreamLoader = outputStreamLoader;
        this.size = -1;
    }

    /**
     * A ctor for the 3 things browsers tell us about the uploaded file
     * @param name The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param bytes the raw data
     */
    public FileTransfer(String name, String mimeType, final byte[] bytes)
    {
        this(name, mimeType, bytes.length, new InputStreamFactory()
        {
            public InputStream getInputStream() throws IOException
            {
                return new ByteArrayInputStream(bytes);
            }
        });
    }

    /**
     * A ctor for the 3 things browsers tell us about the uploaded file
     * @param name The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param size The size of the file
     * @param inputStreamFactory A means by which the data can be read
     */
    public FileTransfer(String name, String mimeType, long size, InputStreamFactory inputStreamFactory)
    {
        this.name = name;
        this.mimeType = mimeType;
        this.size = size;
        this.inputStreamFactory = inputStreamFactory;
        this.outputStreamLoader = null;
    }

    /**
     * Returns the content type passed by the browser or null if not defined.
     */
    public String getMimeType()
    {
        return mimeType;
    }

    /**
     * Returns the size of the file passed by the browser or -1 of this is a download transfer.
     */
    public long getSize()
    {
        return size;
    }

    /**
     * Returns an InputStream that can be used to retrieve the contents of the
     * file.
     */
    public InputStream getInputStream() throws IOException
    {
        return inputStreamFactory != null ? inputStreamFactory.getInputStream() : null;
    }

    /**
     * Returns an InputStream that can be used to retrieve the contents of the
     * file.
     */
    public OutputStreamLoader getOutputStreamLoader()
    {
        return outputStreamLoader;
    }

    /**
     * Returns the original filename in the client's file-system, as provided by
     * the browser (or other client software).
     * In most cases, this will be the base file name, without path information.
     * However, some clients, such as the Opera browser, do include path
     * information.
     * @return The original filename in the client's file-system.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The remote source filename
     */
    private final String name;

    /**
     * The mime type passed in by the browser
     */
    private final String mimeType;

    /**
     * The size of the file
     */
    private final long size;

    /**
     * A means by which the data can be read
     */
    private InputStreamFactory inputStreamFactory;

    /**
     * A means by which the data can be written
     */
    private final OutputStreamLoader outputStreamLoader;
}
