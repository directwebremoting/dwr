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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Interface to the system version info file.
 * DWR version numbers are of the form "Version 1.2.3.3128[.beta]", where:
 * <ul>
 * <li>1 is the major release number. Changes in major version number indicate
 * significant enhancements in functionality</li>
 * <li>2 is the minor release number. Changes in minor version number indicate
 * less significant changes in functionality</li>
 * <li>3 is the revision release number. Changes here typically indicate bug
 * fixes only</li>
 * <li>3128 is the build number. This number increments for each build</li>
 * <li>.beta is a release title that is generally only used for non production
 * releases to indicate the purpose/quality of the release</li>
 * <li>The label is these strings concatenated</li>
 * </ul>
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
        loadProperties();
        return sccInfo;
    }

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     * @deprecated Use {@link #getLabel()}
     */
    @Deprecated
    public static String getVersion()
    {
        return getLabel();
    }

    /**
     * @return The major version number of this release
     */
    public static int getMajor()
    {
        return major;
    }

    /**
     * @return The minor version number of this release
     */
    public static int getMinor()
    {
        return minor;
    }

    /**
     * @return The revision version number of this release
     */
    public static int getRevision()
    {
        return revision;
    }

    /**
     * @return The build number of this release
     */
    public static int getBuild()
    {
        return build;
    }

    /**
     * @return The optional title of this release
     */
    public static String getTitle()
    {
        loadProperties();
        return title;
    }

    /**
     * @return The full version string
     */
    public static String getLabel()
    {
        loadProperties();
        return label;
    }

    /**
     * Load the properties from the internal properties file.
     */
    private static synchronized void loadProperties()
    {
        if (loaded)
        {
            return;
        }

        try
        {
            InputStream in = VersionUtil.class.getResourceAsStream(FILENAME_VERSION);
            Properties props = new Properties();
            props.load(in);

            sccInfo = props.getProperty(KEY_SCC_INFO);
            major = Integer.parseInt(props.getProperty(KEY_MAJOR));
            minor = Integer.parseInt(props.getProperty(KEY_MINOR));
            revision = Integer.parseInt(props.getProperty(KEY_REVISION));
            build = Integer.parseInt(props.getProperty(KEY_BUILD));
            title = props.getProperty(KEY_TITLE);

            if (title.length() == 0)
            {
                label = major + "." + minor + "." + revision;
            }
            else
            {
                label = major + "." + minor + "." + revision + "." + build + "." + title;
            }

            loaded = true;
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private static boolean loaded = false;

    private static final String FILENAME_VERSION = "/dwr-version.properties";

    private static final String KEY_MAJOR = "major";
    private static int major;

    private static final String KEY_MINOR = "minor";
    private static int minor;

    private static final String KEY_REVISION = "revision";
    private static int revision;

    private static final String KEY_BUILD = "build";
    private static int build;

    private static final String KEY_TITLE = "title";
    private static String title;

    private static String label;

    private static final String KEY_SCC_INFO = "scc-info";
    private static String sccInfo;
}
