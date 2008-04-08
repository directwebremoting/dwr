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
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * Utilities to parse GET and POST requests from the DWR javascript section.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ParseUtil
{
    /**
     * Parse an inbound request into a set of fields
     * @param request The original browser's request
     * @return The set of fields parsed from the request
     * @throws ServerException
     */
    public Map<String, FormField> parseRequest(HttpServletRequest request) throws ServerException
    {
        boolean get = "GET".equals(request.getMethod());
        if (get)
        {
            return parseGet(request);
        }
        else
        {
            return parsePost(request);
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
    public static final boolean isMultipartContent(HttpServletRequest request)
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
            // I've had reports of data loss in Tomcat 5.0 that relate to this bug
            //   http://issues.apache.org/bugzilla/show_bug.cgi?id=27447
            // See mails to users@dwr.dev.java.net:
            //   Subject: "Tomcat 5.x read-ahead problem"
            //   From: CAKALIC, JAMES P [AG-Contractor/1000]
            // It would be more normal to do the following:
            // BufferedReader in = req.getReader();
            in = new BufferedReader(new InputStreamReader(req.getInputStream()));
   
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
                        part = LocalUtil.decode(part);
   
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
            throw new ServerException(Messages.getString("ParseUtil.InputReadFailed"), ex);
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
        Iterator<String> it = paramMap.keySet().iterator();
        if (!it.hasNext())
        {
            throw new IllegalStateException("No entries in non empty map!");
        }

        // So get the first
        String key = it.next();
        String value = paramMap.get(key).getString();
        String line = key + ProtocolConstants.INBOUND_DECL_SEPARATOR + value;

        StringTokenizer st = new StringTokenizer(line, "\n");
        while (st.hasMoreTokens())
        {
            String part = st.nextToken();
            part = LocalUtil.decode(part);

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
                throw new ServerException(Messages.getString("ParseUtil.MultiValues", key));
            }
        }

        return convertedMap;
    }

    /**
     * What implementation of FileUpload are we using?
     */
    private static final FileUpload UPLOADER;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ParseUtil.class);

    /**
     * Work out which is the correct implementation of FileUpload
     */
    static
    {
        FileUpload test;
        try
        {
            test = new CommonsFileUpload();
            log.debug("Using commons-file-upload.");
        }
        catch (NoClassDefFoundError ex)
        {
            test = new UnsupportedFileUpload();
            log.debug("Failed to find commons-file-upload. File upload is not supported.");
        }
        catch (Exception ex)
        {
            test = new UnsupportedFileUpload();
            log.debug("Failed to start commons-file-upload. File upload is not supported.");
        }

        UPLOADER = test;
    }
}
