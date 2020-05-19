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
            InputStream in = LocalUtil.getInternalResourceAsStream(VERSION_FILE_PATH);
            Properties props = new Properties();
            props.load(in);

            major = Integer.parseInt(props.getProperty(KEY_MAJOR));
            minor = Integer.parseInt(props.getProperty(KEY_MINOR));
            revision = Integer.parseInt(props.getProperty(KEY_REVISION));
            String buildString = props.getProperty(KEY_BUILD);
            if (null != buildString && buildString.length() > 0) {
                build = Integer.parseInt(props.getProperty(KEY_BUILD));
            }
            title = props.getProperty(KEY_TITLE);

            if (title.length() == 0)
            {
                label = major + "." + minor + "." + revision;
            }
            else if (build > -1)
            {
                label = major + "." + minor + "." + revision + "-" + title + "-" + build;
            }
            else
            {
                label = major + "." + minor + "." + revision + "-" + title;
            }

            loaded = true;
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private static boolean loaded = false;

    private static final String VERSION_FILE_PATH = "/dwr-version.properties";

    private static final String KEY_MAJOR = "major";
    private static int major;

    private static final String KEY_MINOR = "minor";
    private static int minor;

    private static final String KEY_REVISION = "revision";
    private static int revision;

    private static final String KEY_BUILD = "build.number";
    private static int build = -1;

    private static final String KEY_TITLE = "title";
    private static String title;

    private static String label;

}
