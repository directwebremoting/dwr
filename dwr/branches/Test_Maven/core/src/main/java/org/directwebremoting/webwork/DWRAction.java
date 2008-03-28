package org.directwebremoting.webwork;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.util.Logger;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.DispatcherUtils;
import com.opensymphony.webwork.dispatcher.mapper.ActionMapping;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionProxyFactory;
import com.opensymphony.xwork.Result;
import com.opensymphony.xwork.config.ConfigurationException;
import com.opensymphony.xwork.util.OgnlValueStack;
import com.opensymphony.xwork.util.XWorkContinuationConfig;

/**
 * This class/interface 
 */
public class DWRAction
{
    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DWRAction.class);

    private static final DWRAction s_instance = new DWRAction();

    private DispatcherUtils m_wwDispatcher;

    private DWRAction()
    {
    }

    public static AjaxResult execute(ActionDefinition actionDefinition, Map params, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ConfigurationException, ServletException
    {
        return s_instance.doExecute(actionDefinition, params, request, response, servletContext);
    }

    protected AjaxResult doExecute(ActionDefinition actionDefinition, Map params, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ConfigurationException, ServletException
    {
        if (null == m_wwDispatcher)
        {
            DispatcherUtils.initialize(servletContext);
            m_wwDispatcher = DispatcherUtils.getInstance();
        }

        MockHttpServletResponse actionResponse = new MockHttpServletResponse();

        m_wwDispatcher.prepare(request, actionResponse);

        ActionInvocation invocation = invokeAction(m_wwDispatcher, request, actionResponse, servletContext, actionDefinition, params);

        AjaxResult result = null;
        if (actionDefinition.isExecuteResult())
        {
            // HINT: we have output string
            result = getTextResult(actionResponse);
        }
        else
        {
            result = new DefaultAjaxDataResult(invocation.getAction());
        }

        return result;

    }

    protected ActionInvocation invokeAction(DispatcherUtils du, HttpServletRequest request, HttpServletResponse response, ServletContext context, ActionDefinition actionDefinition, Map params) throws ConfigurationException, ServletException
    {
        ActionMapping mapping = getActionMapping(actionDefinition, params);
        Map extraContext = du.createContextMap(request, response, mapping, context);

        // If there was a previous value stack, then create a new copy and pass it in to be used by the new Action
        OgnlValueStack stack = (OgnlValueStack) request.getAttribute(ServletActionContext.WEBWORK_VALUESTACK_KEY);
        if (null != stack)
        {
            extraContext.put(ActionContext.VALUE_STACK, new OgnlValueStack(stack));
        }

        try
        {
            String namespace = mapping.getNamespace();
            String name = mapping.getName();
            String method = mapping.getMethod();

            prepareContinuationAction(request, extraContext);

            ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy(actionDefinition.getNamespace(), actionDefinition.getAction(), extraContext, actionDefinition.isExecuteResult(), false);
            proxy.setMethod(actionDefinition.getMethod());
            request.setAttribute(ServletActionContext.WEBWORK_VALUESTACK_KEY, proxy.getInvocation().getStack());

            // if the ActionMapping says to go straight to a result, do it!
            if (mapping.getResult() != null)
            {
                Result result = mapping.getResult();
                result.execute(proxy.getInvocation());
            }
            else
            {
                proxy.execute();
            }

            return proxy.getInvocation();
        }
        catch (ConfigurationException ce)
        {
            throw ce;
        }
        catch (Exception e)
        {
            throw new ServletException("Cannot invoke action '" + actionDefinition.getAction() + "' in namespace '" + actionDefinition.getNamespace() + "'", e);
        }
        finally
        {
            // If there was a previous value stack then set it back onto the request
            if (null != stack)
            {
                request.setAttribute(ServletActionContext.WEBWORK_VALUESTACK_KEY, stack);
            }
        }
    }

    protected void prepareContinuationAction(HttpServletRequest request, Map extraContext)
    {
        String id = request.getParameter(XWorkContinuationConfig.CONTINUE_PARAM);
        if (null != id)
        {
            // remove the continue key from the params - we don't want to bother setting
            // on the value stack since we know it won't work. Besides, this breaks devMode!
            Map params = (Map) extraContext.get(ActionContext.PARAMETERS);
            params.remove(XWorkContinuationConfig.CONTINUE_PARAM);

            // and now put the key in the context to be picked up later by XWork
            extraContext.put(XWorkContinuationConfig.CONTINUE_KEY, id);
        }
    }

    protected ActionMapping getActionMapping(ActionDefinition actionDefinition, Map params)
    {
        ActionMapping actionMapping = new ActionMapping(actionDefinition.getAction(), actionDefinition.getNamespace(), actionDefinition.getMethod(), params);

        return actionMapping;
    }

    protected AjaxTextResult getTextResult(MockHttpServletResponse response)
    {
        DefaultAjaxTextResult result = new DefaultAjaxTextResult();

        String text = null;
        try
        {
            text = response.getContentAsString();
        }
        catch (UnsupportedEncodingException uee)
        {
            log.warn("Cannot retrieve text output as string", uee);
        }

        if (null == text)
        {
            try
            {
                text = response.getCharacterEncoding() != null ? new String(response.getContentAsByteArray(), response.getCharacterEncoding()) : new String(response.getContentAsByteArray());
            }
            catch (UnsupportedEncodingException uee)
            {
                log.warn("Cannot retrieve text output as encoded byte array", uee);
                text = new String(response.getContentAsByteArray());
            }
        }

        result.setText(text);
        return result;
    }
}
