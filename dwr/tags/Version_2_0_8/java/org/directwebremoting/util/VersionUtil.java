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
package org.directwebremoting.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Interface to the system version info file
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class VersionUtil
{
    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    public static String getSourceControlInfo()
    {
        synchronized (propLock)
        {
            if (props == null)
            {
                loadProperties();
            }

            return props.getProperty(KEY_SCCINFO);
        }
    }

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    public static String getVersion()
    {
        synchronized (propLock)
        {
            if (props == null)
            {
                loadProperties();
            }

            return props.getProperty(KEY_VERSION);
        }
    }

    /**
     * Load the properties from the internal properties file.
     */
    private static void loadProperties()
    {
        synchronized (propLock)
        {
            props = new Properties();

            try
            {
                InputStream in = VersionUtil.class.getResourceAsStream(FILENAME_VERSION);
                props.load(in);
            }
            catch (Exception ex)
            {
                props.put(KEY_VERSION, VALUE_UNKNOWN);
                props.put(KEY_SCCINFO, VALUE_UNKNOWN);
                props.put(KEY_ERROR, ex.toString());
            }
        }
    }

    private static Properties props = null;

    private static final Object propLock = new Object();

    private static final String FILENAME_VERSION = "/dwr-version.properties";

    private static final String KEY_VERSION = "version";

    private static final String KEY_SCCINFO = "scc-info";

    private static final String KEY_ERROR = "error";

    private static final String VALUE_UNKNOWN = "unknown";
}
