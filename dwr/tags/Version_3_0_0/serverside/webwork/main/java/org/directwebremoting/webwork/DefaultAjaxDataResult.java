package org.directwebremoting.webwork;

/**
 * Default implementation of <code>AjaxDataResult</code>
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
public class DefaultAjaxDataResult implements AjaxDataResult
{
    private Object data;

    /**
     * Sole constructor.
     *
     * @param data the wrapped result object
     */
    public DefaultAjaxDataResult(Object data)
    {
        this.data = data;
    }

    /**
     * @see org.directwebremoting.webwork.AjaxDataResult#getData()
     */
    public Object getData()
    {
        return data;
    }

}
