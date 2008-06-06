package org.directwebremoting.webwork;

/**
 * Default implementation of <code>AjaxTextResult</code>
 *
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
public class DefaultAjaxTextResult implements AjaxTextResult
{
    private String m_text;

    /**
     * Empty constructor.
     *
     * @see #setText
     */
    public DefaultAjaxTextResult()
    {
    }

    /**
     * Creates an <code>DefaultAjaxTextResult</code> wrapping the given string.
     *
     * @param text the string to be wrapped
     */
    public DefaultAjaxTextResult(String text)
    {
        m_text = text;
    }

    /**
     * @see org.directwebremoting.webwork.AjaxTextResult#getText()
     */
    public String getText()
    {
        return m_text;
    }

    /**
     * Sets the wrapped string.
     *
     * @param text the string to be wrapped in this result
     */
    public void setText(String text)
    {
        m_text = text;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[AjaxTextResult: '" + m_text + "']";
    }

}
