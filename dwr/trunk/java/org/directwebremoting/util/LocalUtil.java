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
package org.directwebremoting.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Closeable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Various utilities, stuff that we're still surprised isn't in the JDK, and
 * stuff that perhaps is borderline JDK material, but isn't really pure DWR
 * either.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class LocalUtil
{
    /**
     * splitInbound() returns the type info in this parameter
     */
    public static final int INBOUND_INDEX_TYPE = 0;

    /**
     * splitInbound() returns the value info in this parameter
     */
    public static final int INBOUND_INDEX_VALUE = 1;

    /**
     * Prevent instantiation
     */
    private LocalUtil()
    {
    }

    /**
     * Create a string by joining the array elements together with the separator
     * in-between each element. A null (in the array or as a separator) is
     * treated as an empty string.
     * @param array The array of elements to join
     * @param separator The string sequence to place between array elements
     * @return A string containing the joined elements
     */
    public static String join(Object[] array, String separator)
    {
        if (array == null)
        {
            return null;
        }

        if (separator == null)
        {
            separator = "";
        }

        StringBuffer buffer = new StringBuffer();
        boolean isFirst = true;

        for (Object object : array)
        {
            if (isFirst)
            {
                isFirst = false;
            }
            else
            {
                buffer.append(separator);
            }

            if (object != null)
            {
                buffer.append(object);
            }
        }

        return buffer.toString();
    }

    /**
     * Determines if the specified string is permissible as a Java identifier.
     * Returns true if the string is non-null, non-zero length with a Java
     * identifier start as the first character and Java identifier parts in all
     * remaining characters.
     * @param test the string to be tested.
     * @return true if the string is a Java identifier, false otherwise.
     * @see java.lang.Character#isJavaIdentifierPart(char)
     * @see java.lang.Character#isJavaIdentifierStart(char)
     */
    public static boolean isJavaIdentifier(String test)
    {
        if (test == null || test.length() == 0)
        {
            return false;
        }

        if (!Character.isJavaIdentifierStart(test.charAt(0)) && test.charAt(0) != '_')
        {
            return false;
        }

        for (int i = 1; i < test.length(); i++)
        {
            if (!Character.isJavaIdentifierPart(test.charAt(i)) && test.charAt(i) != '_')
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines if the specified string contains only Unicode letters or
     * digits as defined by {@link Character#isLetterOrDigit(char)}
     * @param test The string to test
     * @return true if the string is non-null, non-empty and contains only
     * characters that are unicode letters or digits
     * @see Character#isLetterOrDigit(char)
     */
    public static boolean isLetterOrDigitOrUnderline(String test)
    {
        if (test == null || test.length() == 0)
        {
            return false;
        }

        for (int i = 0; i < test.length(); i++)
        {
            if (!Character.isLetterOrDigit(test.charAt(i)) && test.charAt(i) != '_')
            {
                return false;
            }
        }

        return true;
    }

    /**
     * True if c1 is java.lang.Boolean and c2 is boolean, etc.
     * @param c1 the first class to test
     * @param c2 the second class to test
     * @return true if the classes are equivalent
     */
    public static boolean isEquivalent(Class<?> c1, Class<?> c2)
    {
        if (c1 == Boolean.class || c1 == Boolean.TYPE)
        {
            return c2 == Boolean.class || c2 == Boolean.TYPE;
        }
        else if (c1 == Byte.class || c1 == Byte.TYPE)
        {
            return c2 == Byte.class || c2 == Byte.TYPE;
        }
        else if (c1 == Character.class || c1 == Character.TYPE)
        {
            return c2 == Character.class || c2 == Character.TYPE;
        }
        else if (c1 == Short.class || c1 == Short.TYPE)
        {
            return c2 == Short.class || c2 == Short.TYPE;
        }
        else if (c1 == Integer.class || c1 == Integer.TYPE)
        {
            return c2 == Integer.class || c2 == Integer.TYPE;
        }
        else if (c1 == Long.class || c1 == Long.TYPE)
        {
            return c2 == Long.class || c2 == Long.TYPE;
        }
        else if (c1 == Float.class || c1 == Float.TYPE)
        {
            return c2 == Float.class || c2 == Float.TYPE;
        }
        else if (c1 == Double.class || c1 == Double.TYPE)
        {
            return c2 == Double.class || c2 == Double.TYPE;
        }
        else if (c1 == Void.class || c1 == Void.TYPE)
        {
            return c2 == Void.class || c2 == Void.TYPE;
        }

        return false;
    }

    /**
     * @param type The class to de-primitivize
     * @return The non-primitive version of the class
     */
    public static Class<?> getNonPrimitiveType(Class<?> type)
    {
        if (!type.isPrimitive())
        {
            return type;
        }
        else if (type == Boolean.TYPE)
        {
            return Boolean.class;
        }
        else if (type == Byte.TYPE)
        {
            return Byte.class;
        }
        else if (type == Character.TYPE)
        {
            return Character.class;
        }
        else if (type == Short.TYPE)
        {
            return Short.class;
        }
        else if (type == Integer.TYPE)
        {
            return Integer.class;
        }
        else if (type == Long.TYPE)
        {
            return Long.class;
        }
        else if (type == Float.TYPE)
        {
            return Float.class;
        }
        else if (type == Double.TYPE)
        {
            return Double.class;
        }
        else if (type == Void.TYPE)
        {
            return Void.class;
        }

        return null;
    }

    /**
     * Add headers to prevent browsers and proxies from caching this reply.
     * @param resp The response to add headers to
     */
    public static void addNoCacheHeaders(HttpServletResponse resp)
    {
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");

        // Set to expire far in the past. Prevents caching at the proxy server
        resp.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
    }

    /**
     * Is this class one that we auto fill, so the user can ignore?
     * @param paramType The type to test
     * @return true if the type is a Servlet type
     */
    public static boolean isServletClass(Class<?> paramType)
    {
        return paramType == HttpServletRequest.class ||
               paramType == HttpServletResponse.class ||
               paramType == ServletConfig.class ||
               paramType == ServletContext.class ||
               paramType == HttpSession.class;
    }

    /**
     * URL decode a value.
     * {@link URLDecoder#decode(String, String)} throws an
     * {@link UnsupportedEncodingException}, which is silly given that the most
     * common use case will be to pass in "UTF-8"
     * @param value The string to decode
     * @return The decoded string
     */
    public static String decode(String value)
    {
        try
        {
            return URLDecoder.decode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            log.error("UTF-8 is not a valid char sequence?", ex);
            return value;
        }
    }

    /**
     * Set use reflection to set the setters on the object called by the keys
     * in the params map with the corresponding values
     * @param object The object to setup
     * @param params The settings to use
     * @param ignore List of keys to not warn about if they are not properties
     *               Note only the warning is skipped, we still try the setter
     */
    public static void setParams(Object object, Map<String, ?> params, List<String> ignore)
    {
        for (Map.Entry<String, ?> entry : params.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();

            try
            {
                setProperty(object, key, value);
            }
            catch (NoSuchMethodException ex)
            {
                if (ignore != null && !ignore.contains(key))
                {
                    log.warn("No property '" + key + "' on " + object.getClass().getName());
                }
            }
            catch (InvocationTargetException ex)
            {
                log.warn("Error setting " + key + "=" + value + " on " + object.getClass().getName(), ex.getTargetException());
            }
            catch (Exception ex)
            {
                log.warn("Error setting " + key + "=" + value + " on " + object.getClass().getName(), ex);
            }
        }
    }

    /**
     * Set a property on an object using reflection
     * @param object The object to call the setter on
     * @param key The name of the property to set.
     * @param value The new value to use for the property
     * @throws NoSuchMethodException Passed on from reflection code
     * @throws SecurityException Passed on from reflection code
     * @throws IllegalAccessException Passed on from reflection code
     * @throws IllegalArgumentException Passed on from reflection code
     * @throws InvocationTargetException Passed on from reflection code
     */
    public static void setProperty(Object object, String key, Object value) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Class<? extends Object> real = object.getClass();

        String setterName = "set" + key.substring(0, 1).toUpperCase(Locale.ENGLISH) + key.substring(1);

        try
        {
            // Can we work with whatever type we were given?
            Method method = real.getMethod(setterName, value.getClass());
            method.invoke(object, value);
            return;
        }
        catch (NoSuchMethodException ex)
        {
            // If it is a string then next we try to coerce it to the right type
            // otherwise we give up.
            if (!(value instanceof String))
            {
                throw ex;
            }
        }

        for (Method setter : real.getMethods())
        {
            if (setter.getName().equals(setterName) && setter.getParameterTypes().length == 1)
            {
                Class<?> propertyType = setter.getParameterTypes()[0];
                try
                {
                    Object param = simpleConvert((String) value, propertyType);
                    setter.invoke(object, param);
                    return;
                }
                catch (IllegalArgumentException ex)
                {
                    // The conversion failed - it was speculative anyway so we
                    // don't worry now
                }
            }
        }

        throw new NoSuchMethodException("Failed to find a property called: " + key + " on " + object.getClass().getName());
    }

    /**
     * Can the type be used in a call to {@link #simpleConvert(String, Class)}?
     * @param paramType The type to test
     * @return true if the type is acceptable to simpleConvert()
     */
    public static boolean isTypeSimplyConvertable(Class<?> paramType)
    {
        return paramType == String.class ||
            paramType == Integer.class ||
            paramType == Integer.TYPE ||
            paramType == Short.class ||
            paramType == Short.TYPE ||
            paramType == Byte.class ||
            paramType == Byte.TYPE ||
            paramType == Long.class ||
            paramType == Long.TYPE ||
            paramType == Float.class ||
            paramType == Float.TYPE ||
            paramType == Double.class ||
            paramType == Double.TYPE ||
            paramType == Character.class ||
            paramType == Character.TYPE ||
            paramType == Boolean.class ||
            paramType == Boolean.TYPE;
    }

    /**
     * A very simple conversion function for all the IoC style setup and
     * reflection that we are doing.
     * @param value The value to convert
     * @param paramType The type to convert to. Currently any primitive types and
     * String are supported.
     * @return The converted object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T simpleConvert(String value, Class<T> paramType)
    {
        if (paramType == String.class)
        {
            return (T) value;
        }

        if (paramType == Character.class || paramType == Character.TYPE)
        {
            value = LocalUtil.decode(value);
            if (value.length() == 1)
            {
                return (T) Character.valueOf(value.charAt(0));
            }
            else
            {
                throw new IllegalArgumentException("Can't more than one character in string - can't convert to char: '" + value + "'");
            }
        }

        String trimValue = value.trim();

        if (paramType == Boolean.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return (T) Boolean.valueOf(trimValue);
        }

        if (paramType == Boolean.TYPE)
        {
            return (T) Boolean.valueOf(trimValue);
        }

        if (paramType == Integer.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return (T) Integer.valueOf(trimValue);
        }

        if (paramType == Integer.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return (T) Integer.valueOf(0);
            }

            return (T) Integer.valueOf(trimValue);
        }

        if (paramType == Short.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return (T) Short.valueOf(trimValue);
        }

        if (paramType == Short.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return (T) Short.valueOf((short) 0);
            }

            return (T) Short.valueOf(trimValue);
        }

        if (paramType == Byte.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return (T) Byte.valueOf(trimValue);
        }

        if (paramType == Byte.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return (T) Byte.valueOf((byte) 0);
            }

            return (T) Byte.valueOf(trimValue);
        }

        if (paramType == Long.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return (T) Long.valueOf(trimValue);
        }

        if (paramType == Long.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return (T) Long.valueOf(0);
            }

            return (T) Long.valueOf(trimValue);
        }

        if (paramType == Float.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return (T) Float.valueOf(trimValue);
        }

        if (paramType == Float.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return (T) Float.valueOf(0);
            }

            return (T) Float.valueOf(trimValue);
        }

        if (paramType == Double.class)
        {
            if (trimValue.length() == 0)
            {
                return null;
            }

            return (T) Double.valueOf(trimValue);
        }

        if (paramType == Double.TYPE)
        {
            if (trimValue.length() == 0)
            {
                return (T) Double.valueOf(0.0D);
            }

            return (T) Double.valueOf(trimValue);
        }

        throw new IllegalArgumentException("Unsupported conversion type: " + paramType.getName());
    }

    /**
     * Is this object property one that we can use in a JSON style or do we need
     * to get fancy. i.e does it contain only letters and numbers with an
     * initial letter.
     * @param name The name to test for JSON compatibility
     * @return true if the name is simple
     */
    public static boolean isSimpleName(String name)
    {
        if (name.length() == 0)
        {
            return false;
        }

        if (JavascriptUtil.isReservedWord(name))
        {
            return false;
        }

        boolean isSimple = Character.isLetter(name.charAt(0));
        for (int i = 1; isSimple && i < name.length(); i++)
        {
            if (!Character.isLetterOrDigit(name.charAt(i)))
            {
                isSimple = false;
            }
        }

        return isSimple;
    }

    /**
     * Utility to essentially do Class forName and allow configurable
     * Classloaders.
     * <p>The initial implementation makes use of the context classloader for
     * the current thread.
     * @param className The class to create
     * @return The class if it is safe or null otherwise.
     * @throws ClassNotFoundException If <code>className</code> is not valid
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException
    {
        // Class.forName(className);
        return Thread.currentThread().getContextClassLoader().loadClass(className);
    }

    /**
     * Calling methods using reflection is useful for graceful fallback - this
     * is a helper method to make this easy
     * @param object The object to use as 'this'
     * @param method The method to call, can be null in which case null is returned
     * @param params The parameters to pass to the reflection call
     * @return The results of calling method.invoke() or null
     * @throws IllegalStateException If anything goes wrong
     */
    public static Object invoke(Object object, Method method, Object[] params) throws IllegalStateException
    {
        Object reply = null;
        if (method != null)
        {
            try
            {
                reply = method.invoke(object, params);
            }
            catch (InvocationTargetException ex)
            {
                throw new IllegalStateException("InvocationTargetException calling " + method.getName() + ": " + ex.getTargetException().toString());
            }
            catch (Exception ex)
            {
                throw new IllegalStateException("Reflection error calling " + method.getName() + ": " + ex.toString());
            }
        }

        return reply;
    }

    /**
     * Utility to essentially do Class forName with the assumption that the
     * environment expects failures for missing jar files and can carry on if
     * this process fails.
     * @param <T> The base type that we want a class to implement
     * @param debugContext The name for debugging purposes
     * @param className The class to create
     * @param impl The implementation class - what should className do?
     * @return The class if it is safe or null otherwise.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> classForName(String debugContext, String className, Class<T> impl)
    {
        Class<? extends T> clazz;

        try
        {
            clazz = (Class<? extends T>) classForName(className);
        }
        catch (ClassNotFoundException ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + debugContext + "' due to ClassNotFoundException on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (NoClassDefFoundError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + debugContext + "' due to NoClassDefFoundError on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (TransformerFactoryConfigurationError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + debugContext + "' due to TransformerFactoryConfigurationError on " + className + ". Cause: " + ex.getMessage());
            log.debug("Maybe you need to add xalan.jar to your webserver?");
            return null;
        }

        // Check it is of the right type
        if (!impl.isAssignableFrom(clazz))
        {
            log.error("Class '" + clazz.getName() + "' does not implement '" + impl.getName() + "'.");
            return null;
        }

        // Check we can create it
        try
        {
            clazz.newInstance();
        }
        catch (InstantiationException ex)
        {
            log.error("InstantiationException for '" + debugContext + "' failed:", ex);
            return null;
        }
        catch (IllegalAccessException ex)
        {
            log.error("IllegalAccessException for '" + debugContext + "' failed:", ex);
            return null;
        }
        catch (NoClassDefFoundError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + debugContext + "' due to NoClassDefFoundError on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (TransformerFactoryConfigurationError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + debugContext + "' due to TransformerFactoryConfigurationError on " + className + ". Cause: " + ex.getMessage());
            log.debug("Maybe you need to add xalan.jar to your webserver?");
            return null;
        }
        catch (Exception ex)
        {
            // For some reason we can't catch this?
            if (ex instanceof ClassNotFoundException)
            {
                // We expect this sometimes, hence debug
                log.debug("Skipping '" + debugContext + "' due to ClassNotFoundException on " + className + ". Cause: " + ex.getMessage());
                return null;
            }
            else
            {
                log.error("Failed to load '" + debugContext + "' (" + className + ")", ex);
                return null;
            }
        }

        return clazz;
    }

    /**
     * Utility to essentially do Class forName and newInstance with the
     * assumption that the environment expects failures for missing jar files
     * and can carry on if this process fails.
     * @param <T> The base type that we want a class to implement
     * @param name The name for debugging purposes
     * @param className The class to create
     * @param impl The implementation class - what should className do?
     * @return The new instance if it is safe or null otherwise.
     */
    @SuppressWarnings("unchecked")
    public static <T> T classNewInstance(String name, String className, Class<T> impl)
    {
        Class<T> clazz;

        try
        {
            clazz = (Class<T>) classForName(className);
        }
        catch (ClassNotFoundException ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to ClassNotFoundException on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (NoClassDefFoundError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to NoClassDefFoundError on " + className + ". Cause: " + ex.getMessage());
            return null;
        }
        catch (TransformerFactoryConfigurationError ex)
        {
            // We expect this sometimes, hence debug
            log.debug("Skipping '" + name + "' due to TransformerFactoryConfigurationError on " + className + ". Cause: " + ex.getMessage());
            return null;
        }

        // Check it is of the right type
        if (!impl.isAssignableFrom(clazz))
        {
            log.error("Class '" + clazz.getName() + "' does not implement '" + impl.getName() + "'.");
            return null;
        }

        // Check we can create it
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException ex)
        {
            log.error("InstantiationException for '" + name + "' failed:", ex);
            return null;
        }
        catch (IllegalAccessException ex)
        {
            log.error("IllegalAccessException for '" + name + "' failed:", ex);
            return null;
        }
        catch (TransformerFactoryConfigurationError ex)
        {
            log.error("TransformerFactoryConfigurationError for '" + name + "' failed:", ex);
            return null;
        }
        catch (Exception ex)
        {
            log.error("Failed to load creator '" + name + "', classname=" + className + ": ", ex);
            return null;
        }
    }

    /**
     * InputStream closer that can cope if the input stream is null.
     * If anything goes wrong, the errors are logged and ignored.
     * @param in The resource to close
     */
    public static void close(Closeable in)
    {
        if (in == null)
        {
            return;
        }

        try
        {
            in.close();
        }
        catch (IOException ex)
        {
            log.warn(ex.getMessage(), ex);
        }
    }

    /**
     * Return a List of super-classes for the given class.
     * @param clazz the class to look up
     * @return the List of super-classes in order going up from this one
     */
    public static List<Class<?>> getAllSuperclasses(Class<?> clazz)
    {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null)
        {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }

        return classes;
    }

    /**
     * Return a list of all fields (whatever access status, and on whatever
     * superclass they were defined) that can be found on this class.
     * <p>This is like a union of {@link Class#getDeclaredFields()} which
     * ignores and super-classes, and {@link Class#getFields()} which ignored
     * non-public fields
     * @param clazz The class to introspect
     * @return The complete list of fields
     */
    public static Field[] getAllFields(Class<?> clazz)
    {
        List<Class<?>> classes = getAllSuperclasses(clazz);
        classes.add(clazz);
        return getAllFields(classes);
    }

    /**
     * As {@link #getAllFields(Class)} but acts on a list of {@link Class}s and
     * uses only {@link Class#getDeclaredFields()}.
     * @param classes The list of classes to reflect on
     * @return The complete list of fields
     */
    private static Field[] getAllFields(List<Class<?>> classes)
    {
        Set<Field> fields = new HashSet<Field>();
        for (Class<?> clazz : classes)
        {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }

        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(LocalUtil.class);
}
