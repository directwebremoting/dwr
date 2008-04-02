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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * A container for all the by-products of an HttpRequest parse
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Batch
{
    /**
     * Parse an inbound request into a Calls object
     * @param request The original browser's request
     */
    public Batch(HttpServletRequest request) throws ServerException
    {
        get = "GET".equals(request.getMethod());

        ParseUtil parseUtil = new ParseUtil();
        allParameters = parseUtil.parseRequest(request);

        parseParameters();
    }

    /**
     * Ctor for the Bayeux client which doesn't have requests and responses
     * @param allParameters A set of name value pairs
     * @throws SecurityException If the parameters can't be decoded
     */
    public Batch(Map<String, FormField> allParameters)
    {
        this.allParameters = allParameters;
        parseParameters();
    }

    /**
     * Fish out the important parameters
     * @throws SecurityException If the parsing of input parameter fails
     */
    protected void parseParameters()
    {
        Map<String, FormField> paramMap = new HashMap<String, FormField>(allParameters);
        calls = new Calls();

        // Work out how many calls are in this packet
        String callStr = extractParameter(paramMap, ProtocolConstants.INBOUND_CALL_COUNT);
        int callCount;
        try
        {
            callCount = Integer.parseInt(callStr);
        }
        catch (NumberFormatException ex)
        {
            throw new SecurityException("Invalid Call Count");
        }

        if (callCount > maxCallsPerBatch)
        {
            throw new SecurityException("Too many calls in a batch");
        }

        // Extract the ids, script names and method names
        for (int callNum = 0; callNum < callCount; callNum++)
        {
            Call call = new Call();
            calls.addCall(call);

            InboundContext inctx = new InboundContext();
            inboundContexts.add(inctx);

            String prefix = ProtocolConstants.INBOUND_CALLNUM_PREFIX + callNum + ProtocolConstants.INBOUND_CALLNUM_SUFFIX;

            // The special values
            String callId = extractParameter(paramMap, prefix + ProtocolConstants.INBOUND_KEY_ID);
            call.setCallId(callId);
            if (!LocalUtil.isLetterOrDigitOrUnderline(callId))
            {
                throw new SecurityException("Call IDs may only contain Java Identifiers");
            }

            String scriptName = extractParameter(paramMap, prefix + ProtocolConstants.INBOUND_KEY_SCRIPTNAME);
            call.setScriptName(scriptName);
            if (!LocalUtil.isLetterOrDigitOrUnderline(scriptName))
            {
                throw new SecurityException("Script names may only contain Java Identifiers");
            }

            String methodName = extractParameter(paramMap, prefix + ProtocolConstants.INBOUND_KEY_METHODNAME);
            call.setMethodName(methodName);
            if (!LocalUtil.isLetterOrDigitOrUnderline(methodName))
            {
                throw new SecurityException("Method names may only contain Java Identifiers");
            }

            // Look for parameters to this method
            for (Iterator<Map.Entry<String, FormField>> it = paramMap.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry<String, FormField> entry = it.next();
                String key = entry.getKey();

                if (key.startsWith(prefix))
                {
                    FormField formField = entry.getValue();
                    if (formField.isFile())
                    {
                        inctx.createInboundVariable(callNum, key, ProtocolConstants.TYPE_FILE, formField); 
                    }
                    else
                    {
                        String[] split = ParseUtil.splitInbound(formField.getString());

                        String value = split[LocalUtil.INBOUND_INDEX_VALUE];
                        String type = split[LocalUtil.INBOUND_INDEX_TYPE];
                        inctx.createInboundVariable(callNum, key, type, value);
                    }
                    it.remove();
                }
            }
        }

        String batchId = extractParameter(paramMap, ProtocolConstants.INBOUND_KEY_BATCHID);
        calls.setBatchId(batchId);
        if (!LocalUtil.isLetterOrDigitOrUnderline(batchId))
        {
            throw new SecurityException("Batch IDs may only contain Java Identifiers");
        }

        httpSessionId = extractParameter(paramMap, ProtocolConstants.INBOUND_KEY_HTTP_SESSIONID);
        scriptSessionId = extractParameter(paramMap, ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        page = extractParameter(paramMap, ProtocolConstants.INBOUND_KEY_PAGE);
        windowName = extractParameter(allParameters, ProtocolConstants.INBOUND_KEY_WINDOWNAME);

        for (Map.Entry<String, FormField> entry : paramMap.entrySet())
        {
            String key = entry.getKey();
            FormField value = entry.getValue();
            if (key.startsWith(ProtocolConstants.INBOUND_KEY_METADATA))
            {
                spareParameters.put(key.substring(ProtocolConstants.INBOUND_KEY_METADATA.length()), value);
            }
        }
    }

    /**
     * Extract a parameter and ensure it is in the request.
     * This is needed to cope with Jetty continuations that are not real
     * continuations.
     * @param parameters The parameter list parsed out of the request
     * @param paramName The name of the parameter sent
     * @return The found value
     */
    protected String extractParameter(Map<String, FormField> parameters, String paramName)
    {
        FormField formField = parameters.remove(paramName);
        if (formField == null)
        {
            throw new IllegalArgumentException(Messages.getString("PollHandler.MissingParameter", paramName));
        }

        return formField.getString();
    }

    /**
     * @return the inboundContexts
     */
    public List<InboundContext> getInboundContexts()
    {
        return inboundContexts;
    }

    /**
     * @return the spareParameters
     */
    public Map<String, FormField> getSpareParameters()
    {
        return spareParameters;
    }

    /**
     * @return the calls
     */
    public Calls getCalls()
    {
        return calls;
    }

    /**
     * @return the httpSessionId
     */
    public String getHttpSessionId()
    {
        return httpSessionId;
    }

    /**
     * @return the scriptSessionId
     */
    public String getScriptSessionId()
    {
        return scriptSessionId;
    }

    /**
     * @return the page
     */
    public String getPage()
    {
        return page;
    }

    /**
     * @return the window name
     */
    public String getWindowName()
    {
        return windowName;
    }

    /**
     * Is this request from a GET?
     * @return true if the request is a GET request
     */
    public boolean isGet()
    {
        return get;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Batch[page=" + page + ",scriptSessionId=" + scriptSessionId + "]";
    }

    /**
     * There is one inbound context to keep track of the conversions that are
     * done for each call.
     */
    private List<InboundContext> inboundContexts = new ArrayList<InboundContext>();

    /**
     * We don't want to allow too many calls in a batch
     */
    private int maxCallsPerBatch = 1000;

    /**
     * The list of calls in the batch
     */
    private Calls calls;

    /**
     * The unique ID sent to the browser in the session cookie
     */
    private String httpSessionId;

    /**
     * The page that the request was sent from
     */
    private String page;

    /**
     * Window name is used by reverse ajax to get around the 2 connection limit
     */
    private String windowName;

    /**
     * The unique ID sent to the current page
     */
    private String scriptSessionId;

    /**
     * Is it a GET request?
     */
    private boolean get;

    /**
     * All the parameters sent by the browser
     */
    private Map<String, FormField> allParameters;

    /**
     * The unused parameters
     */
    private Map<String, FormField> spareParameters = new HashMap<String, FormField>();

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(Batch.class);
}
