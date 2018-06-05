package org.directwebremoting;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.io.StringWrapper;

/**
 * A ScriptBuffer is like a StringBuffer except that it is used to create
 * Javascript commands. There are 2 version of the <code>append()</code> method:
 * <p>The first is {@link #appendScript(String)} which assumes that the
 * parameter is to be inserted literally into the output.
 * <p>The second is {@link #appendData(String)} (and variants for Object and
 * primitive types) which assumes that the parameter is a variable which should
 * be properly converted, escaped and quoted.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ScriptBuffer
{
    /**
     * Create an empty ScriptBuffer.
     */
    public ScriptBuffer()
    {
    }

    /**
     * Create a ScriptBuffer with some initial content.
     * {@link #appendScript(String)} is called with the passed string
     * @param str The initial string to place in the buffer
     */
    public ScriptBuffer(String str)
    {
        appendScript(str);
    }

    /**
     * @param buffer The ScriptBuffer to merge into this script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(java.lang.String)
     */
    public ScriptBuffer appendAll(ScriptBuffer buffer)
    {
        for (Object part : buffer.parts)
        {
            parts.add(part);
        }
        return this;
    }

    /**
     * @param str The String to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(java.lang.String)
     */
    public ScriptBuffer appendScript(String str)
    {
        parts.add(new StringWrapper(str));
        return this;
    }

    /**
     * @param b The boolean to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(boolean)
     */
    public ScriptBuffer appendData(boolean b)
    {
        Boolean data = b ? Boolean.TRUE : Boolean.FALSE;
        parts.add(data);
        return this;
    }

    /**
     * @param c The char to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(char)
     */
    public ScriptBuffer appendData(char c)
    {
        parts.add(c);
        return this;
    }

    /**
     * @param d The double to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(double)
     */
    public ScriptBuffer appendData(double d)
    {
        parts.add(d);
        return this;
    }

    /**
     * @param f The float to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(float)
     */
    public ScriptBuffer appendData(float f)
    {
        parts.add(f);
        return this;
    }

    /**
     * @param i The int to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(int)
     */
    public ScriptBuffer appendData(int i)
    {
        parts.add(i);
        return this;
    }

    /**
     * @param l The long to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(long)
     */
    public ScriptBuffer appendData(long l)
    {
        parts.add(l);
        return this;
    }

    /**
     * @param obj The Object to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(java.lang.Object)
     */
    public ScriptBuffer appendData(Object obj)
    {
        parts.add(obj);
        return this;
    }

    /**
     * @param str The String to add to the script
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(java.lang.String)
     */
    public ScriptBuffer appendData(String str)
    {
        parts.add(str);
        return this;
    }

    /**
     * Call a named function with one parameter.
     * @param funcName The name of the function to call
     * @param params The parameters to the above function
     * @return this. To allow buffer.append(x).append(y).append(z);
     * @see org.directwebremoting.ScriptSessions#addFunctionCall
     */
    public ScriptBuffer appendCall(String funcName, Object... params)
    {
        appendScript(funcName);
        appendScript("(");

        for (int i = 0; i < params.length; i++)
        {
            if (i != 0)
            {
                appendScript(",");
            }
            appendData(params[i]);
        }

        appendScript(");");
        return this;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return parts.toString();
    }

    /**
     * For DWR use only - This method is not part of the public API.
     * Do not use it without understanding the implications for future proofing.
     * @return The list of parts of the final output script
     */
    public List<?> getParts()
    {
        return parts;
    }

    /**
     * This is where we store all the script components waiting to be serialized
     */
    private final List<Object> parts = new ArrayList<Object>();
}
