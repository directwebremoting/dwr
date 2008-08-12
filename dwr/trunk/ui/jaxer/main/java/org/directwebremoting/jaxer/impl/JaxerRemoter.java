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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.impl.DefaultRemoter;

/**
 * This is a customization of {@link DefaultRemoter} which is needed because
 * DWR+Jaxer has a different definition of scriptName than plain vanilla DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JaxerRemoter extends DefaultRemoter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Remoter#generateInterfaceScript(java.lang.String, java.lang.String)
     */
    @Override
    public String generateInterfaceScript(String fullCreatorName, boolean includeDto, String contextServletPath) throws SecurityException
    {
        Creator creator = creatorManager.getCreator(fullCreatorName, false);
        if (creator == null)
        {
            log.warn("Failed to find creator using: " + fullCreatorName);
            throw new SecurityException("Failed to find creator");
        }

        String scriptName = creator.getJavascript();

        StringBuilder buffer = new StringBuilder();

        if (includeDto)
            buffer.append(createParameterDefinitions(scriptName));
        buffer.append(EnginePrivate.getEngineInitScript());
        buffer.append(createClassDefinition(scriptName));
        buffer.append(createPathDefinition(scriptName, contextServletPath));
        buffer.append(createMethodDefinitions(fullCreatorName));

        return buffer.toString();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JaxerRemoter.class);
}
