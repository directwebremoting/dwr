package uk.ltd.getahead.dwr;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the main processor that handles all the requests to DWR.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Processor
{
    /**
     * @param allowImpossibleTests The allowImpossibleTests to set.
     */
    public void setAllowImpossibleTests(boolean allowImpossibleTests);

    /**
     * @param scriptCompressed The scriptCompressed to set.
     */
    public void setScriptCompressed(boolean scriptCompressed);

    /**
     * The equivalent of doGet and doPost
     * @param req The browsers request
     * @param resp The response channel
     * @throws IOException
     */
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException;

    /**
     * Check to see if the given word is reserved or a bad idea in any known
     * version of JavaScript.
     * @param name The word to check
     * @return false if the word is not reserved
     */
    public boolean isReservedWord(String name);
}
