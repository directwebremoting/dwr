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
package org.directwebremoting.extend;

import java.io.IOException;
import java.io.InputStream;

import org.directwebremoting.io.InputStreamFactory;

/**
 * The result of a DWR query is normally a set of name/value pairs unless we are
 * doing file-upload in which case there is more information with each field.
 * This class replaces the value part of the set of name/value pairs to
 * provide access to the extra information.
 * @author Lance Semmens [uklance at gmail dot com]
 * @author Niklas Johansson [niklas dot json at gmail dot com]
 */
public class FormField
{
    /**
     * Standard ctor for the normal non file-upload case
     * @param value The string value
     */
    public FormField(String value)
    {
        this.name = null;
        this.mimeType = null;
        this.file = false;
        this.string = value;
        this.inputStreamFactory = null;
    }

    /**
     * Ctor for when we are in the special file-upload case
     * @param name The file name
     * @param mimeType The mime type sent by the browser
     * @param fileSize The size of the file sent by the browser
     * @param inFactory FActory for the input stream sent by the browser
     */
    public FormField(String name, String mimeType, long fileSize, InputStreamFactory inFactory)
    {
        this.name = name;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.file = true;
        this.string = null;
        this.inputStreamFactory = inFactory;
    }

    /**
     * Returns the content type passed by the browser or null if not defined.
     * @return The content type passed by the browser or null if not defined.
     */
    public String getMimeType()
    {
        return mimeType;
    }

    /**
     * Returns the size of the file.
     * @return The size of the file.
     */
    public long getFileSize() {
        if (this.file) {
            return fileSize;
        }
        throw new UnsupportedOperationException();
    }

    /**
     * Returns an InputStream that can be used to retrieve the contents of the file.
     * @return An InputStream that can be used to retrieve the contents of the file.
     */
    public InputStream getInputStream() throws IOException {
        if (inputStreamFactory != null) {
            return inputStreamFactory.getInputStream();
        }
        throw new UnsupportedOperationException();
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
     * Returns the contents of the file item as a String.
     */
    public String getString()
    {
        if (file == true) {
            throw new UnsupportedOperationException("Get string not available for files");
        }
        return string;
    }

    /**
     * Determines whether or not a FormField instance represents a simple form
     * field.
     * @return true for an uploaded file; false for a simple form field.
     */
    public boolean isFile()
    {
        return file;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (file)
        {
            return super.toString();
        }
        else
        {
            return "FormField: " + getString();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hash;
        if (file)
        {
            hash = super.hashCode();
        }
        else
        {
            hash = string == null ? 0 : string.hashCode();
        }
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this.file)
        {
            return super.equals(obj);
        }
        else if (obj instanceof FormField)
        {
            if (obj == this)
            {
                return true;
            }
            FormField that = (FormField) obj;

            if (that.file)
            {
                return false;
            }

            // both non-files (strings)
            return equals(this.string, that.string);
        }
        return false;
    }

    private boolean equals(Object o1, Object o2)
    {
        if (o1 == null)
        {
            return o2 == null;
        }
        return o1.equals(o2);
    }

    private final boolean file;
    private long fileSize;
    private final String name;
    private final String mimeType;
    private final String string;
    private final InputStreamFactory inputStreamFactory;
}

