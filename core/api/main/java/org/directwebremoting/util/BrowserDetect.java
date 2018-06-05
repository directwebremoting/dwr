package org.directwebremoting.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Various functions to do with working out what is at the other end of the
 * wire, and what it can do.
 * 
 * <h2>Version number documentation</h2>
 * 
 * <h3>Firefox</h3>
 * <p>Quick summary:
 * <ul>
 * <li>Firefox 1.0.0 = Gecko/20041108 - 20050405
 * <li>Firefox 1.0.8 = Gecko/20060410 - 20060418
 * <li>Firefox 1.4.0 = Gecko/20050908
 * </ul>
 * 
 * <h3>Safari</h3>
 * <p>Quick summary:
 * <ul>
 * <li>Jaguar  = 10.2.x  = Safari 1.0.x = WebKit/85
 * <li>Panther = 10.3.0+ = Safari 1.1.x = WebKit/100
 * <li>Panther = 10.3.4+ = Safari 1.2.x = WebKit/125
 * <li>Panther = 10.3.9+ = Safari 1.3.x = WebKit/312
 * <li>Tiger   = 10.4.x  = Safari 2.0.x = WebKit/412-419
 * <li>Tiger   = 10.4.11 = Safari 3.0.x = WebKit/523
 * <li>Leopard = 10.5.x  = Safari 3.0.x = WebKit/523
 * <li>Windows           = Safari 3.0.x = WebKit/523
 * <li>Leopard = 10.5.x  = Safari 3.1.x = WebKit/525-526
 * </ul>
 * 
 * <p>For full information see the Safari and WebKit Version Information:
 * <a href="http://developer.apple.com/internet/safari/uamatrix.html">at Apple
 * Developer Connection</a> and for browsers in general, see this fairly complete
 * <a href="http://www.useragentstring.com/pages/useragentstring.php">list of
 * user agent strings</a>.</p>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BrowserDetect
{
    /**
     * How many connections can this browser open simultaneously?
     * @param request The request so we can get at the user-agent header
     * @return The number of connections that we think this browser can take
     */
    public static int getConnectionLimit(HttpServletRequest request)
    {
        if (atLeast(request, UserAgent.IE, 8))
        {
            return 6;
        }
        if (atLeast(request, UserAgent.Firefox, 3))
        {
            return 6;
        }
        else if (atLeast(request, UserAgent.AppleWebKit, 8))
        {
            return 4;
        }
        else if (atLeast(request, UserAgent.Opera, 9))
        {
            return 4;
        }
        else
        {
            return 2;
        }
    }

    /**
     * Does this web browser support comet?
     * @param request The request so we can get at the user-agent header
     * @return True if long lived HTTP connections are supported
     */
    public static boolean supportsComet(HttpServletRequest request)
    {
        String userAgent = request.getHeader("user-agent");

        // None of the non-iPhone mobile browsers that I've tested support comet
        if (userAgent.contains("Symbian"))
        {
            return false;
        }

        // We need to test for other failing browsers here

        return true;
    }

    /**
     * Check that the user-agent string indicates some minimum browser level
     * @param request The browsers request
     * @param requiredUserAgent The UA required
     * @return true iff the browser matches the spec.
     */
    public static boolean atLeast(HttpServletRequest request, UserAgent requiredUserAgent)
    {
        return atLeast(request, requiredUserAgent, -1);
    }

    /**
     * Check that the user-agent string indicates some minimum browser level
     * @param request The browsers request
     * @param requiredUserAgent The UA required. Currently this is major version only
     * @param requiredVersion The version required, or -1 if versions are not important
     * @return true iff the browser matches the spec.
     */
    public static boolean atLeast(HttpServletRequest request, UserAgent requiredUserAgent, int requiredVersion)
    {
        String userAgent = request.getHeader("user-agent");
        int realVersion;

        switch (requiredUserAgent)
        {
        case IE:
            realVersion = getMajorVersionAssumingIE(userAgent);
            break;

        case Gecko:
            realVersion = getMajorVersionAssumingGecko(userAgent);
            break;

        case Opera:
            realVersion = getMajorVersionAssumingOpera(userAgent);
            break;

        case AppleWebKit:
            realVersion = getMajorVersionAssumingAppleWebKit(userAgent);
            break;

        case Firefox:
            realVersion = getMajorVersionAssumingFirefox(userAgent);
            break;

        default:
            throw new UnsupportedOperationException("Detection of " + requiredUserAgent + " is not supported yet.");
        }

        return realVersion >= requiredVersion;
    }

    /**
     * Check {@link #atLeast(HttpServletRequest, UserAgent)} for
     * {@link UserAgent#AppleWebKit}
     */
    private static int getMajorVersionAssumingAppleWebKit(String userAgent)
    {
        int webKitPos = userAgent.indexOf("AppleWebKit");
        if (webKitPos == -1)
        {
            return -1;
        }

        return parseNumberAtStart(userAgent.substring(webKitPos + 12));
    }

    /**
     * Check {@link #atLeast(HttpServletRequest, UserAgent)} for
     * {@link UserAgent#AppleWebKit}
     */
    private static int getMajorVersionAssumingFirefox(String userAgent)
    {
        int firefoxPos = userAgent.indexOf("Firefox");
        if (firefoxPos == -1)
        {
            return -1;
        }

        return parseNumberAtStart(userAgent.substring(firefoxPos + 8));
    }

    /**
     * Check {@link #atLeast(HttpServletRequest, UserAgent)} for
     * {@link UserAgent#Opera}
     */
    private static int getMajorVersionAssumingOpera(String userAgent)
    {
        int operaPos = userAgent.indexOf("Opera");
        if (operaPos == -1)
        {
            return -1;
        }

        return parseNumberAtStart(userAgent.substring(operaPos + 6));
    }

    /**
     * Check {@link #atLeast(HttpServletRequest, UserAgent)} for
     * {@link UserAgent#Gecko}
     */
    private static int getMajorVersionAssumingGecko(String userAgent)
    {
        int geckoPos = userAgent.indexOf(" Gecko/20");
        if (geckoPos == -1 || userAgent.contains("WebKit/"))
        {
            return -1;
        }

        return parseNumberAtStart(userAgent.substring(geckoPos + 7));
    }

    /**
     * Check {@link #atLeast(HttpServletRequest, UserAgent)} for
     * {@link UserAgent#IE}
     */
    private static int getMajorVersionAssumingIE(String userAgent)
    {
        int msiePos = userAgent.indexOf("MSIE ");
        if (msiePos == -1 || userAgent.contains("Opera"))
        {
            return -1;
        }

        return parseNumberAtStart(userAgent.substring(msiePos + 5));
    }

    /**
     * We've found the start of a sequence of numbers, what is it as an int?
     */
    private static int parseNumberAtStart(String numberString)
    {
        if (numberString == null || numberString.length() == 0)
        {
            return -1;
        }

        int endOfNumbers = 0;
        while (Character.isDigit(numberString.charAt(endOfNumbers)))
        {
            endOfNumbers++;
        }

        try
        {
            return Integer.parseInt(numberString.substring(0, endOfNumbers));
        }
        catch (NumberFormatException ex)
        {
            return -1;
        }
    }

    /**
     * This method is for debugging only
     */
    public static String getUserAgentDebugString(HttpServletRequest request)
    {
        String userAgent = request.getHeader("user-agent");

        int version = getMajorVersionAssumingIE(userAgent);
        if (version != -1)
        {
            return "IE/" + version;
        }

        version = getMajorVersionAssumingGecko(userAgent);
        if (version != -1)
        {
            return "Gecko/" + version;
        }

        version = getMajorVersionAssumingAppleWebKit(userAgent);
        if (version != -1)
        {
            return "WebKit/" + version;
        }

        version = getMajorVersionAssumingOpera(userAgent);
        if (version != -1)
        {
            return "Opera/" + version;
        }

        return "Unknown: (" + userAgent + ")";
    }

    /**
     * @deprecated This method is for debugging only
     */
    @Deprecated
    public static void debugBrowserVersions(HttpServletRequest request)
    {
        String userAgent = request.getHeader("user-agent");

        log.debug("User-Agent: " + userAgent);
        log.debug("Version assuming IE: " + getMajorVersionAssumingIE(userAgent));
        log.debug("Version assuming Gecko: " + getMajorVersionAssumingGecko(userAgent));
        log.debug("Version assuming Opera: " + getMajorVersionAssumingOpera(userAgent));
        log.debug("Version assuming WebKit: " + getMajorVersionAssumingAppleWebKit(userAgent));
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BrowserDetect.class);
}
