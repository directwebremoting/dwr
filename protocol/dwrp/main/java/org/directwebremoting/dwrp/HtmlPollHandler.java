package org.directwebremoting.dwrp;


/**
 * A Handler polling DWR calls whose replies are HTML wrapped.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HtmlPollHandler extends BasePollHandler
{
    /**
     * Initialize a BasePollHandler to not do HTML wrapping
     */
    public HtmlPollHandler()
    {
        super(false);
    }
}
