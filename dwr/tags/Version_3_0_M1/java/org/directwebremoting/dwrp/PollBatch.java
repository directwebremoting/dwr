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

import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.util.Messages;

/**
 * A container for the information passed in by an Poll request
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PollBatch
{
    /**
     * @param request The request to parse to fill out this batch
     * @throws ServerException
     */
    public PollBatch(HttpServletRequest request) throws ServerException
    {
        debug = request.getHeader("User-Agent");
        get = "GET".equals(request.getMethod());

        ParseUtil parseUtil = new ParseUtil();
        allParameters = parseUtil.parseRequest(request);

        parseParameters();
    }

    /**
     * Fish out the important parameters
     */
    protected void parseParameters()
    {
        batchId = extractParameter(allParameters, ProtocolConstants.INBOUND_KEY_BATCHID);
        scriptSessionId = extractParameter(allParameters, ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        page = extractParameter(allParameters, ProtocolConstants.INBOUND_KEY_PAGE);
        windowName = extractParameter(allParameters, ProtocolConstants.INBOUND_KEY_WINDOWNAME);
        String prString = extractParameter(allParameters, ProtocolConstants.INBOUND_KEY_PARTIAL_RESPONSE);
        partialResponse = PartialResponse.fromOrdinal(prString);
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
     * @return the batchId
     */
    public String getBatchId()
    {
        return batchId;
    }

    /**
     * @return the partialResponse
     */
    public PartialResponse getPartialResponse()
    {
        return partialResponse;
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
        return "PollBatch[partResp=" + partialResponse + ",debug=" + debug + "]";
    }

    /**
     * The ID of this batch from the browser
     */
    private String batchId;

    /**
     * Does the browser want partial responses?
     */
    private PartialResponse partialResponse;

    /**
     * A quick string for debug purposes
     */
    private String debug;

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
}
