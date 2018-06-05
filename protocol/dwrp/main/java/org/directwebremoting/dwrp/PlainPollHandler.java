package org.directwebremoting.dwrp;

/**
 * A Handler polling DWR calls whose replies are NOT HTML wrapped.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PlainPollHandler extends BasePollHandler
{
    /**
     * Initialize a BasePollHandler to do HTML wrapping
     */
    public PlainPollHandler()
    {
        super(true);
    }
}
