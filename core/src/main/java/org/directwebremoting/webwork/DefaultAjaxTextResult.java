package org.directwebremoting.webwork;

/**
 * This class/interface 
 */
public class DefaultAjaxTextResult implements AjaxTextResult
{
    private String m_text;

    public DefaultAjaxTextResult()
    {
    }

    public DefaultAjaxTextResult(String text)
    {
        m_text = text;
    }

    /**
     * @see net.noco.dwraction.AjaxTextResult#getText()
     */
    public String getText()
    {
        return m_text;
    }

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
