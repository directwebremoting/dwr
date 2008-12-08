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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.io.OutputStreamLoader;

/**
 * Various utilities, stuff that we're still surprised isn't in the JDK, and
 * stuff that perhaps is borderline JDK material, but isn't really pure DWR
 * either.
 * TODO: This probably needs cutting up into
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class LocalUtil
{
    /**
     * Prevent instantiation
     */
    private LocalUtil()
    {
        throw new InstantiationError("Cannot instantiate LocalUtil");
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

        StringBuilder buffer = new StringBuilder();
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
     * Check whether the given CharSequence has actual text.
     * More specifically, returns <code>true</code> if the string not <code>null</code>,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     * <p><pre>
     * LocalUtil.hasText(null) = false
     * LocalUtil.hasText("") = false
     * LocalUtil.hasText(" ") = false
     * LocalUtil.hasText("12345") = true
     * LocalUtil.hasText(" 12345 ") = true
     * </pre>
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not <code>null</code>,
     * its length is greater than 0, and it does not contain whitespace only
     * @author Partly from the Spring Framework
     * @see java.lang.Character#isWhitespace(char)
     */
    public static boolean hasText(CharSequence str)
    {
        if (!hasLength(str))
        {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check that the given CharSequence is neither <code>null</code> nor of length 0.
     * Note: Will return <code>true</code> for a CharSequence that purely consists of whitespace.
     * <p><pre>
     * LocalUtil.hasLength(null) = false
     * LocalUtil.hasLength("") = false
     * LocalUtil.hasLength(" ") = true
     * LocalUtil.hasLength("Hello") = true
     * </pre>
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not null and has length
     * @see #hasText
     * @author Partly from the Spring Framework
     */
    public static boolean hasLength(CharSequence str)
    {
        return (str != null && str.length() > 0);
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
     * Determines if the specified string is permissible as a Java identifier
     * potentially prefixed with package names.
     * It tests each segment of the string separated by "." and returns true if
     * all segments have non-zero length with a Java
     * identifier start as the first character and Java identifier parts in all
     * remaining characters.
     * @param test the string to be tested.
     * @return true if the string is a Java identifier, false otherwise.
     */
    public static boolean isJavaIdentifierWithPackage(String test)
    {
        String[] segments = test.split("\\.");
        for (int i = 0; i < segments.length; i++)
        {
            if (!isJavaIdentifier(segments[i]))
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
     * A helper for implementing {@link Object#equals(Object)} when some of your
     * members could be null. Returns true if both objects are null, or if
     * neither object is null, but object1.equals(object2) returns true.
     * Otherwise returns false.
     * @param object1 The first object to compare.
     * @param object2 The second object to compare.
     * @return True if the objects are both null or {@link #equals(Object)}
     */
    public static boolean equals(Object object1, Object object2)
    {
        if (object1 == null)
        {
            return object2 == null;
        }

        return object1.equals(object2);
    }

    /**
     * {@link java.util.Comparator#compare(Object, Object)} demands that the
     * return is 1, 0, -1. This helps implement that.
     * @param diff The result of some subtraction.
     * @return 1, 0, -1
     */
    public static int shrink(long diff)
    {
        if (diff > 0)
        {
            return 1;
        }
        if (diff < 0)
        {
            return -1;
        }
        else
        {
            return 0;
        }
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
        if (type == Boolean.TYPE)
        {
            return Boolean.class;
        }
        if (type == Byte.TYPE)
        {
            return Byte.class;
        }
        if (type == Character.TYPE)
        {
            return Character.class;
        }
        if (type == Short.TYPE)
        {
            return Short.class;
        }
        if (type == Integer.TYPE)
        {
            return Integer.class;
        }
        if (type == Long.TYPE)
        {
            return Long.class;
        }
        if (type == Float.TYPE)
        {
            return Float.class;
        }
        if (type == Double.TYPE)
        {
            return Double.class;
        }
        if (type == Void.TYPE)
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
     * If something has gone wrong we want to know all about the request that
     * caused the failure
     * @param request The HttpServletRequest the borked
     */
    public static void debugRequest(HttpServletRequest request)
    {
        String queryString = (request.getQueryString() != null) ? "?" + request.getQueryString() : "";
        String requestLine = request.getMethod() + " " + request.getRequestURL() + queryString + " " + request.getProtocol();

        // The headers
        log.debug("Reconstituted HttpServletRequest:");
        log.debug(" " + requestLine);
        @SuppressWarnings("unchecked")
        Enumeration<String> headerNames = request.getHeaderNames();
        for (String headerName : iterableizer(headerNames))
        {
            log.debug("  " + headerName + ": " + request.getHeader(headerName));
        }

        StringWriter buffer = new StringWriter();
        try
        {
            ServletInputStream in = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            int length = 0;
            while (length < 256)
            {
                String line = reader.readLine();
                if (line == null)
                {
                    break;
                }
                buffer.append("\n");
                buffer.append(line);
                length += line.length();
            }
        }
        catch (IOException ex)
        {
            buffer.append("[Unable to read body: " + ex + "]\n");
        }
        log.debug(" " + buffer);

        // The attributes
        log.debug("Attributes attached to the Request:");
        @SuppressWarnings("unchecked")
        Enumeration<String> attributeNames = request.getAttributeNames();
        for (String attributeName : iterableizer(attributeNames))
        {
            log.debug("  " + attributeName + ": " + request.getAttribute(attributeName));
        }

        // Data parsed from the headers
        log.debug("Security properties:");
        log.debug("  AuthType: " + request.getAuthType());
        log.debug("  RemoteUser: " + request.getRemoteUser());
        log.debug("  UserPrincipal: " + request.getUserPrincipal());
    }

    /**
     * Go Java! How many people have written this code?
     * @param en The Enumeration that we want to iterate over
     * @return An implementation of {@link Iterable} for use in a for each loop
     */
    public static <T> Iterable<T> iterableizer(final Enumeration<T> en)
    {
        return new Iterable<T>()
        {
            /* @see java.lang.Iterable#iterator() */
            public Iterator<T> iterator()
            {
                return new Iterator<T>()
                {
                    /* @see java.util.Iterator#hasNext() */
                    public boolean hasNext()
                    {
                        return en.hasMoreElements();
                    }

                    /* @see java.util.Iterator#next() */
                    public T next()
                    {
                        return en.nextElement();
                    }

                    /* @see java.util.Iterator#remove() */
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * Go Java! Why do I even need to do this?
     * @param en The Enumeration that we want to iterate over
     * @param type For when we were given an Enumeration<?> and need an Iterator<T>
     * @return An implementation of {@link Iterable} for use in a for each loop
     */
    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> iterableizer(Enumeration<?> en, Class<T> type)
    {
        return iterableizer((Enumeration<T>) en);
    }

    /**
     * URL decode a value.
     * {@link URLDecoder#decode(String, String)} throws an
     * {@link UnsupportedEncodingException}, which is silly given that the most
     * common use case will be to pass in "UTF-8"
     * @param value The string to decode
     * @return The decoded string
     */
    public static String urlDecode(String value)
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
     * URL encode a value.
     * {@link URLEncoder#encode(String, String)} throws an
     * {@link UnsupportedEncodingException}, which is silly given that the most
     * common use case will be to pass in "UTF-8"
     * @param value The string to decode
     * @return The decoded string
     */
    public static String urlEncode(String value)
    {
        try
        {
            return URLEncoder.encode(value, "UTF-8");
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
        Class<?> real = object.getClass();

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
     * Set a property on an object using reflection
     * @param real The object type to find the setter on
     * @param key The name of the property to set.
     */
    public static Class<?> getPropertyType(Class<?> real, String key)
    {
        // Because getters can't be overloaded, we start by reflecting a getter
        String getterName = "get" + key.substring(0, 1).toUpperCase(Locale.ENGLISH) + key.substring(1);
        try
        {
            // Can we work with whatever type we were given?
            Method method = real.getMethod(getterName);
            return method.getReturnType();
        }
        catch (NoSuchMethodException ex)
        {
            // No getters
        }

        // Next we try for a unique setter
        String setterName = "set" + key.substring(0, 1).toUpperCase(Locale.ENGLISH) + key.substring(1);
        List<Class<?>> available = new ArrayList<Class<?>>();
        for (Method setter : real.getMethods())
        {
            if (setter.getName().equals(setterName) && setter.getParameterTypes().length == 1)
            {
                available.add(setter.getParameterTypes()[0]);
            }
        }
        if (available.isEmpty())
        {
            log.debug("Failed to find a setter called: " + setterName + " on " + real.getName());
            return null;
        }
        if (available.size() == 1)
        {
            return available.get(0);
        }

        // So there are multiple setters. We can still look for a field
        Field[] fields = getAllFields(real);
        for (Field field : fields)
        {
            if (field.getName().equals(key))
            {
                // Maybe there are several fields with the same name in an
                // inheritance hierarchy. And maybe the users get what they
                // deserve if that's happening ;-)
                return field.getType();
            }
        }

        // Just because there were no matching fields doesn't mean we can't guess
        return available.get(0);
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
            value = urlDecode(value);
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
        if (!className.contains("."))
        {
            if (className.equals(Boolean.TYPE.getName()))
            {
                return Boolean.TYPE;
            }
            if (className.equals(Byte.TYPE.getName()))
            {
                return Byte.TYPE;
            }
            if (className.equals(Character.TYPE.getName()))
            {
                return Character.TYPE;
            }
            if (className.equals(Short.TYPE.getName()))
            {
                return Short.TYPE;
            }
            if (className.equals(Integer.TYPE.getName()))
            {
                return Integer.TYPE;
            }
            if (className.equals(Long.TYPE.getName()))
            {
                return Long.TYPE;
            }
            if (className.equals(Float.TYPE.getName()))
            {
                return Float.TYPE;
            }
            if (className.equals(Double.TYPE.getName()))
            {
                return Double.TYPE;
            }
            if (className.equals(Void.TYPE.getName()))
            {
                return Void.TYPE;
            }
        }

        // Class.forName(className);
        return Thread.currentThread().getContextClassLoader().loadClass(className);
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
     * OutputStreamLoader closer that can cope if the input is null.
     * If anything goes wrong, the errors are logged and ignored.
     * @param loader The resource to close
     */
    public static void close(OutputStreamLoader loader)
    {
        if (loader == null)
        {
            return;
        }

        try
        {
            loader.close();
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
     * Utility to find a getter and return it's value from an object
     * If Java had the option to temporarily do dynamic typing there would be
     * no need for this.
     * @param pojo The POJO to extract some data from.
     * @param propertyName The name of the property form which we form a getter
     * name by upper-casing the first letter (in the EN locale) and prefixing
     * with 'get'
     * @return The value of property, or null if it does not exist
     */
    @SuppressWarnings("unchecked")
    public static <T> T getProperty(Object pojo, String propertyName, Class<T> type)
    {
        Class<?> real = pojo.getClass();

        String getterName = "get" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);

        try
        {
            Method method = real.getMethod(getterName);
            if (!type.isAssignableFrom(method.getReturnType()))
            {
                log.debug("Expected that the type of " + real.getName() + "." + propertyName + " was " + type.getName() + " but found " + method.getReturnType().getName() + ".");
                return null;
            }
            else
            {
                return (T) method.invoke(pojo);
            }
        }
        catch (Exception ex)
        {
            log.debug("Failed to get property called " + propertyName + " from a " + real.getName() + ": " + ex);
            return null;
        }
    }

    /**
     * Utility to find a Class<?> from a Type if possible, assuming String.class
     * if the conversion can't be made
     */
    public static Class<?> toClass(Type parameterType, String debugContext)
    {
        if (parameterType instanceof ParameterizedType)
        {
            ParameterizedType ptype = (ParameterizedType) parameterType;
            Type rawType = ptype.getRawType();

            if (rawType instanceof Class)
            {
                Class<?> type = (Class<?>) rawType;
                // log.debug("Using type info from JDK5 ParameterizedType of " + type.getName() + " for " + debugContext);
                return type;
            }
        }
        else if (parameterType instanceof Class)
        {
            Class<?> type = (Class<?>) parameterType;
            // log.debug("Using type info from JDK5 reflection of " + type.getName() + " for " + debugContext);
            return type;
        }

        log.warn("Missing type info for " + debugContext + ". Assuming this is a map with String keys. Please add to <signatures> in dwr.xml");
        return String.class;
    }

    /**
     * Parse the given <code>localeString</code> into a {@link Locale}.
     * <p>This is the inverse operation of {@link Locale#toString Locale's toString}.
     * @param localeString the locale string, following <code>Locale's</code>
     * <code>toString()</code> format ("en", "en_UK", etc);
     * also accepts spaces as separators, as an alternative to underscores
     * @return a corresponding <code>Locale</code> instance
     */
    public static Locale parseLocaleString(String localeString) {
        if (hasText(localeString))
        {
            String[] parts = localeString.trim().split("[_]+");
            String language = parts[0];
            String country = parts.length > 1 ? parts[1] : "";
            String variant = parts.length > 2 ? parts[2] : "";
            return new Locale(language, country, variant);
        }
        return null;
    }

    /**
    /**
     * Get a timestamp for the earliest time that we know the JVM started
     * @return a JVM start time
     */
    public static long getSystemClassloadTime()
    {
        return CLASSLOAD_TIME;
    }

    /**
     * The time on the script files
     */
    private static final long CLASSLOAD_TIME;

    /**
     * Initialize the container start time
     */
    static
    {
        // Browsers are only accurate to the second
        long now = System.currentTimeMillis();
        CLASSLOAD_TIME = now - (now % 1000);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(LocalUtil.class);
}
