package org.directwebremoting.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author David Marginian [david at butterdev dot com].
 *
 * Represent the accessLogLevel's that can be set as init-params
 * on the DWR Servlet (see DefaultRemoter). Each type contains a name
 * and a hierarchy.  The hierarchy is number value (byte) that is used
 * to determine what/when to log based on the accessLogLevel.
 *
 */
public enum AccessLogLevel
{
    CALL ("call", (byte) 0),
    EXCEPTION   ("exception", (byte) 1),
    RUNTIMEEXCEPTION   ("runtimeexception", (byte) 2),
    ERROR   ("error", (byte) 3),
    OFF   ("off", (byte) 4);

    // We only want one accessLogLevelInstance, and we ensure this
    // in the getValue method.
    private static AccessLogLevel accessLogLevelInstance;
    private final String description;
    // Indicates the position in the hierarchy.  All types
    // with a higher hierarchy will be displayed in the log.
    private final byte hierarchy;

    private AccessLogLevel(String description, byte hierarchy) {
        this.description = description;
        this.hierarchy = hierarchy;
    }

    /**
     * This is a safe valueOf method.  Since we cannot override valueOf this method
     * should be used to retrieve an AccessLogLevel based on a String value.  This method
     * will always return an AccessLogLevel.  If accessLogLevelString is not valid a default
     * of OFF will be returned unless we are in debug mode when EXCEPTION will be returned.
     *
     * @param accessLogLevelString
     * @param debug - are we in debug mode
     * @return AccessLogLevel
     */
    public static AccessLogLevel getValue(String accessLogLevelString, boolean debug)
    {
        // May be overkill but use double-checked locking
        // to reduce the effect of the synchronization.
        if (null == accessLogLevelInstance)
        {
            synchronized (AccessLogLevel.class)
            {
                if (null == accessLogLevelInstance)
                {
                    try
                    {
                        accessLogLevelString = accessLogLevelString.toUpperCase();
                        accessLogLevelInstance = AccessLogLevel.valueOf(accessLogLevelString);
                    }
                    catch (Exception e)
                    {
                        // If debug is disabled and the user has attempted to set an accessLogLevel that does not exist
                        // we will default to OFF.  Let the user know why they aren't seeing anything in their logs.
                        if (!debug)
                        {
                            // only log if an accessLogLevel exists.  This means the user attempted to set the accessLogLevel
                            // but it was invalid.  In this case let the user know.
                            if (null != accessLogLevelString)
                            {
                                log.info("DWR is disabling logging.  " + accessLogLevelString + " is not a valid accessLogLevel.  Valid accessLogLevel's are: CALL, EXCEPTION, RUNTIMEEXCEPTION, and ERROR");
                            }
                            accessLogLevelInstance = AccessLogLevel.OFF;
                        }
                        else
                        {
                            if (null != accessLogLevelString)
                            {
                                log.info(accessLogLevelString + " is not a valid accessLogLevel.  Valid accessLogLevel's are: CALL, EXCEPTION, RUNTIMEEXCEPTION, and ERROR.  Since the debug init-param has been set to true DWR will log all Exceptions." );
                            }
                            accessLogLevelInstance = AccessLogLevel.EXCEPTION;
                        }
                    }
                }
            }
        }
        return accessLogLevelInstance;
    }

    public String description()   { return description; }
    public byte hierarchy() { return hierarchy; }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(AccessLogLevel.class);
}

