package org.directwebremoting.spring;

import org.springframework.util.ClassUtils;

/**
 * DWR additions to Spring ClassUtils.
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class LocalClassUtils extends ClassUtils
{
    /**
     * Wrap class lookup so exceptions are normalized to JDK behaviour
     * (notably SpringBoot and other containers have chosen diverging behaviour)
     */
    public static Class<?> forName(String name, ClassLoader cl) throws ClassNotFoundException, LinkageError
    {
        try
        {
            return ClassUtils.forName(name, cl);
        }
        catch(IllegalArgumentException ex)
        {
            // Conform to JDK classloader behaviour where illegal classnames are handled as ClassNotFound
            throw new ClassNotFoundException(ex.getMessage(), ex);
        }
    }
}
