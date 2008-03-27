/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.dwrp;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.AccessControl;
import org.directwebremoting.Call;
import org.directwebremoting.Calls;
import org.directwebremoting.ConverterManager;
import org.directwebremoting.Creator;
import org.directwebremoting.CreatorManager;
import org.directwebremoting.InboundContext;
import org.directwebremoting.InboundVariable;
import org.directwebremoting.MarshallException;
import org.directwebremoting.Marshaller;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.Replies;
import org.directwebremoting.Reply;
import org.directwebremoting.ScriptConduit;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.TypeHintContext;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.DebuggingPrintWriter;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * A Marshaller that output plain Javascript.
 * This marshaller can be tweaked to output Javascript in an HTML context.
 * This class works in concert with CallScriptConduit, they should be
 * considered closely related and it is important to understand what one does
 * while editing the other.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BaseCallMarshaller implements Marshaller
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Marshaller#marshallInbound(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public Calls marshallInbound(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // We must parse the parameters before we setup the conduit because it's
        // only after doing this that we know the scriptSessionId

        WebContext webContext = WebContextFactory.get();
        ParseResponse parseResponse = (ParseResponse) request.getAttribute(ATTRIBUTE_PARSE_RESPONSE);
        if (parseResponse == null)
        {
            parseResponse = parseRequest(request);

            // Save calls for retry exception
            request.setAttribute(ATTRIBUTE_PARSE_RESPONSE, parseResponse);
        }

        // Various bits of parseResponse need to be stashed away places
        storeParseResponse(request, webContext, parseResponse);

        Calls calls = parseResponse.getCalls();

        // Debug the environment
        if (log.isDebugEnabled() && calls.getCallCount() > 0)
        {
            // We can just use 0 because they are all shared
            InboundContext inctx = (InboundContext) parseResponse.getInboundContexts().get(0);
            StringBuffer buffer = new StringBuffer();

            for (Iterator it = inctx.getInboundVariableNames(); it.hasNext();)
            {
                String key = (String) it.next();
                InboundVariable value = inctx.getInboundVariable(key);
                if (key.startsWith(ConversionConstants.INBOUND_CALLNUM_PREFIX) &&
                    key.indexOf(ConversionConstants.INBOUND_CALLNUM_SUFFIX + ConversionConstants.INBOUND_KEY_ENV) != -1)
                {
                    buffer.append(key + '=' + value.toString() + ", "); //$NON-NLS-1$
                }
            }

            if (buffer.length() > 0)
            {
                log.debug("Environment:  " + buffer.toString()); //$NON-NLS-1$
            }
        }

        callLoop:
        for (int callNum = 0; callNum < calls.getCallCount(); callNum++)
        {
            Call call = calls.getCall(callNum);
            InboundContext inctx = (InboundContext) parseResponse.getInboundContexts().get(callNum);

            // Get a list of the available matching methods with the coerced
            // parameters that we will use to call it if we choose to use
            // that method.
            Creator creator = creatorManager.getCreator(call.getScriptName());

            // Which method are we using?
            Method method = findMethod(call, inctx);
            if (method == null)
            {
                String name = call.getScriptName() + '.' + call.getMethodName();
                String error = Messages.getString("DefaultRemoter.UnknownMethod", name); //$NON-NLS-1$
                log.warn("Marshalling exception: " + error); //$NON-NLS-1$

                call.setMethod(null);
                call.setParameters(null);
                call.setException(new IllegalArgumentException(error));

                continue callLoop;
            }

            call.setMethod(method);

            // Check this method is accessible
            String reason = accessControl.getReasonToNotExecute(creator, call.getScriptName(), method);
            if (reason != null)
            {
                throw new SecurityException(Messages.getString("ExecuteQuery.AccessDenied")); //$NON-NLS-1$
            }

            // Convert all the parameters to the correct types
            Object[] params = new Object[method.getParameterTypes().length];
            for (int j = 0; j < method.getParameterTypes().length; j++)
            {
                try
                {
                    Class paramType = method.getParameterTypes()[j];
                    InboundVariable param = inctx.getParameter(callNum, j);
                    TypeHintContext incc = new TypeHintContext(converterManager, method, j);
                    params[j] = converterManager.convertInbound(paramType, param, inctx, incc);
                }
                catch (MarshallException ex)
                {
                    log.warn("Marshalling exception: " + ex.getMessage()); //$NON-NLS-1$

                    call.setMethod(null);
                    call.setParameters(null);
                    call.setException(ex);

                    continue callLoop;
                }
            }

            call.setParameters(params);
        }

        return calls;
    }

    /**
     * Build a ParseResponse and put it in the request
     * @param request Where we store the parsed data
     * @param webContext We need to notify others of some of the data we find
     * @param parseResponse The parsed data to store
     */
    private void storeParseResponse(HttpServletRequest request, WebContext webContext, ParseResponse parseResponse)
    {
        webContext.setCurrentPageInformation(parseResponse.getPage(), parseResponse.getScriptSessionId());

        // Remaining parameters get put into the request for later consumption
        Map paramMap = parseResponse.getSpareParameters();
        if (paramMap.size() != 0)
        {
            for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                request.setAttribute(key, value);
                log.debug("Moved param to request: " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    /**
     * Find the method the best matches the method name and parameters
     * @param call The function call we are going to make
     * @param inctx The data conversion context
     * @return A matching method, or null if one was not found.
     */
    private Method findMethod(Call call, InboundContext inctx)
    {
        if (call.getScriptName() == null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultRemoter.MissingClassParam")); //$NON-NLS-1$
        }

        if (call.getMethodName() == null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultRemoter.MissingMethodParam")); //$NON-NLS-1$
        }

        Creator creator = creatorManager.getCreator(call.getScriptName());
        Method[] methods = creator.getType().getMethods();
        List available = new ArrayList();

        methods:
        for (int i = 0; i < methods.length; i++)
        {
            // Check method name and access
            if (methods[i].getName().equals(call.getMethodName()))
            {
                // Check number of parameters
                if (methods[i].getParameterTypes().length == inctx.getParameterCount())
                {
                    // Clear the previous conversion attempts (the param types
                    // will probably be different)
                    inctx.clearConverted();

                    // Check parameter types
                    for (int j = 0; j < methods[i].getParameterTypes().length; j++)
                    {
                        Class paramType = methods[i].getParameterTypes()[j];
                        if (!converterManager.isConvertable(paramType))
                        {
                            // Give up with this method and try the next
                            continue methods;
                        }
                    }

                    available.add(methods[i]);
                }
            }
        }

        // Pick a method to call
        if (available.size() > 1)
        {
            log.warn("Warning multiple matching methods. Using first match."); //$NON-NLS-1$
        }

        if (available.isEmpty())
        {
            return null;
        }

        // At the moment we are just going to take the first match, for a
        // later increment we might pick the best implementation
        return (Method) available.get(0);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Marshaller#marshallOutbound(org.directwebremoting.Replies, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void marshallOutbound(Replies replies, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // Get the output stream and setup the mimetype
        response.setContentType(getOutboundMimeType());
        PrintWriter out;
        if (log.isDebugEnabled())
        {
            // This might be considered evil - altering the program flow
            // depending on the log status, however DebuggingPrintWriter is
            // very thin and only about logging
            out = new DebuggingPrintWriter("", response.getWriter()); //$NON-NLS-1$
        }
        else
        {
            out = response.getWriter();
        }

        // The conduit to pass on reverse ajax scripts
        ScriptConduit conduit = new CallScriptConduit(out);

        // Setup a debugging prefix
        if (out instanceof DebuggingPrintWriter)
        {
            DebuggingPrintWriter dpw = (DebuggingPrintWriter) out;
            dpw.setPrefix("out(" + conduit.hashCode() + "): "); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // Send the script prefix (if any)
        sendOutboundScriptPrefix(out);

        // From the call to addScriptConduit() there could be 2 threads writing
        // to 'out' so we synchronize on 'out' to make sure there are no
        // clashes
        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();

        out.println(ConversionConstants.SCRIPT_CALL_INSERT);
        scriptSession.addScriptConduit(conduit);
        scriptSession.removeScriptConduit(conduit);
        out.println(ConversionConstants.SCRIPT_CALL_REPLY);

        OutboundContext converted = new OutboundContext();
        for (int i = 0; i < replies.getReplyCount(); i++)
        {
            Reply reply = replies.getReply(i);

            // The existance of a throwable indicates that something went wrong
            if (reply.getThrowable() != null)
            {
                Throwable ex = reply.getThrowable();
                OutboundVariable ov = convertException(ex, converted);

                String script = ov.getInitCode() + "DWREngine._handleServerError('" + reply.getId() + "', " + ov.getAssignCode() + ");"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                sendScript(out, script);

                log.warn("--Erroring: id[" + reply.getId() + "] message[" + ex.toString() + ']'); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else
            {
                Object data = reply.getReply();

                try
                {
                    OutboundVariable ov = converterManager.convertOutbound(data, converted);

                    String script = ov.getInitCode() + "DWREngine._handleResponse('" + reply.getId() + "', " + ov.getAssignCode() + ");"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    sendScript(out, script);
                }
                catch (MarshallException ex)
                {
                    OutboundVariable ov = convertException(ex, converted);

                    String script = ov.getInitCode() + "DWREngine._handleServerError('" + reply.getId() + "', " + ov.getAssignCode() + ");"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    sendScript(out, script);

                    log.warn("--MarshallException: id[" + reply.getId() + "] message[" + ex.toString() + ']'); //$NON-NLS-1$ //$NON-NLS-2$
                }
                catch (Exception ex)
                {
                    OutboundVariable ov = convertException(ex, converted);

                    String script = ov.getInitCode() + "DWREngine._handleServerError('" + reply.getId() + "', " + ov.getAssignCode() + ");"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    sendScript(out, script);

                    log.warn("--Erroring: id[" + reply.getId() + "] message[" + ex.toString() + ']', ex); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        sendOutboundScriptSuffix(out);
    }

    /**
     * Send a script to the browser
     * @param out The stream to write to
     * @param script The script to send
     * @throws IOException If the write fails
     */
    protected abstract void sendScript(PrintWriter out, String script) throws IOException;

    /**
     * iframe mode starts as HTML, so get into script mode
     * @return A script prefix
     */
    protected abstract String getOutboundMimeType();

    /**
     * iframe mode starts as HTML, so get into script mode
     * @param out The stream to write to
     * @throws IOException If the write fails
     */
    protected abstract void sendOutboundScriptPrefix(PrintWriter out) throws IOException;

    /**
     * iframe mode needs to get out of script mode
     * @param out The stream to write to
     * @throws IOException If the write fails
     */
    protected abstract void sendOutboundScriptSuffix(PrintWriter out) throws IOException;

    /**
     * Convert an exception into an outbound variable
     * @param th The exception to be converted
     * @param converted The conversion context
     * @return A new outbound exception
     */
    private OutboundVariable convertException(Throwable th, OutboundContext converted)
    {
        try
        {
            if (converterManager.isConvertable(th.getClass()))
            {
                return converterManager.convertOutbound(th, converted);
            }
        }
        catch (MarshallException ex)
        {
            log.warn("Exception while converting. Exception to be converted: " + th, ex); //$NON-NLS-1$
        }

        // So we will have to create one for ourselves
        OutboundVariable ov = new OutboundVariable();
        String varName = converted.getNextVariableName();
        ov.setAssignCode(varName);
        ov.setInitCode("var " + varName + " = \"" + JavascriptUtil.escapeJavaScript(th.getMessage()) + "\";"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return ov;
    }

    /**
     * Parse an inbound request into a Calls object
     * @param req The original browser's request
     * @return A parsed set of calls
     * @throws MarshallException If reading from the request body stream fails
     */
    private ParseResponse parseRequest(HttpServletRequest req) throws MarshallException
    {
        ParseResponse parseResponse = new ParseResponse();

        if (req.getMethod().equals("GET")) //$NON-NLS-1$
        {
            parseResponse.setAllParameters(ParseUtil.parseGet(req));
        }
        else
        {
            parseResponse.setAllParameters(ParseUtil.parsePost(req));
        }

        parseParameters(parseResponse);
        return parseResponse;
    }

    /**
     * Fish out the important parameters
     * @param parseResponse The call details the methods we are calling
     * @throws MarshallException If the parsing of input parameter fails
     */
    private void parseParameters(ParseResponse parseResponse) throws MarshallException
    {
        Map paramMap = parseResponse.getAllParameters();
        Calls calls = new Calls();
        parseResponse.setCalls(calls);

        // Work out how many calls are in this packet
        String callStr = (String) paramMap.remove(ConversionConstants.INBOUND_CALL_COUNT);
        int callCount;
        try
        {
            callCount = Integer.parseInt(callStr);
        }
        catch (NumberFormatException ex)
        {
            throw new MarshallException(Messages.getString("ExecuteQuery.BadCallCount", callStr)); //$NON-NLS-1$
        }

        List inboundContexts = parseResponse.getInboundContexts();

        // Extract the ids, scriptnames and methodnames
        for (int callNum = 0; callNum < callCount; callNum++)
        {
            Call call = new Call();
            calls.addCall(call);

            InboundContext inctx = new InboundContext();
            inboundContexts.add(inctx);

            String prefix = ConversionConstants.INBOUND_CALLNUM_PREFIX + callNum + ConversionConstants.INBOUND_CALLNUM_SUFFIX;

            // The special values
            call.setId((String) paramMap.remove(prefix + ConversionConstants.INBOUND_KEY_ID));
            call.setScriptName((String) paramMap.remove(prefix + ConversionConstants.INBOUND_KEY_SCRIPTNAME));
            call.setMethodName((String) paramMap.remove(prefix + ConversionConstants.INBOUND_KEY_METHODNAME));

            // Look for parameters to this method
            for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();

                if (key.startsWith(prefix))
                {
                    String data = (String) entry.getValue();
                    String[] split = LocalUtil.splitInbound(data);

                    String value = split[LocalUtil.INBOUND_INDEX_VALUE];
                    String type = split[LocalUtil.INBOUND_INDEX_TYPE];
                    inctx.createInboundVariable(callNum, key, type, value);
                    it.remove();
                }
            }
        }

        paramMap.remove(ConversionConstants.INBOUND_KEY_HTTP_SESSIONID);
        // Maybe we should check the value of this against the cookie value

        String scriptSessionId = (String) paramMap.remove(ConversionConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        parseResponse.setScriptSessionId(scriptSessionId);

        String page = (String) paramMap.remove(ConversionConstants.INBOUND_KEY_PAGE);
        parseResponse.setPage(page);

        parseResponse.setSpareParameters(paramMap);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Marshaller#isConvertable(java.lang.Class)
     */
    public boolean isConvertable(Class paramType)
    {
        return converterManager.isConvertable(paramType);
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param converterManager The new DefaultConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param creatorManager The new DefaultConverterManager
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
    }

    /**
     * Accessor for the security manager
     * @param accessControl The accessControl to set.
     */
    public void setAccessControl(AccessControl accessControl)
    {
        this.accessControl = accessControl;
    }

    /**
     * A ScriptConduit that works with the parent Marshaller.
     * In some ways this is nasty because it has access to essentially private parts
     * of BaseCallMarshaller, however there is nowhere sensible to store them
     * within that class, so this is a hacky simplification.
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    protected class CallScriptConduit extends ScriptConduit
    {
        /**
         * Simple ctor
         * @param out The stream to write to
         */
        protected CallScriptConduit(PrintWriter out)
        {
            super(RANK_SLOW);
            if (out == null)
            {
                throw new NullPointerException("out=null"); //$NON-NLS-1$
            }

            this.out = out;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(java.lang.String)
         */
        public boolean addScript(String script) throws IOException
        {
            sendScript(out, script);
            return true;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#flush()
         */
        public void flush() throws IOException
        {
            // We don't flush because that puts us into chunked mode
        }

        /**
         * The PrintWriter to send output to, and that we should synchronize against
         */
        private final PrintWriter out;
    }

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * How we stash away the request
     */
    protected static final String ATTRIBUTE_REQUEST = "org.directwebremoting.dwrp.request"; //$NON-NLS-1$

    /**
     * How we stash away the conduit
     */
    protected static final String ATTRIBUTE_CONDUIT = "org.directwebremoting.dwrp.conduit"; //$NON-NLS-1$

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_PARSE_RESPONSE = "org.directwebremoting.dwrp.parseResponse"; //$NON-NLS-1$

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(BaseCallMarshaller.class);
}
