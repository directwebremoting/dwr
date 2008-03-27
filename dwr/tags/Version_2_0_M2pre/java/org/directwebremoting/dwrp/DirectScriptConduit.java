package org.directwebremoting.dwrp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.ScriptConduit;

/**
 * A ScriptConduit that works with the parent Marshaller.
 * In some ways this is nasty because it has access to essentially private parts
 * of DwrpPlainJsMarshaller, however there is nowhere sensible to store them
 * within that class, so this is a hacky simplification.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DirectScriptConduit implements ScriptConduit
{
    /**
     * Simple ctor
     * @param out The stream to write to
     * @param response The response to flush on write complete
     * @param marshaller The marshaller that we ask to re-write our Javascript
     */
    public DirectScriptConduit(PrintWriter out, HttpServletResponse response, DwrpPlainJsMarshaller marshaller)
    {
        if (out == null)
        {
            throw new NullPointerException("out=null"); //$NON-NLS-1$
        }

        if (response == null)
        {
            throw new NullPointerException("response=null"); //$NON-NLS-1$
        }

        this.out = out;
        this.response = response;
        this.marshaller = marshaller;
    }

    /**
     * Make sure we don't try to write to a closed conduit. 
     */
    public void close()
    {
        closed = true;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptConduit#addScript(java.lang.String)
     */
    public void addScript(String script) throws IOException
    {
        if (closed)
        {
            throw new IllegalStateException("Attempt to write to closed DirectScriptConduit"); //$NON-NLS-1$
        }

        marshaller.sendScript(out, response, script);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return (int) id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        DirectScriptConduit that = (DirectScriptConduit) obj;

        return this.id == that.id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "DirectScriptConduit[id=" + id + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Are we done with?
     */
    private boolean closed = false;

    /**
     * The PrintWriter to send output to, and that we should synchronize against
     */
    private final PrintWriter out;

    /**
     * Having written to the output we must flush the buffer
     */
    private final HttpServletResponse response;

    /**
     * The marshaller needs to be able to rewrite scripts depending on plain or
     * html modes
     */
    private DwrpPlainJsMarshaller marshaller;

    /**
     * Our ID, to get around serialization issues
     */
    private final long id = nextId++;

    /**
     * The next ID, to get around serialization issues
     */
    private static long nextId = 0L;
}
