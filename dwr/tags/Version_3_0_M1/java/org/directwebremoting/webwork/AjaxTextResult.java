package org.directwebremoting.webwork;

/**
 * Interface defining access to a wrapped rendered action result.
 *
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
public interface AjaxTextResult extends AjaxResult
{
    /**
     * Gets the string representation of the rendered action result.
     *
     * @return string representation of rendered action
     */
    String getText();
}
