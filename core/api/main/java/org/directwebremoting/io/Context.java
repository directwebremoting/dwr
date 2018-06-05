package org.directwebremoting.io;

import org.directwebremoting.ScriptBuffer;

/**
 * Context is a way to return a value that can only be realized on the client.
 * It allows us to return something, and have server side code keep track of the
 * things it is acting on.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Context
{
    /**
     * Create a new Context
     * @param parent Our parent context, or null if we start from scratch
     * @param extension A string to append to the parent context
     */
    public Context(Context parent, String extension)
    {
        this.parent = parent;
        this.extension = extension;
    }

    /**
     * Accessor for the string to append to the parent context to define us
     * @return the extension
     */
    protected String getContextPath()
    {
        String prefix = (parent == null) ? "" : parent.getContextPath();
        return prefix + extension;
    }

    /**
     * Accessor for the current InitScript
     * @param initScript The new InitScript
     */
    protected void setInitScript(ScriptBuffer initScript)
    {
        this.initScript = initScript;
    }

    /**
     * Accessor for the current InitScript
     * @return The current InitScript
     */
    protected ScriptBuffer getInitScript()
    {
        return initScript;
    }

    /**
     * When we're stand-alone, created from 'new' someone else want the start point
     */
    private ScriptBuffer initScript;

    /**
     * Our parent context, or null if we start from scratch
     */
    private Context parent;

    /**
     * A string to append to the parent context to get to where we are
     */
    private String extension;
}
