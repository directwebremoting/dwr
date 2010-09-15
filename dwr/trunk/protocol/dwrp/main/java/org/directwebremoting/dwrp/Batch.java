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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.util.LocalUtil;

/**
 * A Batch is a request from the client.
 * This can be either a from a call ({@link CallBatch}) or an active reverse
 * ajax poll ({@link PollBatch})
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Batch
{
    /**
     * Initialize the batch from an {@link HttpServletRequest}
     */
    public Batch(HttpServletRequest request) throws ServerException
    {
        get = "GET".equals(request.getMethod());
        if (get)
        {
            extraParameters = parseGet(request);
        }
        else
        {
            extraParameters = parsePost(request);
        }

        scriptSessionId = extractParameter(ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID, null);
        if (scriptSessionId.contains("/"))
        {
            dwrSessionId = scriptSessionId.substring(0, scriptSessionId.indexOf('/'));
        }
        else
        {
            dwrSessionId = "";
        }
        page = LocalUtil.urlDecode(extractParameter(ProtocolConstants.INBOUND_KEY_PAGE, THROW));
        windowName = extractParameter(ProtocolConstants.INBOUND_KEY_WINDOWNAME, THROW);
    }

    /**
     * Initialize the batch from a set of pre-parsed parameters
     */
    public Batch(Map<String, FormField> allParameters, boolean get)
    {
        this.extraParameters = allParameters;
        this.get = get;

        scriptSessionId = extractParameter(ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID, null);
        if (scriptSessionId.contains("/"))
        {
            dwrSessionId = scriptSessionId.substring(0, scriptSessionId.indexOf('/'));
        }
        else
        {
            dwrSessionId = "";
        }
        page = extractParameter(ProtocolConstants.INBOUND_KEY_PAGE, THROW);
        windowName = extractParameter(ProtocolConstants.INBOUND_KEY_WINDOWNAME, THROW);
    }

    /**
     * Extract a parameter and ensure it is in the request.
     * This is needed to cope with Jetty continuations that are not real
     * continuations.
     * @param paramName The name of the parameter sent
     * @return The found value
     */
    protected String extractParameter(String paramName, String defaultValue)
    {
        FormField formField = extraParameters.remove(paramName);
        if (formField != null)
        {
            return formField.getString();
        }

        if (defaultValue == THROW)
        {
            throw new IllegalArgumentException("Failed to find parameter: " + paramName);
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * Parse an HTTP POST request to fill out the scriptName, methodName and
     * paramList properties. This method should not fail unless it will not
     * be possible to return any sort of error to the user. Failure cases should
     * be handled by the <code>checkParams()</code> method.
     * @param req The original browser's request
     * @return The equivalent of HttpServletRequest.getParameterMap() for now
     * @throws ServerException If reading from the request body stream fails
     */
    private Map<String, FormField> parsePost(HttpServletRequest req) throws ServerException
    {
        Map<String, FormField> paramMap;

        if (isMultipartContent(req))
        {
            paramMap = UPLOADER.parseRequest(req);
        }
        else
        {
            paramMap = parseBasicPost(req);
        }

        // If there is only 1 param then this must be a broken Safari.
        if (paramMap.size() == 1)
        {
            parseBrokenMacPost(paramMap);
        }

        return paramMap;
    }

    /**
     * Utility method that determines whether the request contains multipart
     * content.
     * @param request The servlet request to be evaluated. Must be non-null.
     * @return true if the request is multipart, false otherwise.
     */
    public static boolean isMultipartContent(HttpServletRequest request)
    {
        if (!"post".equals(request.getMethod().toLowerCase()))
        {
            return false;
        }

        String contentType = request.getContentType();
        if (contentType == null)
        {
            return false;
        }

        if (contentType.toLowerCase().startsWith("multipart/"))
        {
            return true;
        }

        return false;
    }

    /**
     * The default parse case for a normal form submit
     * @param req The http request
     * @return a map of parsed parameters
     * @throws ServerException
     */
    @SuppressWarnings("unchecked")
    private Map<String, FormField> parseBasicPost(HttpServletRequest req) throws ServerException
    {
        Map<String, FormField> paramMap;
        paramMap = new HashMap<String, FormField>();

        BufferedReader in = null;
        try
        {
            String charEncoding = req.getCharacterEncoding();
            if (charEncoding != null)
            {
                in = new BufferedReader(new InputStreamReader(req.getInputStream(), charEncoding));
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(req.getInputStream()));
            }

            while (true)
            {
                String line = in.readLine();

                if (line == null)
                {
                    if (paramMap.isEmpty())
                    {
                        // Normally speaking we should just bail out, but if
                        // we are using DWR with Acegi without ActiveX on IE,
                        // then Acegi 'fixes' the parameters for us.
                        Enumeration<String> en = req.getParameterNames();
                        while (en.hasMoreElements())
                        {
                            String name = en.nextElement();
                            paramMap.put(name, new FormField(req.getParameter(name)));
                        }
                    }

                    break;
                }

                if (line.indexOf('&') != -1)
                {
                    // If there are any &'s then this must be iframe post and all the
                    // parameters have got dumped on one line, split with &
                    log.debug("Using iframe POST mode");
                    StringTokenizer st = new StringTokenizer(line, "&");
                    while (st.hasMoreTokens())
                    {
                        String part = st.nextToken();
                        part = LocalUtil.urlDecode(part);

                        parsePostLine(part, paramMap);
                    }
                }
                else
                {
                    // Hooray, this is a normal one!
                    parsePostLine(line, paramMap);
                }
            }
        }
        catch (Exception ex)
        {
            throw new ServerException("Failed to read input", ex);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException ex)
                {
                    // Ignore
                }
            }
        }
        return paramMap;
    }

    /**
     * All the parameters have got dumped on one line split with \n
     * See: http://bugzilla.opendarwin.org/show_bug.cgi?id=3565
     *      https://dwr.dev.java.net/issues/show_bug.cgi?id=93
     *      http://jira.atlassian.com/browse/JRA-8354
     *      http://developer.apple.com/internet/safari/uamatrix.html
     * @param paramMap The broken parsed parameter
     */
    private static void parseBrokenMacPost(Map<String, FormField> paramMap)
    {
        // This looks like a broken Mac where the line endings are confused
        log.debug("Using Broken Safari POST mode");

        // Iterators insist that we call hasNext() before we start
        Iterator<Map.Entry<String, FormField>> it = paramMap.entrySet().iterator();
        if (!it.hasNext())
        {
            throw new IllegalStateException("No entries in non empty map!");
        }

        // So get the first
        Map.Entry<String, FormField> entry = it.next();
        String key = entry.getKey();
        String value = entry.getValue().getString();
        String line = key + ProtocolConstants.INBOUND_DECL_SEPARATOR + value;

        StringTokenizer st = new StringTokenizer(line, "\n");
        while (st.hasMoreTokens())
        {
            String part = st.nextToken();
            part = LocalUtil.urlDecode(part);

            parsePostLine(part, paramMap);
        }
    }

    /**
     * Sort out a single line in a POST request
     * @param line The line to parse
     * @param paramMap The map to add parsed parameters to
     */
    private static void parsePostLine(String line, Map<String, FormField> paramMap)
    {
        if (line.length() == 0)
        {
            return;
        }

        int sep = line.indexOf(ProtocolConstants.INBOUND_DECL_SEPARATOR);
        if (sep == -1)
        {
            paramMap.put(line, null);
        }
        else
        {
            String key = line.substring(0, sep);
            String value = line.substring(sep  + ProtocolConstants.INBOUND_DECL_SEPARATOR.length());

            paramMap.put(key, new FormField(value));
        }
    }

    /**
     * Parse an HTTP GET request to fill out the scriptName, methodName and
     * paramList properties. This method should not fail unless it will not
     * be possible to return any sort of error to the user. Failure cases should
     * be handled by the <code>checkParams()</code> method.
     * @param req The original browser's request
     * @return Simply HttpRequest.getParameterMap() for now
     * @throws ServerException If the parsing fails
     */
    @SuppressWarnings("unchecked")
    private Map<String, FormField> parseGet(HttpServletRequest req) throws ServerException
    {
        Map<String, FormField> convertedMap = new HashMap<String, FormField>();
        Map<String, String[]> paramMap = req.getParameterMap();

        for (Map.Entry<String, String[]> entry : paramMap.entrySet())
        {
            String key = entry.getKey();
            String[] array = entry.getValue();

            if (array.length == 1)
            {
                convertedMap.put(key, new FormField(array[0]));
            }
            else
            {
                log.error("Multiple values for key: " + key);
                throw new ServerException("Multiple values for key. See console for more information");
            }
        }

        return convertedMap;
    }

    /**
     * Is this request from a GET?
     * @return true if the request is a GET request
     */
    public boolean isGet()
    {
        return get;
    }

    /**
     * Is it a GET request?
     */
    private final boolean get;

    /**
     * @return the scriptSessionId
     */
    public String getScriptSessionId()
    {
        return scriptSessionId;
    }

    /**
     * The unique ID sent to the current page
     */
    private final String scriptSessionId;

    /**
     * @return the httpSessionId
     */
    public String getDwrSessionId()
    {
        return dwrSessionId;
    }

    /**
     * The unique ID sent to the browser in the session cookie
     */
    private final String dwrSessionId;

    /**
     * @return the page
     */
    public String getPage()
    {
        return page;
    }

    /**
     * The page that the request was sent from
     */
    private final String page;

    /**
     * @return the window name
     */
    public String getWindowName()
    {
        return windowName;
    }

    /**
     * Window name is used by reverse ajax to get around the 2 connection limit
     */
    private final String windowName;

    /**
     * @return the spareParameters
     */
    public Map<String, FormField> getExtraParameters()
    {
        return extraParameters;
    }

    /**
     * All the parameters sent by the browser
     */
    private final Map<String, FormField> extraParameters;

    /**
     * A special marker for the default value for extractParameter
     */
    protected static final String THROW = "throw";

    /**
     * What implementation of FileUpload are we using?
     */
    private static final FileUpload UPLOADER;

    /**
     * Retrieve the File Upload implem
     */
    static
    {
        Container container = WebContextFactory.get().getContainer();
        UPLOADER = container.getBean(FileUpload.class);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Batch.class);
}
