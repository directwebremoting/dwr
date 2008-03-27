package uk.ltd.getahead.dwr.util;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletOutputStream;

/**
 * This is not the evil hack you are looking for.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class WriterOutputStream extends ServletOutputStream
{
    /**
     * ctor using platform default encoding
     * @param writer
     */
    public WriterOutputStream(Writer writer)
    {
        this.writer = writer;
    }

    /**
     * ctor that allows us to specify how strings are created
     * @param writer
     * @param encoding
     */
    public WriterOutputStream(Writer writer, String encoding)
    {
        this.writer = writer;
        this.encoding = encoding;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#print(java.lang.String)
     */
    public void print(String s) throws IOException
    {
        writer.write(s);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(byte[] b) throws IOException
    {
        if (encoding == null)
        {
            writer.write(new String(b));
        }
        else
        {
            writer.write(new String(b, encoding));
        }
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b, int off, int len) throws IOException
    {
        if (encoding == null)
        {
            writer.write(new String(b, off, len));
        }
        else
        {
            writer.write(new String(b, off, len, encoding));
        }
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    public synchronized void write(int b) throws IOException
    {
        buffer[0] = (byte) b;
        write(buffer);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#close()
     */
    public void close() throws IOException
    {
        writer.close();
        writer = null;
        encoding = null;
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    public void flush() throws IOException
    {
        writer.flush();
    }

    /**
     * The destination of all our printing
     */
    private Writer writer;

    /**
     * What string encoding should we use
     */
    private String encoding;

    /**
     * Buffer for write(int)
     */
    private byte[] buffer = new byte[1];
}
