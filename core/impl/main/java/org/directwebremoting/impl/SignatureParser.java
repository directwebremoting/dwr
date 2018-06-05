package org.directwebremoting.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.OverrideProperty;
import org.directwebremoting.extend.ParameterProperty;
import org.directwebremoting.extend.Property;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;

/**
 * A parser for type info in a dwr.xml signature.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SignatureParser
{
    /**
     * Simple ctor
     * @param converterManager Having understood the extra type info we add it in here.
     * @param creatorManager If we can't find a class by Java name we can lookup by Javascript name
     */
    public SignatureParser(ConverterManager converterManager, CreatorManager creatorManager)
    {
        this.converterManager = converterManager;
        this.creatorManager = creatorManager;
        packageImports.add("java.lang");
    }

    /**
     * Parse some text and add it into the converter manager.
     * @param sigtext The text to parse
     */
    public void parse(String sigtext)
    {
        try
        {
            Loggers.STARTUP.debug("Parsing extra type info: ");

            String reply = LegacyCompressor.stripMultiLineComments(sigtext);
            reply = LegacyCompressor.stripSingleLineComments(reply);
            String process = reply;

            process = process.replace('\n', ' ');
            process = process.replace('\r', ' ');
            process = process.replace('\t', ' ');

            StringTokenizer st = new StringTokenizer(process, ";");
            while (st.hasMoreTokens())
            {
                String line = st.nextToken();
                line = line.trim();
                if (line.length() == 0)
                {
                    continue;
                }

                if (line.startsWith("import "))
                {
                    parseImportLine(line);
                }
                else
                {
                    parseDeclarationLine(line);
                }
            }
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.error("Unexpected Error", ex);
        }
    }

    /**
     * Parse a single import line
     * @param line The import statement
     */
    private void parseImportLine(String line)
    {
        String shortcut = line.substring(7, line.length());
        shortcut = shortcut.trim();

        if (line.endsWith(".*"))
        {
            shortcut = shortcut.substring(0, shortcut.length() - 2);
            packageImports.add(shortcut);
        }
        else
        {
            int lastDot = line.lastIndexOf('.');
            if (lastDot == -1)
            {
                Loggers.STARTUP.error("Missing . from import statement: " + line);
                return;
            }

            String leaf = line.substring(lastDot + 1);
            classImports.put(leaf, shortcut);
        }
    }

    /**
     * Parse a single declaration line.
     * Where line is defined as being everything in between 2 ; chars.
     * @param line The line to parse
     */
    private void parseDeclarationLine(String line)
    {
        int openBrace = line.indexOf('(');
        int closeBrace = line.indexOf(')');

        if (openBrace == -1)
        {
            Loggers.STARTUP.error("Missing ( in declaration: " + line);
            return;
        }

        if (closeBrace == -1)
        {
            Loggers.STARTUP.error("Missing ) in declaration: " + line);
            return;
        }

        if (openBrace > closeBrace)
        {
            Loggers.STARTUP.error("( Must come before ) in declaration: " + line);
            return;
        }

        // Class name and method name come before the opening (
        String classMethod = line.substring(0, openBrace).trim();

        Method method = findMethod(classMethod);
        if (method == null)
        {
            // Debug is done by findMethod()
            return;
        }

        // Now we need to get a list of all the parameters
        String paramDecl = line.substring(openBrace + 1, closeBrace);
        String[] paramNames = split(paramDecl);

        // Check that we have the right number
        if (method.getParameterTypes().length != paramNames.length)
        {
            Loggers.STARTUP.error("Parameter mismatch parsing signatures section in dwr.xml on line: " + line);
            Loggers.STARTUP.info("- Reflected method had: " + method.getParameterTypes().length + " parameters: " + method.toString());
            Loggers.STARTUP.info("- Signatures section had: " + paramNames.length + " parameters");
            Loggers.STARTUP.info("- This can be caused by method overloading which is not supported by Javascript or DWR");
            return;
        }

        for (int i = 0; i < paramNames.length; i++)
        {
            String[] genericList = getGenericParameterTypeList(paramNames[i]);
            for (int j = 0; j < genericList.length; j++)
            {
                String type = genericList[j].trim();
                Class<?> clazz = findClass(type);

                if (clazz != null)
                {
                    Property parent = new ParameterProperty(new MethodDeclaration(method), i);
                    Property child = parent.createChild(j);
                    child = converterManager.checkOverride(child);
                    Property replacement = new OverrideProperty(clazz);
                    converterManager.setOverrideProperty(child, replacement);

                    if (Loggers.STARTUP.isDebugEnabled())
                    {
                        Loggers.STARTUP.debug("- " + child + " = " + clazz.getName());
                    }
                }
                else
                {
                    Loggers.STARTUP.warn("Missing class (" + type + ") while parsing signature section on line: " + line);
                }
            }
        }
    }

    /**
     * Lookup a class according to the import rules
     * @param type The name of the class to find
     * @return The found class, or null if it does not exist
     */
    private Class<?> findClass(String type)
    {
        String itype = type;
        Class<?> clazz = null;

        // If type is an Array work it.
        try
        {
            if (type.contains("[]"))
            {
                itype = type.substring(0, type.indexOf('['));
            }
        } catch (Exception ex) {
            log.debug("SignatureParser can't find class: " + ex.getMessage());
        }

        // Handle inner classes
        if (itype.indexOf('.') != -1)
        {
            Loggers.STARTUP.debug("Inner class detected: " + itype);
            itype = itype.replace('.', '$');
        }

        clazz = retrieveClassFromClassImports(itype);
        if (null == clazz)
        {
            clazz = retrieveClassFromPackageImports(itype);
        }
        if (null == clazz)
        {
            // So we've failed to find a Java class name. We can also lookup by
            // Javascript name to help the situation where there is a dynamic proxy
            // in the way.
            Creator creator = creatorManager.getCreator(type, false);
            if (creator != null)
            {
                clazz = creator.getType();
            }
        }

        if (null != clazz)
        {
            if (type.contains("[]"))
            {
                return Array.newInstance(clazz, 0).getClass();
            }
            return clazz;
        }

        Loggers.STARTUP.error("Failed to find class: '" + itype + "' from <signature> block.");
        Loggers.STARTUP.info("- Looked in the following class imports:");
        for (Entry<String, String> entry : classImports.entrySet())
        {
            Loggers.STARTUP.info("  - " + entry.getKey() + " -> " + entry.getValue());
        }
        Loggers.STARTUP.info("- Looked in the following package imports:");
        for (String pkg : packageImports)
        {
            Loggers.STARTUP.info("  - " + pkg);
        }
        return null;
    }

    /**
     * Attempts to retrieve a Class from the signatures class imports.  Returns null
     * if no class can be found.
     *
     * @param itype
     * @return Class<?>
     */
    private Class<?> retrieveClassFromClassImports(String itype)
    {
        try
        {
            String full = classImports.get(itype);
            if (full == null)
            {
                full = itype;
            }
            return LocalUtil.classForName(full);
        }
        catch (Exception ex)
        {
            log.debug("SignatureParser - Can't find class in signature class imports, will attempt package imports.");
        }
        return null;
    }

    /**
     * Attempts to retrieve a Class from the package imports.  Returns null
     * if no class can be found.
     *
     * @param itype
     * @return Class<?>
     */
    private Class<?> retrieveClassFromPackageImports(String itype)
    {
        for (String pkg : packageImports)
        {
            String lookup = pkg + '.' + itype;

            try
            {
                return LocalUtil.classForName(lookup);
            }
            catch (Exception ex)
            {
                log.debug("SignatureParser - Can't find class in package imports: " + lookup);
            }
        }
        return null;
    }

    /**
     * Convert a parameter like "Map&lt;Integer, URL&gt;" into an array,
     * something like [Integer, URL].
     * @param paramName The parameter declaration string
     * @return The array of generic types as strings
     */
    private static String[] getGenericParameterTypeList(String paramName)
    {
        int openGeneric = paramName.indexOf('<');
        if (openGeneric == -1)
        {
            Loggers.STARTUP.debug("No < in paramter declaration: " + paramName);
            return new String[0];
        }

        int closeGeneric = paramName.lastIndexOf('>');
        if (closeGeneric == -1)
        {
            Loggers.STARTUP.error("Missing > in generic declaration: " + paramName);
            return new String[0];
        }

        String generics = paramName.substring(openGeneric + 1, closeGeneric);
        StringTokenizer st = new StringTokenizer(generics, ",");
        String[] types = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens())
        {
            types[i] = st.nextToken();
            i++;
        }

        return types;
    }

    /**
     * Find a method from the declaration string
     * @param classMethod The declaration that comes before the (
     * @return The found method, or null if one was not found.
     */
    private Method findMethod(String classMethod)
    {
        String classMethodChop = classMethod;

        // If there is a return type then it must be before the last space.
        int lastSpace = classMethodChop.lastIndexOf(' ');
        if (lastSpace >= 0)
        {
            classMethodChop = classMethodChop.substring(lastSpace);
        }

        // The method name comes after the last .
        int lastDot = classMethodChop.lastIndexOf('.');
        if (lastDot == -1)
        {
            Loggers.STARTUP.error("Missing . to separate class name and method: " + classMethodChop);
            return null;
        }

        String className = classMethodChop.substring(0, lastDot).trim();
        String methodName = classMethodChop.substring(lastDot + 1).trim();

        Class<?> clazz = findClass(className);
        if (clazz == null)
        {
            // Debug is done by findClass()
            return null;
        }

        Method method = null;
        Method[] methods = clazz.getMethods();
        for (Method test : methods)
        {
            if (test.getName().equals(methodName))
            {
                if (method == null)
                {
                    method = test;
                }
                else
                {
                    Loggers.STARTUP.warn("Setting extra type info to overloaded methods may fail with <parameter .../>");
                }
            }
        }

        if (method == null)
        {
            Loggers.STARTUP.error("Unable to find method called: " + methodName + " on type: " + clazz.getName());
        }

        return method;
    }

    /**
     * Chop a parameter declaration string into separate parameters
     * @param paramDecl The full set of parameter declarations
     * @return An array of found parameters
     */
    private static String[] split(String paramDecl)
    {
        List<String> params = new ArrayList<String>();

        boolean inGeneric = false;
        int start = 0;
        for (int i = 0; i < paramDecl.length(); i++)
        {
            char c = paramDecl.charAt(i);
            if (c == '<')
            {
                if (inGeneric)
                {
                    Loggers.STARTUP.error("Found < while parsing generic section: " + paramDecl);
                    break;
                }

                inGeneric = true;
            }

            if (c == '>')
            {
                if (!inGeneric)
                {
                    Loggers.STARTUP.error("Found > while not parsing generic section: " + paramDecl);
                    break;
                }

                inGeneric = false;
            }

            if (!inGeneric && c == ',')
            {
                // This is the start of a new parameter
                String param = paramDecl.substring(start, i);
                params.add(param);
                start = i + 1;
            }
        }

        // Add in the bit at the end:
        String param = paramDecl.substring(start, paramDecl.length());
        params.add(param);

        return params.toArray(new String[params.size()]);
    }

    /**
     * The map of specific class imports that we have parsed.
     */
    private final Map<String, String> classImports = new HashMap<String, String>();

    /**
     * The map of package imports that we have parsed.
     */
    private final List<String> packageImports = new ArrayList<String>();

    /**
     * Having understood the extra type info we add it in here.
     */
    private final ConverterManager converterManager;

    /**
     * If we can't find a class by Java name we can lookup by Javascript name
     */
    private final CreatorManager creatorManager;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(SignatureParser.class);
}
