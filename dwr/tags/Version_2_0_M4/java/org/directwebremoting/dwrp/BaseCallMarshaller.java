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

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Marshaller;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.impl.RemoteDwrEngine;
import org.directwebremoting.impl.ScriptBufferUtil;
import org.directwebremoting.util.DebuggingPrintWriter;
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
    public Calls marshallInbound(HttpServletRequest request, HttpServletResponse response) throws IOException, ServerException
    {
        // We must parse the parameters before we setup the conduit because it's
        // only after doing this that we know the scriptSessionId

        WebContext webContext = WebContextFactory.get();
        ParsedRequest parsedRequest = (ParsedRequest) request.getAttribute(ATTRIBUTE_PARSED_REQUEST);
        if (parsedRequest == null)
        {
            parsedRequest = parseRequest(request);

            // Save calls for retry exception
            request.setAttribute(ATTRIBUTE_PARSED_REQUEST, parsedRequest);
        }

        // A check to see that this isn't a csrf attack
        // http://en.wikipedia.org/wiki/Cross-site_request_forgery
        // http://www.tux.org/~peterw/csrf.txt
        if (request.isRequestedSessionIdValid() && request.isRequestedSessionIdFromCookie() && crossDomainSessionSecurity)
        {
            String requestedSessionId = request.getRequestedSessionId();
            if (requestedSessionId.length() > 0)
            {
                if (!requestedSessionId.equals(parsedRequest.getHttpSessionId()))
                {
                    throw new SecurityException("Session Error");
                }
            }
        }

        // Various bits of the ParsedRequest need to be stashed away places
        storeParsedRequest(request, webContext, parsedRequest);

        Calls calls = parsedRequest.getCalls();

        // Debug the environment
        if (log.isDebugEnabled() && calls.getCallCount() > 0)
        {
            // We can just use 0 because they are all shared
            InboundContext inctx = (InboundContext) parsedRequest.getInboundContexts().get(0);
            StringBuffer buffer = new StringBuffer();

            for (Iterator it = inctx.getInboundVariableNames(); it.hasNext();)
            {
                String key = (String) it.next();
                InboundVariable value = inctx.getInboundVariable(key);
                if (key.startsWith(ConversionConstants.INBOUND_CALLNUM_PREFIX) &&
                    key.indexOf(ConversionConstants.INBOUND_CALLNUM_SUFFIX + ConversionConstants.INBOUND_KEY_ENV) != -1)
                {
                    buffer.append(key + '=' + value.toString() + ", ");
                }
            }

            if (buffer.length() > 0)
            {
                log.debug("Environment:  " + buffer.toString());
            }
        }

        callLoop:
        for (int callNum = 0; callNum < calls.getCallCount(); callNum++)
        {
            Call call = calls.getCall(callNum);
            InboundContext inctx = (InboundContext) parsedRequest.getInboundContexts().get(callNum);

            // Get a list of the available matching methods with the coerced
            // parameters that we will use to call it if we choose to use
            // that method.
            Creator creator = creatorManager.getCreator(call.getScriptName());

            // Which method are we using?
            Method method = findMethod(call, inctx);
            if (method == null)
            {
                String name = call.getScriptName() + '.' + call.getMethodName();
                String error = Messages.getString("BaseCallMarshaller.UnknownMethod", name);
                log.warn("Marshalling exception: " + error);

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
                log.error("Access denied: " + reason + ". creator=" + creator + ", call=" + call.getScriptName() + "." + method);

                call.setMethod(null);
                call.setParameters(null);
                call.setException(new SecurityException(Messages.getString("BaseCallMarshaller.AccessDenied")));

                continue callLoop;
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
                    log.warn("Marshalling exception: " + ex.getMessage());

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
     * Build a ParsedRequest and put it in the request
     * @param request Where we store the parsed data
     * @param webContext We need to notify others of some of the data we find
     * @param parsedRequest The parsed data to store
     */
    private void storeParsedRequest(HttpServletRequest request, WebContext webContext, ParsedRequest parsedRequest)
    {
        String normalizedPage = pageNormalizer.normalizePage(parsedRequest.getPage());
        webContext.setCurrentPageInformation(normalizedPage, parsedRequest.getScriptSessionId());

        // Remaining parameters get put into the request for later consumption
        Map paramMap = parsedRequest.getSpareParameters();
        if (paramMap.size() != 0)
        {
            for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                request.setAttribute(key, value);
                log.debug("Moved param to request: " + key + "=" + value);
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
            throw new IllegalArgumentException(Messages.getString("BaseCallMarshaller.MissingClassParam"));
        }

        if (call.getMethodName() == null)
        {
            throw new IllegalArgumentException(Messages.getString("BaseCallMarshaller.MissingMethodParam"));
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
            log.warn("Warning multiple matching methods. Using first match.");
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
            out = new DebuggingPrintWriter("", response.getWriter());
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
            dpw.setPrefix("out(" + conduit.hashCode() + "): ");
        }

        // Send the script prefix (if any)
        sendOutboundScriptPrefix(out);

        // From the call to addScriptConduit() there could be 2 threads writing
        // to 'out' so we synchronize on 'out' to make sure there are no
        // clashes
        RealScriptSession scriptSession = (RealScriptSession) WebContextFactory.get().getScriptSession();

        out.println(ConversionConstants.SCRIPT_CALL_INSERT);
        scriptSession.addScriptConduit(conduit);
        scriptSession.removeScriptConduit(conduit);
        out.println(ConversionConstants.SCRIPT_CALL_REPLY);

        for (int i = 0; i < replies.getReplyCount(); i++)
        {
            Reply reply = replies.getReply(i);
            String id = reply.getId();

            try
            {
                // The existance of a throwable indicates that something went wrong
                if (reply.getThrowable() != null)
                {
                    Throwable ex = reply.getThrowable();
                    RemoteDwrEngine.remoteHandleException(conduit, id, ex);

                    log.warn("--Erroring: id[" + id + "] message[" + ex.toString() + ']');
                }
                else
                {
                    Object data = reply.getReply();
                    RemoteDwrEngine.remoteHandleCallback(conduit, id, data);
                }
            }
            catch (IOException ex)
            {
                // We're a bit stuck we died half way through writing so
                // we can't be sure the browser can react to the failure.
                // Since we can no longer do output we just log and end
                log.error("--Output Error: id[" + id + "] message[" + ex.toString() + ']', ex);
            }
            catch (MarshallException ex)
            {
                RemoteDwrEngine.remoteHandleMarshallException(conduit, id, ex);
                log.warn("--MarshallException: id=" + id + " class=" + ex.getConversionType().getName() + " message=" + ex.toString());
            }
            catch (Exception ex)
            {
                // This is a bit of a "this can't happen" case so I am a bit
                // nervous about sending the exception to the client, but we
                // want to avoid silently dying so we need to do something.
                RemoteDwrEngine.remoteHandleException(conduit, id, ex);
                log.error("--MarshallException: id=" + id + " message=" + ex.toString());
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
     * Parse an inbound request into a Calls object
     * @param req The original browser's request
     * @return A parsed set of calls
     * @throws ServerException If reading from the request body stream fails
     */
    private ParsedRequest parseRequest(HttpServletRequest req) throws ServerException
    {
        ParsedRequest parsedRequest = new ParsedRequest();

        if (req.getMethod().equals("GET"))
        {
            parsedRequest.setAllParameters(ParseUtil.parseGet(req));
        }
        else
        {
            parsedRequest.setAllParameters(ParseUtil.parsePost(req));
        }

        parseParameters(parsedRequest);
        return parsedRequest;
    }

    /**
     * Fish out the important parameters
     * @param parsedRequest The call details the methods we are calling
     * @throws ServerException If the parsing of input parameter fails
     */
    private void parseParameters(ParsedRequest parsedRequest) throws ServerException
    {
        Map paramMap = parsedRequest.getAllParameters();
        Calls calls = new Calls();
        parsedRequest.setCalls(calls);

        // Work out how many calls are in this packet
        String callStr = (String) paramMap.remove(ConversionConstants.INBOUND_CALL_COUNT);
        int callCount;
        try
        {
            callCount = Integer.parseInt(callStr);
        }
        catch (NumberFormatException ex)
        {
            throw new ServerException(Messages.getString("ExecuteQuery.BadCallCount", callStr));
        }

        List inboundContexts = parsedRequest.getInboundContexts();

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

        String httpSessionId = (String) paramMap.remove(ConversionConstants.INBOUND_KEY_HTTP_SESSIONID);
        parsedRequest.setHttpSessionId(httpSessionId);

        String scriptSessionId = (String) paramMap.remove(ConversionConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        parsedRequest.setScriptSessionId(scriptSessionId);

        String page = (String) paramMap.remove(ConversionConstants.INBOUND_KEY_PAGE);
        parsedRequest.setPage(page);

        parsedRequest.setSpareParameters(paramMap);
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
     * Accessor for the PageNormalizer.
     * @param pageNormalizer The new PageNormalizer
     */
    public void setPageNormalizer(PageNormalizer pageNormalizer)
    {
        this.pageNormalizer = pageNormalizer;
    }

    /**
     * To we perform cross-domain session security checks?
     * @param crossDomainSessionSecurity the cross domain session security setting
     */
    public void setCrossDomainSessionSecurity(boolean crossDomainSessionSecurity)
    {
        this.crossDomainSessionSecurity = crossDomainSessionSecurity;
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
                throw new NullPointerException("out=null");
            }

            this.out = out;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
         */
        public boolean addScript(ScriptBuffer script) throws IOException, MarshallException
        {
            sendScript(out, ScriptBufferUtil.createOutput(script, converterManager));
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
     * To we perform cross-domain session security checks?
     */
    protected boolean crossDomainSessionSecurity = true; 

    /**
     * How we turn pages into the canonical form.
     */
    protected PageNormalizer pageNormalizer;

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
    protected static final String ATTRIBUTE_REQUEST = "org.directwebremoting.dwrp.request";

    /**
     * How we stash away the conduit
     */
    protected static final String ATTRIBUTE_CONDUIT = "org.directwebremoting.dwrp.conduit";

    /**
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_PARSED_REQUEST = "org.directwebremoting.dwrp.parsedRequest";

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(BaseCallMarshaller.class);
}
