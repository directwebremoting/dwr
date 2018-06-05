package org.directwebremoting;

/**
 * A way to loop over a set {@link ScriptSession}s looking for instances that
 * match some pattern.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ScriptSessionFilter
{
    /**
     * Does this {@link ScriptSession} match according to the matching pattern
     * declared by this filter.
     * @param session The ScriptSession to check for a match
     * @return true if the session matches, false otherwise
     */
    public boolean match(ScriptSession session);
}
