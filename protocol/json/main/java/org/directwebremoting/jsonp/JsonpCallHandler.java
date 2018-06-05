package org.directwebremoting.jsonp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.extend.ParameterProperty;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * A Handler JSON/REST DWR calls.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonpCallHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (!jsonpEnabled)
        {
            log.warn("JSONP request denied. To enable JSONP mode add an init-param of jsonpEnabled=true to web.xml");
            throw new SecurityException("JSONP interface disabled");
        }

        // Get the output stream and setup the mime type
        response.setContentType(MimeConstants.MIME_JS);
        String callback = getCallback(request);
        PrintWriter out = response.getWriter();

        try
        {
            Calls calls = convertToCalls(request);
            Replies replies = remoter.execute(calls);

            // There will only be one of these while JSON mode does not do batching
            for (int i = 0; i < replies.getReplyCount(); i++)
            {
                Reply reply = replies.getReply(i);

                try
                {
                    // The existence of a throwable indicates that something went wrong
                    if (reply.getThrowable() != null)
                    {
                        Throwable ex = reply.getThrowable();
                        writeData(out, ex, callback);

                        log.warn("--Erroring: message[" + ex.toString() + ']');
                    }
                    else
                    {
                        Object data = reply.getReply();
                        writeData(out, data, callback);
                    }
                }
                catch (Exception ex)
                {
                    // This is a bit of a "this can't happen" case so I am a bit
                    // nervous about sending the exception to the client, but we
                    // want to avoid silently dying so we need to do something.
                    writeData(out, ex, callback);
                    log.error("--ConversionException: message=" + ex.toString());
                }
            }
        }
        catch (Exception ex)
        {
            writeData(out, ex, callback);
        }
    }

    public String getCallback(HttpServletRequest request)
    {
        return request.getParameter("callback");
    }

    /**
     * Take an HttpServletRequest and create from it a Calls object.
     * @param request The input data
     * @return A Calls object that represents the data in the request
     */
    @SuppressWarnings("unchecked")
    public Calls convertToCalls(HttpServletRequest request)
    {
        InboundContext inboundContext = new InboundContext();
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");

        if (pathParts.length < 4)
        {
            log.warn("pathInfo '" + pathInfo + "' contains " + pathParts.length + " parts. At least 4 are required.");
            throw new JsonpCallException("Bad JSON request. See logs for more details.");
        }

        if (pathParts.length > 4)
        {
            for (int i = 4; i < pathParts.length; i++)
            {
                String key = ProtocolConstants.INBOUND_CALLNUM_PREFIX + 0 +
                             ProtocolConstants.INBOUND_CALLNUM_SUFFIX +
                             ProtocolConstants.INBOUND_KEY_PARAM + (i - 4);
                inboundContext.createInboundVariable(0, key, "string", pathParts[i]);
            }
        }
        else
        {
            Map<String, String[]> requestParams = request.getParameterMap();
            int i = 0;
            while (true)
            {
                String[] values = requestParams.get("param" + i);
                if (values == null)
                {
                    break;
                }
                else
                {
                    String key = ProtocolConstants.INBOUND_CALLNUM_PREFIX + 0 +
                                 ProtocolConstants.INBOUND_CALLNUM_SUFFIX +
                                 ProtocolConstants.INBOUND_KEY_PARAM + i;
                    inboundContext.createInboundVariable(0, key, "string", values[0], true);
                    i++;
                }
            }
        }

        Call call = new Call(null, pathParts[2], pathParts[3]);

        // JSON does not support batching
        Calls calls = new Calls();
        calls.addCall(call);

        // Which method are we using?
        call.findMethod(moduleManager, converterManager, inboundContext, 0);
        MethodDeclaration method = call.getMethodDeclaration();

        // Check this method is accessible
        accessControl.assertGeneralExecutionIsPossible(call.getScriptName(), method);

        // We are now sure we have the set of input lined up. They may
        // cross-reference so we do the de-referencing all in one go.
        try
        {
            inboundContext.dereference();
        }
        catch (ConversionException ex)
        {
            log.warn("Dereferencing exception", ex);
            throw new JsonpCallException("Error dereferencing call. See logs for more details.");
        }

        // Convert all the parameters to the correct types
        Object[] params = new Object[method.getParameterTypes().length];
        for (int j = 0; j < method.getParameterTypes().length; j++)
        {
            Class<?> paramType = method.getParameterTypes()[j];
            InboundVariable param = inboundContext.getParameter(0, j);
            Property property = new ParameterProperty(method, j);

            try
            {
                params[j] = converterManager.convertInbound(paramType, param, property);
            }
            catch (ConversionException ex)
            {
                log.warn("Marshalling exception. Param " + j + ", ", ex);
                throw new JsonpCallException("Error marshalling parameters. See logs for more details.");
            }
        }

        call.setParameters(params);

        return calls;
    }

    /**
     * Create output for some data and write it to the given stream.
     */
    public void writeData(PrintWriter out, Object data, String callback)
    {
        try
        {
            ScriptBuffer buffer = new ScriptBuffer();
            buffer.appendData(data);

            String output = ScriptBufferUtil.createOutput(buffer, converterManager, true);
            if (data instanceof Exception)
            {
                output = "{\"error\":" + output + "}";
            }
            if (callback != null && !"".equals(callback.trim()))
            {
                output = callback + "(" + output + ")";
            }
            out.println(output);
        }
        catch (ConversionException ex)
        {
            log.warn("--ConversionException: class=" + ex.getConversionType().getName(), ex);

            ScriptBuffer buffer = new ScriptBuffer();
            buffer.appendData(ex);

            try
            {
                String output = ScriptBufferUtil.createOutput(buffer, converterManager, true);
                output = "{\"error\":" + output + "}";
                if (callback != null && !"".equals(callback.trim()))
                {
                    output = callback + "(" + output + ")";
                }
                out.println(output);
            }
            catch (ConversionException ex1)
            {
                log.error("--Nested ConversionException: Is there an exception handler registered? class=" + ex.getConversionType().getName(), ex);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Marshaller#isConvertable(java.lang.Class)
     */
    public boolean isConvertable(Class<?> paramType)
    {
        return converterManager.isConvertable(paramType);
    }

    /**
     * Accessor for the ConverterManager that we configure
     * @param converterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * Accessor for the ModuleManager that we configure
     * @param moduleManager
     */
    public void setModuleManager(ModuleManager moduleManager)
    {
        this.moduleManager = moduleManager;
    }

    /**
     * How we create new beans
     */
    protected ModuleManager moduleManager = null;

    /**
     * Accessor for the security manager
     * @param accessControl The accessControl to set.
     */
    public void setAccessControl(AccessControl accessControl)
    {
        this.accessControl = accessControl;
    }

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * Are we allowing remote hosts to contact us using JSON?
     */
    public void setJsonpEnabled(boolean jsonpEnabled)
    {
        this.jsonpEnabled = jsonpEnabled;
    }

    /**
     * Are we allowing remote hosts to contact us using JSON?
     */
    protected boolean jsonpEnabled = false;

    /**
     * Setter for the remoter
     * @param remoter The new remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     * The bean to execute remote requests and generate interfaces
     */
    protected Remoter remoter = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JsonpCallHandler.class);
}
