package org.directwebremoting.extend;

import java.util.Collection;

/**
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public interface ModuleManager
{

    /**
     * Returns the names of all modules handled by this ModuleManager.
     * @param includeHidden Should hidden (internal) modules also be listed?
     * @return collection of name strings
     */
    Collection<String> getModuleNames(boolean includeHidden);

    /**
     * Returns the specific module instance matching the supplied name.
     * @param name module name
     * @param includeHidden Should hidden (internal) modules also be fetchable?
     * @return module object
     */
    Module getModule(String name, boolean includeHidden);

}

