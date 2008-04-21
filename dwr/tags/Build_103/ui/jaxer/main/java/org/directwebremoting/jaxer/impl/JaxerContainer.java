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

import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.impl.DefaultContainer;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.jaxer.servlet.JaxerInterfaceHandler;

/**
 * This is a simple enhancement of {@link DefaultContainer} that provides a set
 * of default resources for running inside Jaxer. Since it provides a complete
 * set of resources without the need for external customization it also
 * completes the setup phase (in {@link DefaultContainer} terminology)
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JaxerContainer extends DefaultContainer
{
    /**
     * Provide the defaults
     */
    public JaxerContainer()
    {
        // Setup the same set of defaults that DWR uses
        StartupUtil.setupDefaults(this);

        // Overrides for custom implementations
        addImplementation(CreatorManager.class, WideOpenCreatorManager.class);
        addImplementation(Remoter.class, JaxerRemoter.class);
        StartupUtil.createPathMapping(this, "/new/", JaxerInterfaceHandler.class, "interfaceHandlerUrl");

        // Custom settings for existing DWR implementations
        addParameter("debug", "true");
        addParameter("activeReverseAjaxEnabled", "true");
        addParameter("initApplicationScopeCreatorsAtStartup", "true");
        addParameter("maxWaitAfterWrite", "100");
        addParameter("preferDataUrlSchema", "false");
        addParameter("allowGetForSafariButMakeForgeryEasier", "true");
        addParameter("crossDomainSessionSecurity", "false");
        addParameter("allowScriptTagRemoting", "true");
        addParameter("useAbsolutePath", "true");
        addParameter("defaultToAsync", "false");
    }
}
