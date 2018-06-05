package org.directwebremoting.webwork;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <code>DWRAction</code> pre/post processor.
 *
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
public interface IDWRActionProcessor
{
    /**
     *
     * @param request
     * @param originalResponse
     * @param actionResponse
     * @param invocationParameters
     */
    void preProcess(HttpServletRequest request, HttpServletResponse originalResponse, HttpServletResponse actionResponse, Map<String, String> invocationParameters);

    /**
     *
     * @param request
     * @param originalResponse
     * @param actionResponse
     * @param result
     */
    void postProcess(HttpServletRequest request, HttpServletResponse originalResponse, HttpServletResponse actionResponse, AjaxResult result);
}

