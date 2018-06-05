package org.directwebremoting.annotations;

import org.directwebremoting.extend.Creator;

/**
 * A scripting scope.
 * See {@link org.directwebremoting.extend.Creator} for details.
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public enum ScriptScope
{
    /**
     * @see Creator#APPLICATION
     */
    APPLICATION(Creator.APPLICATION),

    /**
     * @see Creator#SESSION
     */
    SESSION(Creator.SESSION),

    /**
     * @see Creator#SCRIPT
     */
    SCRIPT(Creator.SCRIPT),

    /**
     * @see Creator#REQUEST
     */
    REQUEST(Creator.REQUEST),

    /**
     * @see Creator#PAGE
     */
    PAGE(Creator.PAGE);

    /**
     * Link a ScriptScope to constant from Creator
     * @param value The scope string constant
     */
    private ScriptScope(String value)
    {
        this.value = value;
    }

    /**
     * Accessor for the scope string constant from Creator
     * @return One of "application", "session", etc.
     */
    String getValue()
    {
        return value;
    }

    /**
     * The scope constant value
     */
    private String value;
}
