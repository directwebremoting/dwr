package org.directwebremoting.impl;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExportUtil
{
    /**
     * Is this class the System export as from the system dwr.xml?
     * @param scriptName The script name of the exported object
     * @return True if the scriptName represents the System export
     * @see org.directwebremoting.export.System
     */
    public static boolean isSystemClass(String scriptName)
    {
        return "__System".equals(scriptName);
    }

    /**
     * Is this class the System export as from the system dwr.xml?
     * @param scriptName The script name of the exported object
     * @return True if the scriptName represents the System export
     * @see org.directwebremoting.export.System
     */
    public static boolean isUnprotectedSystemMethod(String scriptName, String methodName)
    {
        return "__System".equals(scriptName) && "generateId".equals(methodName);
    }
}
