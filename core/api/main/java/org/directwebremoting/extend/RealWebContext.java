package org.directwebremoting.extend;

import org.directwebremoting.WebContext;

/**
 * An interface that extends WebContext with some functions that should not
 * be used by end users, but could be useful to system extenders.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface RealWebContext extends WebContext
{
    /**
     * Fill in the page details from the servlet request.
     * <p>This method should be used by anything that parses a batch, and then
     * allows either uses a {@link WebContext} or allows other things to use a
     * WebContext.
     * @param sentPage The URL of the current page
     * @param scriptSession The active ScriptSession
     */
    void initialize(String sentPage, RealScriptSession scriptSession);
}
