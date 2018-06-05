package org.directwebremoting.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A collection of standard log destinations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Loggers
{
    /**
     * Log destination for method access and exceptions caught outside of DWR's code base.
     */
    public static final Log ACCESS = LogFactory.getLog("org.directwebremoting.log.accessLog");

    /**
     * Log destination for startup messages
     */
    public static final Log STARTUP = LogFactory.getLog("org.directwebremoting.log.startup");

    /**
     * Log destination for session change messages
     */
    public static final Log SESSION = LogFactory.getLog("org.directwebremoting.log.session");

    /**
     * Log destination for script debug messages
     */
    public static final Log SCRIPTS = LogFactory.getLog("org.directwebremoting.log.scripts");
}
