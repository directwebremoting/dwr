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
package org.directwebremoting.util;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * A PrintWriter that also sends its output to a log stream
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DebuggingPrintWriter extends PrintWriter
{
    /**
     * Create a new PrintWriter, without automatic line flushing.
     * @param prefix A tag to prefix lines with for debugging purposes
     * @param out A character-output stream
     */
    public DebuggingPrintWriter(String prefix, Writer out)
    {
        super(out, false);
        this.prefix = prefix;
    }

    /**
     * Create a new PrintWriter.
     * @param prefix A tag to prefix lines with for debugging purposes
     * @param out A character-output stream
     * @param autoFlush A boolean; if true, the println() methods will flush the output buffer
     */
    public DebuggingPrintWriter(String prefix, Writer out, boolean autoFlush)
    {
        super(out, autoFlush);
        this.prefix = prefix;
    }

    /**
     * Create a new PrintWriter, without automatic line flushing, from an
     * existing OutputStream.  This convenience constructor creates the
     * necessary intermediate OutputStreamWriter, which will convert characters
     * into bytes using the default character encoding.
     * @param prefix A tag to prefix lines with for debugging purposes
     * @param out An output stream
     * @see java.io.OutputStreamWriter#OutputStreamWriter(java.io.OutputStream)
     */
    public DebuggingPrintWriter(String prefix, OutputStream out)
    {
        super(out, false);
        this.prefix = prefix;
    }

    /**
     * Create a new PrintWriter from an existing OutputStream.  This convenience
     * constructor creates the necessary intermediate OutputStreamWriter, which
     * will convert characters into bytes using the default character encoding.
     * @param prefix A tag to prefix lines with for debugging purposes
     * @param out An output stream
     * @param autoFlush A boolean; if true, the println() methods will flush the output buffer
     * @see java.io.OutputStreamWriter#OutputStreamWriter(java.io.OutputStream)
     */
    public DebuggingPrintWriter(String prefix, OutputStream out, boolean autoFlush)
    {
        super(new BufferedWriter(new OutputStreamWriter(out)), autoFlush);
        this.prefix = prefix;
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(boolean)
     */
    public void print(boolean x)
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(char)
     */
    public void print(char x)
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(int)
     */
    public void print(int x)
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(long)
     */
    public void print(long x)
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(float)
     */
    public void print(float x)
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(double)
     */
    public void print(double x)
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(char[])
     */
    public void print(char x[])
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(java.lang.String)
     */
    public void print(String x)
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#print(java.lang.Object)
     */
    public void print(Object x)
    {
        super.print(x);
        buffer.append(x);
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println()
     */
    public void println()
    {
        synchronized (lock)
        {
            printBuffer();
            super.println();
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(boolean)
     */
    public void println(boolean x)
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(char)
     */
    public void println(char x)
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(int)
     */
    public void println(int x)
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(long)
     */
    public void println(long x)
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(float)
     */
    public void println(float x)
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(double)
     */
    public void println(double x)
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(char[])
     */
    public void println(char x[])
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(java.lang.String)
     */
    public void println(String x)
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /* (non-Javadoc)
     * @see java.io.PrintWriter#println(java.lang.Object)
     */
    public void println(Object x)
    {
        synchronized (lock)
        {
            printBuffer();
            super.println(x);
        }
    }

    /**
     * Write the characters in the print buffer out to the stream
     */
    private void printBuffer()
    {
        if (buffer.length() > 0)
        {
            log.debug(prefix + buffer.toString());
            buffer.setLength(0);
        }
    }

    /**
     * How to we prefix all the debugging lines?
     * @return the prefix
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * How to we prefix all the debugging lines?
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    /**
     * How to we prefix all the debugging lines?
     */
    private String prefix;

    /**
     * A buffer where we store stuff before a newline
     */
    protected final StringBuffer buffer = new StringBuffer();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DebuggingPrintWriter.class);
}
