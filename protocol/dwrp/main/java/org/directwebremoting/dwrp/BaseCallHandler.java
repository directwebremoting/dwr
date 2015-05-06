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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.ParameterProperty;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.RealWebContext;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.extend.SimpleInputStreamFactory;
import org.directwebremoting.impl.AccessLogLevel;
import org.directwebremoting.impl.ExportUtil;
import org.directwebremoting.io.FileTransfer;
import org.directwebremoting.io.InputStreamFactory;
import org.directwebremoting.util.DebuggingPrintWriter;
import org.directwebremoting.util.LocalUtil;

/**
 * A Marshaller that output plain Javascript.
 * This marshaller can be tweaked to output Javascript in an HTML context.
 * This class works in concert with CallScriptConduit, they should be
 * considered closely related and it is important to understand what one does
 * while editing the other.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BaseCallHandler extends BaseDwrpHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        boolean scriptPrefixSent = false;

        try
        {
            CallBatch batch = new CallBatch(request);

            // Security checks first, once we've parsed the input
            checkGetAllowed(batch);
            boolean checkCsrf = false;
            for (int i = 0; i < batch.getCalls().getCallCount(); i++)
            {
                Call call = batch.getCalls().getCall(i);
                if (!ExportUtil.isUnprotectedSystemMethod(call.getScriptName(), call.getMethodName()))
                {
                    checkCsrf = true;
                    break;
                }
            }
            if (checkCsrf) {
                checkNotCsrfAttack(request, batch);
            }

            // Save the batch so marshallException can get at a batch id
            request.setAttribute(ATTRIBUTE_BATCH, batch);

            String normalizedPage = pageNormalizer.normalizePage(batch.getPage());
            RealScriptSession scriptSession = scriptSessionManager.getOrCreateScriptSession(batch.getScriptSessionId(), normalizedPage, request.getSession(false));
            RealWebContext webContext = (RealWebContext) WebContextFactory.get();
            webContext.initialize(normalizedPage, scriptSession);

            // Various bits of the CallBatch need to be stashed away places
            storeParsedRequest(request, webContext, batch);

            Calls calls = marshallInbound(batch);

            Replies replies = remoter.execute(calls);
            scriptPrefixSent = true; // we can set this here as marshallOutbound will send prefix before throwing any exception
            marshallOutbound(batch, replies, response);

            if (checkCsrf) {
                updateCsrfState(request, batch);
            }
        }
        catch (Exception ex)
        {
            if (debug)
            {
                if (LocalUtil.getRootCause(ex) instanceof IOException) {
                    log.debug("I/O error while processing batch", ex);
                } else {
                    log.warn("Exception while processing batch", ex);
                }
            }

            marshallException(request, response, ex, !scriptPrefixSent);
        }
    }

    /**
     * Convert batch into calls.
     * @param batch The data we've parsed from the request
     * @return The function calls to make
     */
    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    public Calls marshallInbound(CallBatch batch)
    {
        Calls calls = batch.getCalls();

        // Debug the environment
        /*
        if (log.isDebugEnabled() && calls.getCallCount() > 0)
        {
            // We can just use 0 because they are all shared
            InboundContext inctx = batch.getInboundContexts().get(0);
            StringBuffer buffer = new StringBuffer();

            for (Iterator<String> it = inctx.getInboundVariableNames(); it.hasNext();)
            {
                String key = it.next();
                InboundVariable value = inctx.getInboundVariable(key);
                if (key.startsWith(ProtocolConstants.INBOUND_CALLNUM_PREFIX) &&
                    key.contains(ProtocolConstants.INBOUND_CALLNUM_SUFFIX + ProtocolConstants.INBOUND_KEY_ENV))
                {
                    buffer.append(key);
                    buffer.append('=');
                    buffer.append(value.toString());
                    buffer.append(", ");
                }
            }

            if (buffer.length() > 0)
            {
                log.debug("Environment: " + buffer.toString());
            }
        }
        //*/

        callLoop:
        for (int callNum = 0; callNum < calls.getCallCount(); callNum++)
        {
            Call call = calls.getCall(callNum);

            try
            {
                InboundContext inctx = batch.getInboundContexts().get(callNum);

                // Get a list of the available matching methods with the coerced
                // parameters that we will use to call it if we choose to use
                // that method.

                // Which method are we using?
                call.findMethod(moduleManager, converterManager, inctx, callNum);
                MethodDeclaration method = call.getMethodDeclaration();
                if (method == null)
                {
                    log.warn("No methods to match " + call.getScriptName() + '.' + call.getMethodName());
                    throw new IllegalArgumentException("Missing method or missing parameter converters");
                }

                // We are now sure we have the set of inputs lined up. They may
                // cross-reference so we do the de-referencing all in one go.
                // TODO: should we do this here? - why not earlier?
                // do we need to know the method before we dereference?
                inctx.dereference();

                // Convert all the parameters to the correct types
                int destParamCount = method.getParameterTypes().length;
                Object[] arguments = new Object[destParamCount];
                int inboundArgIndex = 0;
                for (int outboundArgIndex = 0; outboundArgIndex < destParamCount; outboundArgIndex++)
                {
                    InboundVariable param;
                    if (method.isVarArgs() && outboundArgIndex + 1 == destParamCount)
                    {
                        param = inctx.createArrayWrapper(callNum, destParamCount);
                    }
                    else
                    {
                        param = inctx.getParameter(callNum, inboundArgIndex);
                    }

                    Property property = new ParameterProperty(method, outboundArgIndex);

                    // TODO: Having just got a property, shouldn't we call property.getPropertyType() in place of this?
                    Class<?> paramType = method.getParameterTypes()[outboundArgIndex];
                    try
                    {
                        arguments[outboundArgIndex] = converterManager.convertInbound(paramType, param, property);
                    }
                    catch (Exception ex)
                    {
                        log.debug("Problem converting param=" + param + ", property=" + property + ", into paramType=" + paramType.getName() + ": " + ex);
                        throw ex;
                    }
                    // Only increment the inboundArgIndex if the parameterType of the destination method is not
                    // a Servlet class.  // In this case the arguments value is auto-populated by DWR and a place-holder
                    // argument is not passed from the client.
                    if (!LocalUtil.isServletClass(paramType)) {
                        inboundArgIndex++;
                    }
                }

                call.setParameters(arguments);
            }
            catch (Exception ex)
            {
                log.debug("Marshalling exception", ex);
                call.setMarshallFailure(ex);
                continue callLoop;
            }
        }

        return calls;
    }

    /**
     * Build a CallBatch and put it in the request
     * @param request Where we store the parsed data
     * @param webContext We need to notify others of some of the data we find
     * @param batch The parsed data to store
     */
    private void storeParsedRequest(HttpServletRequest request, RealWebContext webContext, CallBatch batch) throws IOException
    {
        // Remaining parameters get put into the request for later consumption
        Map<String, FormField> paramMap = batch.getExtraParameters();
        if (!paramMap.isEmpty())
        {
            for (Map.Entry<String, FormField> entry : paramMap.entrySet())
            {
                String key = entry.getKey();
                final FormField formField = entry.getValue();
                Object value;

                if (formField == null)
                {
                    log.warn("Found a parameter with a null value. This is likely to be due to a URL with an & before the query parameters. Please URL encode such pages.");
                    throw new IllegalArgumentException("Empty input parameter. See logs for suggestions");
                }

                if (formField.isFile())
                {
                    InputStreamFactory inFactory = new SimpleInputStreamFactory(formField.getInputStream());
                    value = new FileTransfer(formField.getName(), formField.getMimeType(), formField.getFileSize(), inFactory);
                }
                else
                {
                    value = formField.getString();
                }

                if (key.startsWith(ProtocolConstants.INBOUND_KEY_METADATA))
                {
                    request.setAttribute(key.substring(ProtocolConstants.INBOUND_KEY_METADATA.length()), value);
                    log.debug("Moved param to request: " + key + "=" + value);
                }
                else
                {
                    log.debug("Ignoring parameter: " + key + "=" + value);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Marshaller#marshallOutbound(org.directwebremoting.Replies, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void marshallOutbound(Batch batch, Replies replies, HttpServletResponse response) throws IOException
    {
        RealScriptSession scriptSession;
        try
        {
            scriptSession = (RealScriptSession) WebContextFactory.get().getScriptSession();
        }
        catch (SecurityException ex)
        {
            // If this is a call to System.pageUnloaded() or if something else
            // has caused the ScriptSession to expire, then we shouldn't do any
            // output, and just ignore the write. Officially there isn't
            // anything to write to anyway.
            return;
        }

        // Basic setup
        response.setContentType(getOutboundMimeType());
        PrintWriter out;

        // AccessLogLevel.getValue is null safe, it will always return an AccessLogLevel.
        if (debugScriptOutput || AccessLogLevel.getValue(this.accessLogLevel, debug).hierarchy() == 0)
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

        // Setup a debugging prefix
        if (out instanceof DebuggingPrintWriter)
        {
            DebuggingPrintWriter dpw = (DebuggingPrintWriter) out;
            dpw.setPrefix("out(" + replies.hashCode() + "): ");
        }

        // Send the script prefix (if any)
        sendOutboundScriptPrefix(out, batch.getInstanceId(), batch.getBatchId(), batch.getDocumentDomain());

        if (batch.getNextReverseAjaxIndex() != null)
        {
            out.println(ProtocolConstants.SCRIPT_CALL_INSERT);

            scriptSession.confirmScripts(batch.getNextReverseAjaxIndex() - 1);
            RealScriptSession.Scripts scripts = scriptSession.getScripts(batch.getNextReverseAjaxIndex());
            for(int i=0; i<scripts.getScripts().size(); i++) {
                sendScript(
                    out,
                    EnginePrivate.getRemoteHandleReverseAjaxScript(
                        scripts.getScriptIndexOffset() + i,
                        scripts.getScripts().get(i)));
            }
        }

        out.println(ProtocolConstants.SCRIPT_CALL_REPLY);

        String batchId = replies.getCalls().getBatchId();
        for (int i = 0; i < replies.getReplyCount(); i++)
        {
            Reply reply = replies.getReply(i);
            String callId = reply.getCallId();

            try
            {
                ScriptBuffer script;
                // The existence of a throwable indicates that something went wrong
                if (reply.getThrowable() != null)
                {
                    Throwable ex = reply.getThrowable();
                    script = EnginePrivate.getRemoteHandleExceptionScript(batchId, callId, ex);
                }
                else
                {
                    Object data = reply.getReply();
                    script = EnginePrivate.getRemoteHandleCallbackScript(batchId, callId, data);
                }
                sendScript(out, ScriptBufferUtil.createOutput(script, converterManager, jsonOutput));
            }
            catch (IOException ex)
            {
                // We're a bit stuck we died half way through writing so
                // we can't be sure the browser can react to the failure.
                // Since we can no longer do output we just log and end
                log.error("--Output Error: batchId[" + batchId + "] message[" + ex.toString() + ']', ex);
            }
            catch (ConversionException ex)
            {
                ScriptBuffer script = EnginePrivate.getRemoteHandleExceptionScript(batchId, callId, ex);
                addScriptHandleExceptions(out, script);
                log.warn("--ConversionException: batchId=" + batchId + " class=" + ex.getConversionType().getName(), ex);
            }
            catch (Exception ex)
            {
                // This is a bit of a "this can't happen" case so I am a bit
                // nervous about sending the exception to the client, but we
                // want to avoid silently dying so we need to do something.
                ScriptBuffer script = EnginePrivate.getRemoteHandleExceptionScript(batchId, callId, ex);
                addScriptHandleExceptions(out, script);
                log.error("--ConversionException: batchId=" + batchId + " message=" + ex.toString());
            }
        }

        sendOutboundScriptSuffix(out, replies.getCalls().getInstanceId(), replies.getCalls().getBatchId());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Marshaller#marshallException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Exception)
     */
    public void marshallException(HttpServletRequest request, HttpServletResponse response, Exception ex, boolean sendPrefix) throws IOException
    {
        response.setContentType(getOutboundMimeType());
        PrintWriter out = response.getWriter();
        Batch batch = (Batch) request.getAttribute(ATTRIBUTE_BATCH);

        String batchId = null;
        String instanceId = "0";
        String documentDomain = null;
        if (batch != null)
        {
            batchId = batch.getBatchId();
            instanceId = batch.getInstanceId();
            documentDomain = batch.getDocumentDomain();
        }

        if (sendPrefix)
        {
            sendOutboundScriptPrefix(out, instanceId, batchId, documentDomain);
        }
        String script = EnginePrivate.getRemoteHandleBatchExceptionScript(batchId, ex);
        out.println(script);
        sendOutboundScriptSuffix(out, instanceId, batchId);
    }

    /**
     * Marshall a Script without worrying about MarshallExceptions
     */
    public void addScriptHandleExceptions(PrintWriter out, ScriptBuffer script) throws IOException
    {
        try
        {
            sendScript(out, ScriptBufferUtil.createOutput(script, converterManager, jsonOutput));
        }
        catch (ConversionException ex)
        {
            log.warn("Error marshalling exception. Is the exception converter configured?", ex);
        }
    }

    /**
     * Send a script to the browser
     * @param out The stream to write to
     * @param script The script to send
     * @throws IOException If the write fails
     */
    protected abstract void sendScript(PrintWriter out, String script) throws IOException;

    /**
     * What mime type should we send to the browser for this data?
     * @return A mime-type
     */
    protected abstract String getOutboundMimeType();

    /**
     * iframe mode starts as HTML, so get into script mode
     * @param out The stream to write to
     * @param batchId The batch identifier so we can prepare the environment
     * @throws IOException If the write fails
     */
    protected abstract void sendOutboundScriptPrefix(PrintWriter out, String instanceId, String batchId, String documentDomain) throws IOException;

    /**
     * iframe mode needs to get out of script mode
     * @param out The stream to write to
     * @param batchId The batch identifier so we can prepare the environment
     * @throws IOException If the write fails
     */
    protected abstract void sendOutboundScriptSuffix(PrintWriter out, String instanceId, String batchId) throws IOException;

    /* (non-Javadoc)
     * @see org.directwebremoting.Marshaller#isConvertable(java.lang.Class)
     */
    public boolean isConvertable(Class<?> paramType)
    {
        return converterManager.isConvertable(paramType);
    }

    /**
     * Set the debug status
     * @param debug The new debug setting
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * Are we in debug mode?
     */
    protected boolean debug = false;

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
     * Do we debug all the scripts that we output?
     * @param debugScriptOutput true to debug all of the output scripts (verbose)
     */
    public void setDebugScriptOutput(boolean debugScriptOutput)
    {
        this.debugScriptOutput = debugScriptOutput;
    }

    /**
     * Do we debug all the scripts that we output?
     */
    protected boolean debugScriptOutput = false;

    /**
     * @return Are we outputting in JSON mode?
     */
    public boolean isJsonOutput()
    {
        return jsonOutput;
    }

    /**
     * @param jsonOutput Are we outputting in JSON mode?
     */
    public void setJsonOutput(boolean jsonOutput)
    {
        this.jsonOutput = jsonOutput;
    }

    /**
     * Are we outputting in JSON mode?
     */
    protected boolean jsonOutput = false;

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    public void setAccessLogLevel(String accessLogLevel)
    {
        this.accessLogLevel = accessLogLevel;
    }

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    protected String accessLogLevel = null;

    /**
     * Accessor for the PageNormalizer.
     * @param pageNormalizer The new PageNormalizer
     */
    public void setPageNormalizer(PageNormalizer pageNormalizer)
    {
        this.pageNormalizer = pageNormalizer;
    }

    /**
     * How we turn pages into the canonical form.
     */
    protected PageNormalizer pageNormalizer = null;

    /**
     * Accessor for the ScriptSessionManager that we configure
     * @param scriptSessionManager
     */
    public void setScriptSessionManager(ScriptSessionManager scriptSessionManager)
    {
        this.scriptSessionManager = scriptSessionManager;
    }

    /**
     * How we handle ScriptSessions
     */
    protected ScriptSessionManager scriptSessionManager = null;

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
     * How we stash away the results of the request parse
     */
    protected static final String ATTRIBUTE_BATCH = "org.directwebremoting.dwrp.batch";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BaseCallHandler.class);
}
