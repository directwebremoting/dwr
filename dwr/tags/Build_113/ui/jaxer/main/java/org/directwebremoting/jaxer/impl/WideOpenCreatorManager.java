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
package org.directwebremoting.jaxer.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.impl.DefaultCreatorManager;
import org.directwebremoting.util.LocalUtil;

/**
 * This is a modification of {@link DefaultCreatorManager} that attempts to do
 * a second round of configuration to allow direct access to any classes in the
 * system using a script name something like: creator/com/example/ClassName
 * <p><strong>WARNING:</strong> This class fundamentally breaks the DWR promise
 * of "we won't touch any of your code without your express permission". You
 * should only configure this class if you fully understand the implications of
 * doing so.
 * <p>This class was created to allow remoting between Jaxer and DWR for scripts
 * marked <code>runat="server"</code>. This should ensure that the exported
 * classes are not available to the outside world.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class WideOpenCreatorManager extends DefaultCreatorManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.CreatorManager#getCreator(java.lang.String)
     */
    @Override
    public Creator getCreator(String scriptName, boolean includeHidden) throws SecurityException
    {
        Creator creator = creators.get(scriptName);

        if (creator == null)
        {
            creator = shortNames.get(scriptName);
        }

        if (creator == null)
        {
            // So there is no creator by the given name. Next we try the more
            // verbose version where the script name is something like:
            // creator/com/example/ClassName
            String[] parts = scriptName.split("/", 2);
            if (parts.length == 1)
            {
                log.warn("Malformed scriptName: " + scriptName);
                throw new SecurityException("Malformed scriptName");
            }

            String className = parts[1];
            String creatorName = parts[0];
            try
            {
                // This is roughly equivalent to:
                // addCreator(className, creatorName, { });

                Class<? extends Creator> clazz = creatorTypes.get(creatorName);
                if (clazz == null)
                {
                    log.error("Missing creator: " + creatorName + " (while initializing creator for: " + className + ".js)");
                    throw new SecurityException("Missing creator");
                }

                creator = clazz.newInstance();

                // Nasty! Each creator has a different set of init params. The
                // 'new' creator needs a class param, but the spring creator
                // needs a 'bean' param. We only have enough for a single param
                // in the URL and we don't know what it should be called. So
                // this code is tied to the new creator. Yeulch
                Map<String, String> params = new HashMap<String, String>();
                params.put("class", className);
                params.put("scope", "script");

                LocalUtil.setParams(creator, params, ignore);
                creator.setProperties(params);
                addCreator(scriptName, creator);
                shortNames.put(creator.getJavascript(), creator);

                creator = creators.get(scriptName);
            }
            catch (Exception ex)
            {
                log.warn("Error adding creator: " + scriptName, ex);
                throw new SecurityException("Error adding creator");
            }
        }

        if (creator.isHidden() && !includeHidden)
        {
            log.warn("Attempt made to get hidden class with name: " + scriptName + " while includeHidden=false");
            throw new SecurityException("Missing script name");
        }

        return creator;
    }

    /**
     * We index creators by short name as well as full name
     */
    private Map<String, Creator> shortNames = new HashMap<String, Creator>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(WideOpenCreatorManager.class);
}
