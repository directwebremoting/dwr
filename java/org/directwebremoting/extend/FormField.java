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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * The result of a DWR query is normally a set of name/value pairs unless we are
 * doing file-upload in which case there is more information with each field.
 * This class replaces the value part of the set of name/value pairs to
 * provide access to the extra information.
 * @author Lance Semmens [uklance at gmail dot com]
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
        this.bytes = value.getBytes();
        this.string = value;
    }

    /**
     * Ctor for when we are in the special file-upload case
     * @param name The file name
     * @param mimeType The mime type sent by the browser
     * @param bytes The bytes sent by the browser
     */
    public FormField(String name, String mimeType, byte[] bytes)
    {
        this.name = name;
        this.mimeType = mimeType;
        this.file = true;
        this.bytes = bytes;
        this.string = null;
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
     * Returns an InputStream that can be used to retrieve the contents of the file. 
     * @return An InputStream that can be used to retrieve the contents of the file.
     */
    public InputStream getInputStream()
    {
        return new ByteArrayInputStream(bytes);
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
        if (string == null && bytes != null)
        {
            string = new String(bytes); 
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
            return "FormField: " + ("byteCount=" + (bytes == null ? 0 : bytes.length));
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
        int hash = 0;
        if (file)
        {
            if (mimeType != null)
            {
                hash += mimeType.hashCode();
            }

            if (name != null)
            {
                hash += name.hashCode();
            }
        }

        hash += getString().hashCode();
        return hash;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (!(obj instanceof FormField))
        {
            return false;
        }

        FormField that = (FormField) obj;

        if (this.file != that.file)
        {
            return false;
        }

        if (this.file)
        {
            if (!equals(this.mimeType, that.mimeType))
            {
                return false;
            }

            if (!equals(this.name, that.name))
            {
                return false;
            }
        }

        if (this.bytes.length != that.bytes.length)
        {
            return false;
        }

        for (int i = 0; i < this.bytes.length; ++i)
        {
            if (this.bytes[i] != that.bytes[i])
            {
                return false;
            }
        }

        return true;
    }
    
    private boolean equals(Object o1, Object o2)
    {
        if (o1 == null)
        {
            return o2 == null;
        }
        return o1.equals(o2);
    }

    private boolean file;
    private byte[] bytes;
    private String name;
    private String mimeType;
    private String string;
}

