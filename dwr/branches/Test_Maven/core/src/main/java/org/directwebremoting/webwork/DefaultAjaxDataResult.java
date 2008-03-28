package org.directwebremoting.webwork;

/**
 * This class/interface 
 */
public class DefaultAjaxDataResult implements AjaxDataResult
{
    private Object m_data;

    public DefaultAjaxDataResult(Object data)
    {
        m_data = data;
    }

    /**
     * @see net.noco.dwraction.AjaxDataResult#getData()
     */
    public Object getData()
    {
        return m_data;
    }

}
