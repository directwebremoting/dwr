package org.directwebremoting.webwork;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.util.FakeHttpServletResponse;
import org.directwebremoting.util.LocalUtil;
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
 * This class represents the entry point to all WebWork action invocations. It identifies the
 * action to be invoked, prepares the action invocation context and finally wraps the
 * result.
 * You can configure an <code>IDWRActionProcessor</code> through a context-wide initialization parameter
 * <code>dwrActionProcessor</code> that whose methods will be invoked around action invocation.
 *
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
public class DWRAction
{
    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DWRAction.class);

    private static final String DWRACTIONPROCESSOR_INIT_PARAM = "dwrActionProcessor";

    private static DWRAction s_instance;

    private DispatcherUtils m_wwDispatcher;

    private IDWRActionProcessor m_actionProcessor;

    private DWRAction(ServletContext servletContext) throws ServletException
    {
        DispatcherUtils.initialize(servletContext);
        m_wwDispatcher = DispatcherUtils.getInstance();
        m_actionProcessor = loadActionProcessor(servletContext.getInitParameter(DWRACTIONPROCESSOR_INIT_PARAM));
    }

    /**
     * Entry point for all action invocations.
     *
     * @param actionDefinition the identification information for the action
     * @param params action invocation parameters
     * @param request original request
     * @param response original response
     * @param servletContext current <code>ServletContext</code>
     * @return an <code>AjaxResult</code> wrapping invocation result
     *
     * @throws ServletException thrown if the initialization or invocation of the action fails
     */
    public static AjaxResult execute(ActionDefinition actionDefinition, Map params, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException
    {
        initialize(servletContext);

        return s_instance.doExecute(actionDefinition, params, request, response, servletContext);
    }

    protected AjaxResult doExecute(ActionDefinition actionDefinition, Map params, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException
    {

        FakeHttpServletResponse actionResponse = new FakeHttpServletResponse();

        if (null != m_actionProcessor)
        {
            m_actionProcessor.preProcess(request, response, actionResponse, params);
        }

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

        if (null != m_actionProcessor)
        {
            m_actionProcessor.postProcess(request, response, actionResponse, result);
        }

        return result;
    }

    protected ActionInvocation invokeAction(DispatcherUtils du, HttpServletRequest request, HttpServletResponse response, ServletContext context, ActionDefinition actionDefinition, Map params) throws ServletException
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
            throw new ServletException("Cannot invoke action '" + actionDefinition.getAction() + "' in namespace '" + actionDefinition.getNamespace() + "'", ce);
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

    protected AjaxTextResult getTextResult(FakeHttpServletResponse response)
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

    /**
     * Performs the one time initialization of the singleton <code>DWRAction</code>.
     *
     * @param servletContext
     * @throws ServletException thrown in case the singleton initialization fails
     */
    private static void initialize(ServletContext servletContext) throws ServletException
    {
        synchronized(DWRAction.class)
        {
            if (null == s_instance)
            {
                s_instance = new DWRAction(servletContext);
            }
        }
    }

    /**
     * Tries to instantiate an <code>IDWRActionProcessor</code> if defined in web.xml.
     *
     * @param actionProcessorClassName
     * @return an instance of <code>IDWRActionProcessor</code> if the init-param is defined or <code>null</code>
     * @throws ServletException thrown if the <code>IDWRActionProcessor</code> cannot be loaded and instantiated
     */
    private static IDWRActionProcessor loadActionProcessor(String actionProcessorClassName) throws ServletException
    {
        if (null == actionProcessorClassName || "".equals(actionProcessorClassName))
        {
            return null;
        }

        try
        {
            Class actionProcessorClass = LocalUtil.classForName(actionProcessorClassName);

            return (IDWRActionProcessor) actionProcessorClass.newInstance();
        }
        catch(ClassNotFoundException cnfe)
        {
            throw new ServletException("Cannot load DWRActionProcessor class '" + actionProcessorClassName + "'", cnfe);
        }
        catch(IllegalAccessException iae)
        {
            throw new ServletException("Cannot instantiate DWRActionProcessor class '" + actionProcessorClassName + "'. Default constructor is not visible", iae);
        }
        catch(InstantiationException ie)
        {
            throw new ServletException("Cannot instantiate DWRActionProcessor class '" + actionProcessorClassName + "'. No default constructor found", ie);
        }
        catch(Throwable cause)
        {
            throw new ServletException("Cannot instantiate DWRActionProcessor class '" + actionProcessorClassName + "'", cause);
        }
    }

}
