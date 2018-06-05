package org.directwebremoting.extend;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.io.InputStreamFactory;
import org.directwebremoting.util.CopyUtils;

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
     * @param string The string value
     */
    public FormField(String string)
    {
        this.string = string;

        this.name = null;
        this.mimeType = null;
        this.inputStreamFactory = null;
        this.fileSize = -1;
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
        this.string = null;

        this.name = name;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
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
    public long getFileSize()
    {
        if (string != null)
        {
            return string.length();
        }

        return fileSize;
    }

    /**
     * Returns an InputStream that can be used to retrieve the contents of the file.
     * @return An InputStream that can be used to retrieve the contents of the file.
     */
    public InputStream getInputStream() throws IOException
    {
        if (inputStreamFactory == null)
        {
            throw new UnsupportedOperationException("Can't getInputStream() from a string FormField");
        }

        return inputStreamFactory.getInputStream();
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
        if (name == null)
        {
            throw new UnsupportedOperationException("Can't getName() from a string FormField");
        }

        return name;
    }

    /**
     * Returns the contents of the file item as a String.
     */
    public String getString()
    {
        if (string == null)
        {
            try
            {
                StringWriter buffer = new StringWriter();
                CopyUtils.copy(inputStreamFactory.getInputStream(), buffer);
                return buffer.toString();
            }
            catch (IOException ex)
            {
                log.error("Failed to read input", ex);
                return null;
            }
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
        return inputStreamFactory != null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (string == null)
        {
            return "FormField:File:" + name;
        }
        else
        {
            return "FormField:String:" + string;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        if (string == null)
        {
            return super.hashCode();
        }
        else
        {
            return string.hashCode();
        }
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

        if (obj == null || this.getClass() != obj.getClass())
        {
            return false;
        }

        FormField that = (FormField) obj;

        if (string == null)
        {
            return super.equals(that);
        }
        else
        {
            if (!this.string.equals(that.string))
            {
                return false;
            }
        }

        return true;
    }

    private final String string;

    private final long fileSize;

    private final String name;

    private final String mimeType;

    private final InputStreamFactory inputStreamFactory;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(FormField.class);
}
