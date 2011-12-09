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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.PageNormalizer;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.util.Messages;

/**
 * A container for the information passed in by an Poll request
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PollBatch
{
    /**
     * @param request
     * @param pageNormalizer 
     * @throws ServerException 
     */
    public PollBatch(HttpServletRequest request, PageNormalizer pageNormalizer) throws ServerException
    {
        debug = request.getHeader("User-Agent");

        get = "GET".equals(request.getMethod());
        Map parameters = null;
        if (get)
        {
            parameters = ParseUtil.parseGet(request);
        }
        else
        {
            parameters = ParseUtil.parsePost(request);
        }

        batchId = extractParameter(parameters, ProtocolConstants.INBOUND_KEY_BATCHID);
        scriptId = extractParameter(parameters, ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        httpSessionId = extractParameter(parameters, ProtocolConstants.INBOUND_KEY_HTTP_SESSIONID);
        String page = extractParameter(parameters, ProtocolConstants.INBOUND_KEY_PAGE);
        String prString = extractParameter(parameters, ProtocolConstants.INBOUND_KEY_PARTIAL_RESPONSE);
        partialResponse = PartialResponse.fromOrdinal(prString);

        // We must parse the parameters before we setup the conduit because it's
        // only after doing this that we know the scriptSessionId
        WebContext webContext = WebContextFactory.get();
        // Various bits of parseResponse need to be stashed away places
        String normalizedPage = pageNormalizer.normalizePage(page);
        webContext.setCurrentPageInformation(normalizedPage, scriptId);

        scriptSession = (RealScriptSession) webContext.getScriptSession();
    }

    /**
     * Extract a parameter and ensure it is in the request.
     * This is needed to cope with Jetty continuations that are not real
     * continuations.
     * @param parameters The parameter list parsed out of the request
     * @param paramName The name of the parameter sent
     * @return The found value
     */
    protected String extractParameter(Map parameters, String paramName)
    {
        String id = (String) parameters.remove(paramName);
        if (id == null)
        {
            throw new IllegalArgumentException(Messages.getString("PollHandler.MissingParameter", paramName));
        }

        return id;
    }

    /**
     * @return the batchId
     */
    public String getBatchId()
    {
        return batchId;
    }

    /**
     * @return the scriptId
     */
    public String getScriptId()
    {
        return scriptId;
    }

    /**
     * @return the partialResponse
     */
    public int getPartialResponse()
    {
        return partialResponse;
    }

    /**
     * @return the get
     */
    public boolean isGet()
    {
        return get;
    }

    /**
     * @return the scriptSession
     */
    public RealScriptSession getScriptSession()
    {
        return scriptSession;
    }

    /**
     * @return
     */
    public String getHttpSessionId()
    {
        return httpSessionId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "PollBatch[partResp=" + partialResponse + ",debug=" + debug + "]";
    }

    /**
     * The ID of this batch from the browser
     */
    private String batchId;

    /**
     * The id of the page
     */
    private String scriptId;

    /**
     * Does the browser want partial responses?
     */
    private int partialResponse;

    /**
     * Is it a GET request?
     */
    private boolean get;

    /**
     * A quick string for debug purposes
     */
    private String debug;

    /**
     * The HTTP session from which this came
     */
    private final String httpSessionId;

    /**
     * The script session backing the script id
     */
    private RealScriptSession scriptSession;

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_PARAMETERS = "org.directwebremoting.dwrp.parameters";

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_CALL_ID = "org.directwebremoting.dwrp.callId";

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_SESSION_ID = "org.directwebremoting.dwrp.sessionId";

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_PAGE = "org.directwebremoting.dwrp.page";

    /**
     * How we stash away the results of the request parse
     */
    public static final String ATTRIBUTE_PARTIAL_RESPONSE = "org.directwebremoting.dwrp.partialResponse";

    /**
     * We remember the notify conduit so we can reuse it
     */
    public static final String ATTRIBUTE_NOTIFY_CONDUIT = "org.directwebremoting.dwrp.notifyConduit";
}
