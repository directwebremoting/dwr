package uk.ltd.getahead.dwr;

import java.io.Serializable;
import java.util.List;

/**
 * An interface to the browser so that we can check if it is still 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Browser extends Serializable
{
    /**
     * We might be able to detect that the user has gone away.
     * @return true unless we can be sure that the user has gone away.
     */
    public boolean isValid();

    /**
     * Add a script to the list waiting for remote execution
     * @param script The script to execute
     */
    public void addScript(ClientScript script);

    /**
     * Remove a script from the list waiting for execution
     * @param script The script to remove
     * @return true if a script was removed, or false otherwise
     */
    public boolean removeScript(ClientScript script);

    /**
     * Expected to be of use to the system that forwards scripts to the client
     * @return A list of scripts, which could be empty, but not null.
     */
    public List removeAllScripts();
}
