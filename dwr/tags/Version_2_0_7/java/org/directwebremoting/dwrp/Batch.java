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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
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
     * @param crossDomainSessionSecurity Are we checking for CSRF attacks
     * @param allowGetForSafariButMakeForgeryEasier Do we allow GET?
     * @param sessionCookieName "JSESSIONID" unless it has been overridden
     * @throws ServerException If reading from the request body stream fails
     */
    public Batch(HttpServletRequest request, boolean crossDomainSessionSecurity, boolean allowGetForSafariButMakeForgeryEasier, String sessionCookieName) throws ServerException
    {
        boolean isGet = request.getMethod().equals("GET");
        if (isGet)
        {
            setAllParameters(ParseUtil.parseGet(request));
        }
        else
        {
            setAllParameters(ParseUtil.parsePost(request));
        }

        parseParameters();

        if (!allowGetForSafariButMakeForgeryEasier && isGet)
        {
            log.error("GET is disallowed because it makes request forgery easier. See http://getahead.org/dwr/security/allowGetForSafariButMakeForgeryEasier for more details.");
            throw new SecurityException("GET Disalowed");
        }

        if (crossDomainSessionSecurity)
        {
            checkNotCsrfAttack(request, sessionCookieName);
        }
    }

    /**
     * @return the allParameters
     */
    public Map getAllParameters()
    {
        return new HashMap(allParameters);
    }

    /**
     * @param allParameters the allParameters to set
     */
    public void setAllParameters(Map allParameters)
    {
        this.allParameters = allParameters;
    }

    /**
     * @return the inboundContexts
     */
    public List getInboundContexts()
    {
        return inboundContexts;
    }

    /**
     * @param inboundContexts the inboundContexts to set
     */
    public void setInboundContexts(List inboundContexts)
    {
        this.inboundContexts = inboundContexts;
    }

    /**
     * @return the spareParameters
     */
    public Map getSpareParameters()
    {
        return spareParameters;
    }

    /**
     * @param spareParameters the spareParameters to set
     */
    public void setSpareParameters(Map spareParameters)
    {
        this.spareParameters = spareParameters;
    }

    /**
     * @return the page
     */
    public String getPage()
    {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(String page)
    {
        this.page = page;
    }

    /**
     * @return the scriptSessionId
     */
    public String getScriptSessionId()
    {
        return scriptSessionId;
    }

    /**
     * @param scriptSessionId the scriptSessionId to set
     */
    public void setScriptSessionId(String scriptSessionId)
    {
        this.scriptSessionId = scriptSessionId;
    }

    /**
     * @return the httpSessionId
     */
    public String getHttpSessionId()
    {
        return httpSessionId;
    }

    /**
     * @param httpSessionId the httpSessionId to set
     */
    public void setHttpSessionId(String httpSessionId)
    {
        this.httpSessionId = httpSessionId;
    }

    /**
     * @return the calls
     */
    public Calls getCalls()
    {
        return calls;
    }

    /**
     * @param calls the calls to set
     */
    public void setCalls(Calls calls)
    {
        this.calls = calls;
    }

    /**
     * Check that this request is not subject to a CSRF attack
     * @param request The original browser's request
     * @param sessionCookieName "JSESSIONID" unless it has been overridden
     */
    private void checkNotCsrfAttack(HttpServletRequest request, String sessionCookieName)
    {
        // A check to see that this isn't a csrf attack
        // http://en.wikipedia.org/wiki/Cross-site_request_forgery
        // http://www.tux.org/~peterw/csrf.txt
        if (request.isRequestedSessionIdValid() && request.isRequestedSessionIdFromCookie())
        {
            String headerSessionId = request.getRequestedSessionId();
            if (headerSessionId.length() > 0)
            {
                String bodySessionId = getHttpSessionId();

                // Normal case; if same session cookie is supplied by DWR and
                // in HTTP header then all is ok
                if (headerSessionId.equals(bodySessionId))
                {
                    return;
                }

                // Weblogic adds creation time to the end of the incoming
                // session cookie string (even for request.getRequestedSessionId()).
                // Use the raw cookie instead
                Cookie[] cookies = request.getCookies();
                for (int i = 0; i < cookies.length; i++)
                {
                    Cookie cookie = cookies[i];
                    if (cookie.getName().equals(sessionCookieName) &&
                            cookie.getValue().equals(bodySessionId))
                    {
                        return;
                    }
                }

                // Otherwise error
                log.error("A request has been denied as a potential CSRF attack.");
                throw new SecurityException("Session Error");
            }
        }
    }

    /**
     * Fish out the important parameters
     * @throws ServerException If the parsing of input parameter fails
     */
    protected void parseParameters() throws ServerException
    {
        Map paramMap = getAllParameters();
        calls = new Calls();

        // Work out how many calls are in this packet
        String callStr = (String) paramMap.remove(ProtocolConstants.INBOUND_CALL_COUNT);
        int callCount;
        try
        {
            callCount = Integer.parseInt(callStr);
        }
        catch (NumberFormatException ex)
        {
            throw new ServerException(Messages.getString("BaseCallMarshaller.BadCallCount"));
        }

        // Extract the ids, scriptnames and methodnames
        for (int callNum = 0; callNum < callCount; callNum++)
        {
            Call call = new Call();
            calls.addCall(call);

            InboundContext inctx = new InboundContext();
            inboundContexts.add(inctx);

            String prefix = ProtocolConstants.INBOUND_CALLNUM_PREFIX + callNum + ProtocolConstants.INBOUND_CALLNUM_SUFFIX;

            // The special values
            String callId = (String) paramMap.remove(prefix + ProtocolConstants.INBOUND_KEY_ID);
            call.setCallId(callId);
            if (!LocalUtil.isLetterOrDigitOrUnderline(callId))
            {
                throw new SecurityException("Call IDs may only contain Java Identifiers");
            }

            String scriptName = (String) paramMap.remove(prefix + ProtocolConstants.INBOUND_KEY_SCRIPTNAME);
            call.setScriptName(scriptName);
            if (!LocalUtil.isLetterOrDigitOrUnderline(scriptName))
            {
                throw new SecurityException("Script names may only contain Java Identifiers");
            }

            String methodName = (String) paramMap.remove(prefix + ProtocolConstants.INBOUND_KEY_METHODNAME);
            call.setMethodName(methodName);
            if (!LocalUtil.isLetterOrDigitOrUnderline(methodName))
            {
                throw new SecurityException("Method names may only contain Java Identifiers");
            }

            // Look for parameters to this method
            for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();

                if (key.startsWith(prefix))
                {
                    String data = (String) entry.getValue();
                    String[] split = ParseUtil.splitInbound(data);

                    String value = split[LocalUtil.INBOUND_INDEX_VALUE];
                    String type = split[LocalUtil.INBOUND_INDEX_TYPE];
                    inctx.createInboundVariable(callNum, key, type, value);
                    it.remove();
                }
            }
        }

        String batchId = (String) paramMap.remove(ProtocolConstants.INBOUND_KEY_BATCHID);
        calls.setBatchId(batchId);
        if (!LocalUtil.isLetterOrDigitOrUnderline(batchId))
        {
            throw new SecurityException("Batch IDs may only contain Java Identifiers");
        }

        httpSessionId = (String) paramMap.remove(ProtocolConstants.INBOUND_KEY_HTTP_SESSIONID);
        scriptSessionId = (String) paramMap.remove(ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID);
        page = (String) paramMap.remove(ProtocolConstants.INBOUND_KEY_PAGE);

        for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (key.startsWith(ProtocolConstants.INBOUND_KEY_METADATA))
            {
                spareParameters.put(key.substring(ProtocolConstants.INBOUND_KEY_METADATA.length()), value);
            }
        }
    }

    private List inboundContexts = new ArrayList();

    private String scriptSessionId;

    private String httpSessionId;

    private String page;

    private Calls calls;

    private Map allParameters = new HashMap();

    private Map spareParameters = new HashMap();

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(Batch.class);
}
