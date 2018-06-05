package org.directwebremoting.extend;

/**
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public interface Module
{
    /**
     * Returns the name uniquely identifying the module. This name is also used
     * in JavaScript land.
     * @return name string
     */
    String getName();

    /**
     * Tells whether the module information as described by this interface may
     * change over time, or may be cached.
     * @return true if cacheable
     */
    boolean isCacheable();

    /**
     * Returns all logical method declarations for the module.
     * @return array of method declarations
     */
    MethodDeclaration[] getMethods();

    /**
     * Returns a particular method as matched by the name and parameter types.
     * @param methodName
     * @param parameterTypes
     * @return method declaration
     */
    MethodDeclaration getMethod(String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException;

    /**
     * Invokes the code behind a logical method declaration, using the supplied
     * parameters.
     * @param method
     * @param parameters
     * @return the return value of the method
     */
    Object executeMethod(MethodDeclaration method, Object[] parameters) throws Exception;

    /**
     * Returns a pretty-print friendly string describing the module.
     * @return human-readable descriptive string
     */
    String toString();
}

