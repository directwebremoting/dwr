package org.directwebremoting.webwork;

/**
 * Interface defining access to a wrapped action result.
 *
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
public interface AjaxDataResult extends AjaxResult
{
    /**
     * Access the wrapped result. Usually this is an action instance.
     *
     * @return wrapped result
     */
    Object getData();
}
