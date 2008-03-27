package uk.ltd.getahead.dwr;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import uk.ltd.getahead.dwr.util.Logger;

/**
 * Internationalization for DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Messages
{
    /**
     * Prevent instansiation
     */
    private Messages()
    {
    }

    /**
     * Internationalize a simple string
     * @param key The lookup key
     * @return The internationalized version of the key
     */
    public static String getString(String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException ex)
        {
            log.error("Missing I18N string: " + key, ex); //$NON-NLS-1$
            return '!' + key + '!';
        }
    }

    /**
     * Internationalize a parameterized string
     * @param key The lookup key
     * @param param Lookup parameter number 1
     * @return The internationalized version of the key
     */
    public static String getString(String key, Object param)
    {
        return getString(key, new Object[] { param, });
    }

    /**
     * Internationalize a parameterized string
     * @param key The lookup key
     * @param param1 Lookup parameter number 1
     * @param param2 Lookup parameter number 2
     * @return The internationalized version of the key
     */
    public static String getString(String key, Object param1, Object param2)
    {
        return getString(key, new Object[] { param1, param2, });
    }

    /**
     * Internationalize a parameterized string
     * @param key The lookup key
     * @param param1 Lookup parameter number 1
     * @param param2 Lookup parameter number 2
     * @param param3 Lookup parameter number 3
     * @return The internationalized version of the key
     */
    public static String getString(String key, Object param1, Object param2, Object param3)
    {
        return getString(key, new Object[] { param1, param2, param3, });
    }

    /**
     * Internationalize a parameterized string
     * @param key The lookup key
     * @param param1 Lookup parameter number 1
     * @param param2 Lookup parameter number 2
     * @param param3 Lookup parameter number 3
     * @param param4 Lookup parameter number 4
     * @return The internationalized version of the key
     */
    public static String getString(String key, Object param1, Object param2, Object param3, Object param4)
    {
        return getString(key, new Object[] { param1, param2, param3, param4, });
    }

    /**
     * Internationalize a parameterized string
     * @param key The lookup key
     * @param params 
     * @return The internationalized version of the key
     */
    public static String getString(String key, Object[] params)
    {
        try
        {
            String format = RESOURCE_BUNDLE.getString(key);
            return MessageFormat.format(format, params);
        }
        catch (MissingResourceException ex)
        {
            log.error("Missing I18N string: " + key, ex); //$NON-NLS-1$
            return '!' + key + '!';
        }
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Messages.class);

    /**
     * The lookup bundle
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("uk.ltd.getahead.dwr.messages"); //$NON-NLS-1$
}
