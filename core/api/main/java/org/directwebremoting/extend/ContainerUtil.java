package org.directwebremoting.extend;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;

/**
 * An abstraction of all the common servlet operations that are required to host
 * a DWR service that depends on the servlet spec.
 * It would be good to have a base class for all servlet operations, however
 * lack of MI prevents us from doing this.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ContainerUtil
{
    /**
     * Get an boolean setting from the Container.
     * @param container The container to look into
     * @param name The name of the setting to investigate
     * @param defaultValue The value to return if none is given
     * @return The value of the setting as an boolean, or the defaultValue if the
     * setting is empty or not convertible.
     */
    public static boolean getBooleanSetting(Container container, String name, boolean defaultValue)
    {
        Object value = container.getBean(name);
        if (value == null)
        {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.toString());
    }

    /**
     * Get an integer setting from the Container.
     * @param container The container to look into
     * @param name The name of the setting to investigate
     * @param defaultValue The value to return if none is given
     * @return The value of the setting as an int, or the defaultValue if the
     * setting is empty or not convertible.
     */
    public static int getIntSetting(Container container, String name, int defaultValue)
    {
        Object value = container.getBean(name);
        if (value == null)
        {
            return defaultValue;
        }

        try
        {
            return Integer.parseInt(value.toString());
        }
        catch (NumberFormatException ex)
        {
            log.warn("Failed to convert value '" + value + "' from setting '" + name + "' to an integer: " + ex.getMessage());
            return defaultValue;
        }
    }

    /**
     * Look up the Handler associated with the URL in the named URL property.
     * F ex supply the property name "engineHandlerUrl" to look up EngineHandler.
     *
     * @param container The container to look into
     * @param property a URL property name known by the container (usually
     * declared in defaults.properties)
     * @return the Handler registered on the URL
     */
    public static Handler getHandlerForUrlProperty(Container container, String property)
    {
        String registeredHandlerUrl = (String) container.getBean(property);
        return (Handler) container.getBean("url:" + registeredHandlerUrl);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ContainerUtil.class);
}
