package org.directwebremoting.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.directwebremoting.extend.InputStreamFactoryOutputStreamLoader;
import org.directwebremoting.extend.OutputStreamLoaderInputStreamFactory;
import org.directwebremoting.extend.SimpleInputStreamFactory;
import org.directwebremoting.util.CopyUtils;

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
     * @param filename The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param outputStreamLoader A means by which the data can be read
     */
    public FileTransfer(String filename, String mimeType, OutputStreamLoader outputStreamLoader)
    {
        this.filename = filename;
        this.mimeType = mimeType;
        this.outputStreamLoader = outputStreamLoader;
        this.size = -1;
        this.inputStreamFactory = null;
    }

    /**
     * A ctor for the 3 things browsers tell us about the uploaded file
     */
    public FileTransfer(BufferedImage image, String type)
    {
        this(image, "image", type);
    }

    /**
     * A ctor for the 3 things browsers tell us about the uploaded file
     */
    public FileTransfer(final BufferedImage image, String filename, final String type)
    {
        this.filename = filename;
        this.mimeType = "image/" + type;
        this.outputStreamLoader = new OutputStreamLoader()
        {
            public void load(OutputStream out) throws IOException
            {
                ImageIO.write(image, type, out);
            }

            public void close() throws IOException
            {
            }
        };
        this.size = -1;
        this.inputStreamFactory = null;
    }

    /**
     * A ctor for the 3 things browsers tell us about the uploaded file
     * @param filename The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param bytes the raw data
     */
    public FileTransfer(String filename, String mimeType, final byte[] bytes)
    {
        this.filename = filename;
        this.mimeType = mimeType;
        this.size = bytes.length;
        this.outputStreamLoader = new OutputStreamLoader()
        {
            public void load(OutputStream out) throws IOException
            {
                CopyUtils.copy(bytes, out);
            }

            public void close() throws IOException
            {
            }
        };
        this.inputStreamFactory = null;
    }

    /**
     * A ctor for InputStreamFactory + all other details
     * @param filename The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param size The size of the file
     * @param inputStreamFactory A means by which the data can be read
     */
    public FileTransfer(String filename, String mimeType, long size, final InputStreamFactory inputStreamFactory)
    {
        this.filename = filename;
        this.mimeType = mimeType;
        this.size = size;
        this.outputStreamLoader = null;
        this.inputStreamFactory = inputStreamFactory;
    }

    /**
     * A ctor for InputStream + all other details
     * @param filename The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param size The size of the file
     * @param in A means by which the data can be read.
     */
    public FileTransfer(String filename, String mimeType, long size, final InputStream in)
    {
        this.filename = filename;
        this.mimeType = mimeType;
        this.size = size;
        this.outputStreamLoader = null;
        this.inputStreamFactory = new SimpleInputStreamFactory(in, true);
    }

    /**
     * A ctor for the 3 things browsers tell us about the uploaded file
     * @param filename The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param inputStreamFactory A means by which the data can be read
     */
    public FileTransfer(String filename, String mimeType, final InputStreamFactory inputStreamFactory)
    {
        this.filename = filename;
        this.mimeType = mimeType;
        this.size = -1;
        this.outputStreamLoader = null;
        this.inputStreamFactory = inputStreamFactory;
    }

    /**
     * A ctor for the 3 things browsers tell us about the uploaded file
     * @param filename The remote source filename
     * @param mimeType The mime type passed in by the browser
     * @param in A means by which the data can be read.
     */
    public FileTransfer(String filename, String mimeType, final InputStream in)
    {
        this.filename = filename;
        this.mimeType = mimeType;
        this.size = -1;
        this.outputStreamLoader = null;
        this.inputStreamFactory = new SimpleInputStreamFactory(in, true);
    }

    /**
     * Returns the original filename in the client's file-system, as provided by
     * the browser (or other client software).
     * In most cases, this will be the base file name, without path information.
     * However, some clients, such as the Opera browser, do include path
     * information.
     * @return The original filename in the client's file-system.
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * Returns the content type passed by the browser or null if not defined.
     */
    public String getMimeType()
    {
        return mimeType;
    }

    /**
     * Returns the size of the file passed by the browser or -1 if this is not
     * known.
     */
    public long getSize()
    {
        return size;
    }

    /**
     * Returns an OutputStreamLoader that can be used to retrieve the contents
     * of the file.
     */
    public OutputStreamLoader getOutputStreamLoader()
    {
        if (outputStreamLoader != null)
        {
            return outputStreamLoader;
        }
        else
        {
            return new InputStreamFactoryOutputStreamLoader(inputStreamFactory);
        }
    }

    /**
     * Returns an {@link InputStream} that can be used to retrieve the contents
     * of the file.
     * {@link InputStreamFactory} is used place of direct access to an
     * {@link InputStream} to ensure that resources are properly closed.
     */
    public InputStream getInputStream() throws IOException
    {
        if (inputStreamFactory != null)
        {
            return inputStreamFactory.getInputStream();
        }
        else
        {
            return new OutputStreamLoaderInputStreamFactory(outputStreamLoader).getInputStream();
        }
    }

    /**
     * The remote source filename
     */
    private final String filename;

    /**
     * The mime type passed in by the browser
     */
    private final String mimeType;

    /**
     * The size of the file
     */
    private final long size;

    /**
     * One of 2 means by which the data can be obtained
     * @see #inputStreamFactory
     */
    private final OutputStreamLoader outputStreamLoader;

    /**
     * One of 2 means by which the data can be obtained
     * @see #outputStreamLoader
     */
    private final InputStreamFactory inputStreamFactory;
}
