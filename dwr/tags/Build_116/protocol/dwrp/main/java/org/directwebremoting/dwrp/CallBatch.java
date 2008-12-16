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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.ConvertUtil;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.util.LocalUtil;

/**
 * A container for all the by-products of an HttpRequest parse
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CallBatch extends Batch
{
    /**
     * Parse an inbound request into a Calls object
     * @param request The original browser's request
     */
    public CallBatch(HttpServletRequest request) throws ServerException
    {
        super(request);
        parseParameters();
    }

    /**
     * Ctor for the Bayeux client which doesn't have requests and responses
     * @param allParameters A set of name value pairs
     * @param get This this request come from a get request
     * @throws SecurityException If the parameters can't be decoded
     */
    public CallBatch(Map<String, FormField> allParameters, boolean get)
    {
        super(allParameters, get);
        parseParameters();
    }

    /**
     * Fish out the important parameters
     * @throws SecurityException If the parsing of input parameter fails
     */
    private void parseParameters()
    {
        // Work out how many calls are in this packet
        String callStr = extractParameter(ProtocolConstants.INBOUND_CALL_COUNT);
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

        // Extract the batch id
        String batchId = extractParameter(ProtocolConstants.INBOUND_KEY_BATCHID);
        calls.setBatchId(batchId);
        if (!LocalUtil.isLetterOrDigitOrUnderline(batchId))
        {
            throw new SecurityException("CallBatch IDs may only contain Java Identifiers");
        }

        // Extract the ids, script names and method names
        for (int callNum = 0; callNum < callCount; callNum++)
        {
            InboundContext inboundContext = new InboundContext();
            inboundContexts.add(inboundContext);

            String prefix = ProtocolConstants.INBOUND_CALLNUM_PREFIX + callNum + ProtocolConstants.INBOUND_CALLNUM_SUFFIX;

            // The special values
            String callId = extractParameter(prefix + ProtocolConstants.INBOUND_KEY_ID);
            if (!LocalUtil.isLetterOrDigitOrUnderline(callId))
            {
                throw new SecurityException("Call IDs may only contain Java Identifiers");
            }

            String scriptName = extractParameter(prefix + ProtocolConstants.INBOUND_KEY_SCRIPTNAME);
            if (!LocalUtil.isLetterOrDigitOrUnderline(scriptName))
            {
                throw new SecurityException("Script names may only contain Java Identifiers");
            }

            String methodName = extractParameter(prefix + ProtocolConstants.INBOUND_KEY_METHODNAME);
            if (!LocalUtil.isLetterOrDigitOrUnderline(methodName))
            {
                throw new SecurityException("Method names may only contain Java Identifiers");
            }

            // Look for parameters to this method
            for (Iterator<Map.Entry<String, FormField>> it = getExtraParameters().entrySet().iterator(); it.hasNext();)
            {
                Map.Entry<String, FormField> entry = it.next();
                String key = entry.getKey();

                if (key.startsWith(prefix))
                {
                    FormField formField = entry.getValue();
                    if (formField.isFile())
                    {
                        inboundContext.createInboundVariable(callNum, key, ProtocolConstants.TYPE_FILE, formField);
                    }
                    else
                    {
                        String[] split = ConvertUtil.splitInbound(formField.getString());

                        String value = split[ConvertUtil.INBOUND_INDEX_VALUE];
                        String type = split[ConvertUtil.INBOUND_INDEX_TYPE];
                        inboundContext.createInboundVariable(callNum, key, type, value);
                    }
                    it.remove();
                }
            }

            Call call = new Call(callId, scriptName, methodName);
            calls.addCall(call);
        }
    }

    /**
     * @return the inboundContexts
     */
    public List<InboundContext> getInboundContexts()
    {
        return inboundContexts;
    }

    /**
     * There is one inbound context to keep track of the conversions that are
     * done for each call.
     */
    private final List<InboundContext> inboundContexts = new ArrayList<InboundContext>();

    /**
     * @return the calls
     */
    public Calls getCalls()
    {
        return calls;
    }

    /**
     * The list of calls in the batch
     */
    private final Calls calls = new Calls();

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "CallBatch[page=" + getPage() + ",scriptSessionId=" + getScriptSessionId() + "]";
    }

    /**
     * We don't want to allow too many calls in a batch
     */
    private final int maxCallsPerBatch = 1000;
}
