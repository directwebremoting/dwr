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

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
            log.error("Missing I18N string: " + key, ex);
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
            // Create a list of the parameters
            StringBuffer buffer = new StringBuffer();
            buffer.append('[');
            for (int i = 0; i < params.length; i++)
            {
                if (i != 0)
                {
                    buffer.append(',');
                }
                buffer.append(params[i].toString());
            }
            buffer.append(']');

            log.error("Missing I18N string: " + key + ". Params: " + buffer.toString());
            return '!' + key + '!' + buffer.toString() + '!';
        }
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Messages.class);

    /**
     * The lookup bundle
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.directwebremoting.messages");
}
