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
package org.directwebremoting.impl;

import java.util.Collection;
import java.util.HashSet;

import org.directwebremoting.extend.Module;
import org.directwebremoting.extend.ModuleManager;

/**
 * A ModuleManager that delegates to customModuleManager and fallbackModuleManager.
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public class MasterModuleManager implements ModuleManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ModuleManager#getModuleNames(boolean)
     */
    public Collection<String> getModuleNames(boolean includeHidden)
    {
        Collection<String> names = new HashSet<String>();
        if (customModuleManager != null)
        {
            names.addAll(customModuleManager.getModuleNames(includeHidden));
        }
        if (fallbackModuleManager != null)
        {
            names.addAll(fallbackModuleManager.getModuleNames(includeHidden));
        }
        return names;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ModuleManager#getModule(java.lang.String, boolean)
     */
    public Module getModule(String scriptName, boolean includeHidden)
    {
        Module module = null;
        if (customModuleManager != null)
        {
            module = customModuleManager.getModule(scriptName, includeHidden);
        }
        if (module == null && fallbackModuleManager != null)
        {
            module = fallbackModuleManager.getModule(scriptName, includeHidden);
        }
        return module;
    }

    /**
     * @param moduleManager the moduleManager to set
     */
    public void setCustomModuleManager(ModuleManager moduleManager)
    {
        this.customModuleManager = moduleManager;
    }

    /**
     * @param moduleManager the moduleManager to set
     */
    public void setFallbackModuleManager(ModuleManager moduleManager)
    {
        this.fallbackModuleManager = moduleManager;
    }

    ModuleManager customModuleManager;

    ModuleManager fallbackModuleManager;
}

