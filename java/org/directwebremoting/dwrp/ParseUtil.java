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

import org.directwebremoting.extend.ServerException;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * Utilities to parse GET and POST requests from the DWR javascript section.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ParseUtil
{
    /**
     * Parse an HTTP POST request to fill out the scriptName, methodName and
     * paramList properties. This method should not fail unless it will not
     * be possible to return any sort of error to the user. Failure cases should
     * be handled by the <code>checkParams()</code> method.
     * @param req The original browser's request
     * @return The equivalent of HttpServletRequest.getParameterMap() for now
     * @throws ServerException If reading from the request body stream fails
     */
    public static Map parsePost(HttpServletRequest req) throws ServerException
    {
        Map paramMap = new HashMap();

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
                        Enumeration en = req.getParameterNames();
                        while (en.hasMoreElements())
                        {
                            String name = (String) en.nextElement();
                            paramMap.put(name, req.getParameter(name));
                        }
                    }

                    break;
                }

                if (line.indexOf('&') != -1)
                {
                    // If there are any &s then this must be iframe post and all the
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
                    // Horay, this is a normal one!
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

        // If there is only 1 param then this must be a broken Safari. All
        // the parameters have got dumped on one line split with \n
        // See: http://bugzilla.opendarwin.org/show_bug.cgi?id=3565
        //      https://dwr.dev.java.net/issues/show_bug.cgi?id=93
        //      http://jira.atlassian.com/browse/JRA-8354
        //      http://developer.apple.com/internet/safari/uamatrix.html
        if (paramMap.size() == 1)
        {
            // This looks like a broken Mac where the line endings are confused
            log.debug("Using Broken Safari POST mode");

            // Iterators insist that we call hasNext() before we start
            Iterator it = paramMap.keySet().iterator();
            if (!it.hasNext())
            {
                throw new IllegalStateException("No entries in non empty map!");
            }

            // So get the first
            String key = (String) it.next();
            String value = (String) paramMap.get(key);
            String line = key + ProtocolConstants.INBOUND_DECL_SEPARATOR + value;

            StringTokenizer st = new StringTokenizer(line, "\n");
            while (st.hasMoreTokens())
            {
                String part = st.nextToken();
                part = LocalUtil.decode(part);

                parsePostLine(part, paramMap);
            }
        }

        return paramMap;
    }

    /**
     * Sort out a single line in a POST request
     * @param line The line to parse
     * @param paramMap The map to add parsed parameters to
     */
    private static void parsePostLine(String line, Map paramMap)
    {
        if (line.length() == 0)
        {
            return;
        }

        int sep = line.indexOf(ProtocolConstants.INBOUND_DECL_SEPARATOR);
        if (sep == -1)
        {
            log.warn("Missing separator in POST line: " + line);
        }
        else
        {
            String key = line.substring(0, sep);
            String value = line.substring(sep  + ProtocolConstants.INBOUND_DECL_SEPARATOR.length());

            paramMap.put(key, value);
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
    public static Map parseGet(HttpServletRequest req) throws ServerException
    {
        Map convertedMap = new HashMap();
        Map paramMap = req.getParameterMap();

        for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String[] array = (String[]) entry.getValue();

            if (array.length == 1)
            {
                convertedMap.put(key, array[0]);
            }
            else
            {
                throw new ServerException(Messages.getString("ParseUtil.MultiValues", key));
            }
        }

        return convertedMap;
    }

    /**
     * The javascript outbound marshaller prefixes the toString value with a
     * colon and the original type information. This undoes that.
     * @param data The string to be split up
     * @return A string array containing the split data
     */
    public static String[] splitInbound(String data)
    {
        String[] reply = new String[2];

        int colon = data.indexOf(ProtocolConstants.INBOUND_TYPE_SEPARATOR);
        if (colon == -1)
        {
            log.error("Missing : in conversion data (" + data + ')');
            reply[LocalUtil.INBOUND_INDEX_TYPE] = ProtocolConstants.TYPE_STRING;
            reply[LocalUtil.INBOUND_INDEX_VALUE] = data;
        }
        else
        {
            reply[LocalUtil.INBOUND_INDEX_TYPE] = data.substring(0, colon);
            reply[LocalUtil.INBOUND_INDEX_VALUE] = data.substring(colon + 1);
        }

        return reply;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ParseUtil.class);
}
