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
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ContainerUtil.class);
}
