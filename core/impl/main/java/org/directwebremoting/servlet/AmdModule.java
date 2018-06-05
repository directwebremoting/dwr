package org.directwebremoting.servlet;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that abstracts the generation of AMD script modules.
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class AmdModule extends BaseModule
{
    /**
     * Constructor. Path parameters are supplied for expanding path variables in
     * module paths.
     * @param contextPath
     * @param servletPath
     */
    public AmdModule(String contextPath, String servletPath)
    {
        super(contextPath, servletPath, "/");
    }

    /**
     * Add a module that this module is dependent upon.
     * @param baseModulePath the module root that should be added to
     * the module name (may be empty, may be absolute or relative and
     * may contain path variables ${contextPath} and ${servletPath})
     * @param moduleName the module name, f ex a/b (dots a.b will be
     * converted to slashes)
     * @param parameterName the name of the dependency's parameter in
     * the script function
     */
    public void addDependency(String baseModulePath, String moduleName, String parameterName)
    {
        dependencies.add(expandModulePath(baseModulePath, moduleName));
        parameters.add(parameterName);
    }

    /**
     * Generates the module text
     */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        boolean first;

        buf.append("define(");

        // Dependency array
        if (dependencies.size() > 0)
        {
            buf.append("[");
            first = true;
            for(String modulePath : dependencies)
            {
                if (!first)
                {
                    buf.append(", ");
                }
                buf.append("\"" + modulePath + "\"");
                first = false;
            }
            buf.append("], ");
        }

        // Function
        buf.append("function(");
        first = true;
        for(String parameter : parameters)
        {
            if (!first)
            {
                buf.append(", ");
            }
            buf.append(parameter);
            first = false;
        }
        buf.append(") {\n");

        buf.append(contentBuf.toString());

        buf.append("});\n");

        return buf.toString();
    }

    /**
     * List of dependency module names.
     */
    private final List<String> dependencies = new ArrayList<String>();

    /**
     * List of function parameter names.
     */
    private final List<String> parameters = new ArrayList<String>();
}
