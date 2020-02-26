package org.directwebremoting.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
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
import org.directwebremoting.extend.DwrConstants;
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
     * Determines whether the supplied string is a valid script name to use for
     * remoted classes.
     *
     * @param test
     * @return true if the string is a valid script name
     */
    public static boolean isValidScriptName(String test)
    {
        return isSafeHierarchicalIdentifierInBrowser(test);
    }

    /**
     * Determines whether the supplied string is a valid class name to use for
     * class-mapped data classes.
     *
     * @param test
     * @return true if the string is a valid mapped class name
     */
    public static boolean isValidMappedClassName(String test)
    {
        return isSafeHierarchicalIdentifierInBrowser(test);
    }

    /**
     * Tests if a string contains only characters that will allow safe use
     * inside html element attributes and url:s, and is a valid hierarchical
     * identifier wrt to dot ("package") segments.
     *
     * @param test
     * @return true if string is safe
     */
    public static boolean isSafeHierarchicalIdentifierInBrowser(String test)
    {
        if (test.endsWith("/"))
        {
            return false;
        }
        String[] segments = test.split("\\.");
        for (String segment : segments)
        {
            if (segment.equals(""))
            {
                return false;
            }
            if (!isSafeIdentifierInBrowser(segment))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests if a string contains only characters that will allow safe use
     * inside html element attributes and url:s.
     *
     * @param test
     * @return true if string is safe
     */
    public static boolean isSafeIdentifierInBrowser(String test)
    {
        for(int i=0; i<test.length(); i++) {
            char ch = test.charAt(i);

            // Disallow characters that may change parsing mode in HTML
            if ("<>&'\"".indexOf(ch) >= 0)
            {
                return false;
            }

            // Disallow characters that may break URL handling
            //   ;  delimits path parameters
            //   ?  delimits query string
            //   #  delimits anchor string
            if (";?#%".indexOf(ch) >= 0)
            {
                return false;
            }

            // Disallow characters outside the normal-characters ascii range that
            // are not letters or digits
            if ((ch < 32 || 126 < ch) && !Character.isLetterOrDigit(ch))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Expands wildcards in an identifier string by using information in a base identifier name.
     * @param base the base identifier
     * @param wildcarded the wildcarded string to be replaced
     * @return a string that does not contain * or null
     */
    public static String inferWildcardReplacements(String base, String wildcarded)
    {
        String result = wildcarded;
        if (wildcarded != null)
        {
            if ("*".equals(wildcarded))
            {
                result = base.substring(base.lastIndexOf('.') + 1);
            }
            else if ("**".equals(wildcarded))
            {
                result = base;
            }
            else if (wildcarded.indexOf("*") > 0)
            {
                result = wildcarded.replace("*", base.substring(base.lastIndexOf('.') + 1));
            }

            if (!result.equals(wildcarded) && log.isDebugEnabled())
            {
                Loggers.STARTUP.debug("- expanded wildcarded string [" + wildcarded + "] to [" + result + "] for " + base);
            }
        }

        return result;
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

        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");

        // Set to expire far in the past. Prevents caching at the proxy server
        resp.setDateHeader("Expires", 0);
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
     *
     * @param request
     * @return String
     */
    public static String getFullUrlToDwrServlet(HttpServletRequest request) {
        StringBuilder absolutePath = new StringBuilder();
        String scheme = request.getScheme();
        int port = request.getServerPort();

        absolutePath.append(scheme);
        absolutePath.append("://");
        absolutePath.append(request.getServerName());

        if (port > 0 && (("http".equalsIgnoreCase(scheme) && port != 80) || ("https".equalsIgnoreCase(scheme) && port != 443)))
        {
            absolutePath.append(':');
            absolutePath.append(port);
        }

        absolutePath.append(request.getContextPath());
        absolutePath.append(request.getServletPath());

        return absolutePath.toString();
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
        Enumeration<String> headerNames = request.getHeaderNames();
        for (String headerName : iterableizer(headerNames))
        {
            log.debug("  " + headerName + ": " + request.getHeader(headerName));
        }

        StringWriter buffer = new StringWriter();
        ServletInputStream in = null;
        BufferedReader reader = null;
        try
        {
            in = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
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
        finally {
            try
            {
                if (null != in)
                {
                    in.close();
                }
                if (null != reader)
                {
                    reader.close();
                }
            }
            catch(Exception e)
            {
                log.error(e.getMessage());
            }
        }
        log.debug(" " + buffer);

        // The attributes
        log.debug("Attributes attached to the Request:");
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
            if (!(value instanceof String || isWrapper(value)))
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
                    Object param = value;
                    if (value instanceof String)
                    {
                        param = simpleConvert((String) value, propertyType);
                    }
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
     * Checks if this object is an instance of a primitive wrapper.
     *
     * @param object anything
     * @return true if this object is an Integer, Long, Float, Double, Boolean...
     */
    public static final boolean isWrapper(Object object)
    {
        boolean wrapper = false;
        if (object != null)
        {
            wrapper = primitiveWrapperMap.containsValue(object.getClass());
        }
        return wrapper;
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
     * <p>This implementation makes use of the context classloader for
     * the current thread, with fallback to the local classloader if needed.
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

        String remappedClassName = remappedDwrClassName(className);
        Class<?> clazz = null;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(remappedClassName);
        } catch(ClassNotFoundException ex) {
            // fall through
        } catch(IllegalArgumentException ex) {
            // Conform to JDK classloader behaviour where illegal classnames are handled as ClassNotFound
            // fall through
        }

        try {
            clazz = LocalUtil.class.getClassLoader().loadClass(remappedClassName);
        } catch(IllegalArgumentException ex) {
            // Conform to JDK classloader behaviour where illegal classnames are handled as ClassNotFound
            throw new ClassNotFoundException(ex.getMessage(), ex);
        }

        return clazz;
    }

    /**
     * Is Atmosphere/Meteor present on the classpath?
     *
     * @return boolean
     */
    public static boolean isMeteorAvailable() {
        boolean useMeteor = false;
        try {
            classForName("org.atmosphere.cpr.MeteorServlet");
            // If a ClassNotFoundException was not thrown, Atmosphere is in the classpath.
            useMeteor = true;
        } catch (ClassNotFoundException cnfe) {
            // Meteor is not present.
        }
        return useMeteor;
    }

    /**
     * Converts a remapped DWR classname to the corresponding original name in
     * the org.directwebremoting package.
     *
     * @param className
     * @return full class name relative original DWR package
     */
    public static String originalDwrClassName(String className)
    {
        if (isClassNameInDwrRemappedPackage(className))
        {
            return className.substring(packageNamePrefixAndDot.length());
        }
        return className;
    }

    /**
     * Converts a DWR classname in the original org.directwebremoting package
     * to the corresponding name in the remapped package, if applicable.
     *
     * @param className
     * @return full class name relative remapped DWR package
     */
    public static String remappedDwrClassName(String className)
    {
        if (isClassNameInDwrOriginalPackage(className))
        {
            return packageNamePrefixAndDot + className;
        }
        return className;
    }

    /**
     * Determines if a classname resides in the DWR original package
     * (org.directwebremoting).
     *
     * @param className
     * @return true if class in original DWR package
     */
    public static boolean isClassNameInDwrOriginalPackage(String className)
    {
        if (!className.startsWith(DwrConstants.PACKAGE_NAME))
        {
            return false;
        }

        if (className.indexOf(DwrConstants.PACKAGE_NAME, 1) >= 0)
        {
            return false;
        }

        return true;
    }

    /**
     * Determines if a classname resides in the remapped package, if
     * applicable.
     *
     * @param className
     * @return true if class in remapped DWR package
     */
    public static boolean isClassNameInDwrRemappedPackage(String className)
    {
        if (packageNamePrefixAndDot.length() == 0)
        {
            return false;
        }

        if (!className.startsWith(packageNamePrefixAndDot + DwrConstants.PACKAGE_NAME))
        {
            return false;
        }

        return true;
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
     * Utility to retrieve a method.
     *
     * @param clazz - The class where the method exists.
     * @param name - The name of the method to retrieve.
     * @param args - The arguments the method takes.
     * @return - The method, null if an exception is thrown or clazz is null.
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... args)
    {
        if (clazz == null)
        {
            return null;
        }

        try
        {
            return clazz.getMethod(name, args);
        }
        catch (SecurityException ex)
        {
            return null;
        }
        catch (NoSuchMethodException ex)
        {
            return null;
        }
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
     * Adjust a resource path to match any remapping done to the DWR package path.
     *
     * @param path original resource path
     * @return path adjusted wrt package remapping
     */
    public static String remappedResourcePath(String path)
    {
        if (resourcePathPrefix.length() > 0)
        {
            return resourcePathPrefix + (path.startsWith("/") ? "" : "/") + path;
        }
        else
        {
            return path;
        }
    }

    /**
     * Open a stream to an internal file resource located in the DWR package tree.
     *
     * @param path
     * @return an open stream
     */
    public static InputStream getInternalResourceAsStream(String path)
    {
        return LocalUtil.class.getResourceAsStream(remappedResourcePath(path));
    }

    /**
     * Open a stream to a file resource located on the classpath.
     *
     * @param path
     * @return an open stream
     */
    public static URL getResource(String path)
    {
        return LocalUtil.class.getResource(path);
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
     * Make the given constructor accessible, explicitly setting it accessible
     * if necessary. The <code>setAccessible(true)</code> method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * @param ctor the constructor to make accessible
     * @see java.lang.reflect.Constructor#setAccessible
     */
    public static void makeAccessible(Constructor<?> ctor) {
            if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers()))
                            && !ctor.isAccessible()) {
                    ctor.setAccessible(true);
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

        /*
        Once 3.0 is out we should uncomment this and check it still works
        String prefix = "get";
        if (type == Boolean.class || type == Boolean.TYPE)
        {
            prefix = "is";
        }
        */

        String getterName = "get" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);

        try
        {
            Method method = real.getMethod(getterName);
            if (!type.isAssignableFrom(primitiveToWrapper(method.getReturnType())))
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
        if (parameterType instanceof Class<?>)
        {
            return (Class<?>) parameterType;
        }
        else if (parameterType instanceof ParameterizedType)
        {
            return toClass(((ParameterizedType) parameterType).getRawType(), debugContext);
        }
        else if (parameterType instanceof GenericArrayType)
        {
            Type componentType = ((GenericArrayType) parameterType).getGenericComponentType();
            Class<?> componentClass = toClass(componentType, debugContext);
            if (componentClass != null)
            {
                return Array.newInstance(componentClass, 0).getClass();
            }
            else
            {
                log.warn("The inbound Arrays component class is null.");
            }
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
     * Converts the specified primitive Class object to its corresponding wrapper Class object.
     *
     * @param clazz the class to convert, may be null
     * @return the wrapper class for cls or cls if cls is not a primitive. null if null input.
     */
    public static Class<?> primitiveToWrapper(Class<?> clazz)
    {
        Class<?> convertedClass = clazz;
        if ((clazz != null) && (clazz.isPrimitive()))
        {
            convertedClass = primitiveWrapperMap.get(clazz);
        }
        return convertedClass;
    }

    /**
     * Changes the first letter to upper case. No other letters are affected.
     *
     * @param input any
     * @return any
     */
    public static String capitalize(String input)
    {
        String capitalized = input;
        if (hasText(input))
        {
            StringBuffer buf = new StringBuffer(input.length());
            buf.append(Character.toUpperCase(input.charAt(0)));
            buf.append(input.substring(1));
            return buf.toString();
        }
        return capitalized;
    }

    /**
     * Obtains the write method for a property.
     *
     * @param property any
     * @return any
     */
    public static Method getWriteMethod(Class<?> clazz, PropertyDescriptor property)
    {
        Method setter = null;
        if (property != null)
        {
            Method getter = property.getReadMethod();
            setter = property.getWriteMethod();
            if ((getter != null) && (setter == null) && (clazz != null))
            {
                Class<?> type = getter.getReturnType();
                String setterName = "set" + capitalize(property.getName());
                try
                {
                    setter = clazz.getMethod(setterName, new Class[] {type});
                }
                catch (Exception ex)
                {
                    setter = find(setterName, type, clazz.getMethods());
                    if (setter == null)
                    {
                        setter = find(setterName, type, clazz.getDeclaredMethods());
                    }

                }
            }
        }
        if ((setter != null) && (!Modifier.isPublic(setter.getModifiers())))
        {
            setter.setAccessible(true);
        }
        return setter;
    }

    private static Method find(String name, Class<?> type, Method[] methods)
    {
        if (methods != null)
        {
            for (int index = 0; index < methods.length; index++)
            {
                Method method = methods[index];
                Class<?>[] params = method.getParameterTypes();
                if (name.equals(method.getName()) && (params != null) && (params.length == 1))
                {
                    if (type.isAssignableFrom(params[0]))
                    {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if a JavaScript type is assignable to the passed in Java class.
     *
     * @param javaScriptType
     * @param clazz
     * @return boolean
     */
    public static boolean isJavaScriptTypeAssignableTo(String javaScriptType, Class<?> clazz)
    {
        if ("array".equals(javaScriptType))
        {
            return isJavaScriptArrayConvertableTo(clazz);
        }
        else if ("boolean".equals(javaScriptType))
        {
            return isJavaScriptBooleanConvertableTo(clazz);
        }
        else if ("number".equals(javaScriptType))
        {
            return isJavaScriptNumberConvertableTo(clazz);
        }
        else if ("string".equals(javaScriptType))
        {
            return isJavaScriptStringConvertableTo(clazz);
        }
        else if ("date".equals(javaScriptType))
        {
            return isJavaScriptDateConvertableTo(clazz);
        }
        else if ("Object_Object".equals(javaScriptType))
        {
            return isJavaScriptObjectConvertableTo(clazz);
        }
        // We may need to revisit this, we aren't explicitly handling some types including:
        // "reference", "null", "Object_ObjectWithLightClassMapping", etc.
        return true;
    }

    /**
     * Can a JavaScript "array" be converted to type?
     *
     * @param type
     * @return boolean
     */
    private static boolean isJavaScriptArrayConvertableTo(Class<?> type)
    {
        return (type.isArray() || Collection.class.isAssignableFrom(type));
    }

    /**
     * Can a JavaScript "object" be converted to type?
     *
     * @param type
     * @return boolean
     */
    private static boolean isJavaScriptObjectConvertableTo(Class<?> type)
    {
        return !(isTypeSimplyConvertable(type) || isJavaScriptArrayConvertableTo(type));
    }

    /**
     * Can a JavaScript "boolean" be converted to type?
     *
     * @param type
     * @return boolean
     */
    private static boolean isJavaScriptBooleanConvertableTo(Class<?> type)
    {
        return (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type));
    }

    private static final List<?> TYPES_COMPATIBLE_WITH_JS_NUMBER =  Arrays.asList(new Class[] { Byte.TYPE, Byte.class, Short.TYPE, Short.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Float.TYPE, Float.class, Double.TYPE, Double.class, BigDecimal.class, BigInteger.class });

    /**
     * Can a JavaScript "number" be converted to type?
     *
     * @param type
     * @return boolean
     */
    private static boolean isJavaScriptNumberConvertableTo(Class<?> type)
    {
        return (TYPES_COMPATIBLE_WITH_JS_NUMBER.contains(type));
    }

    /**
     * Can a JavaScript "string" be converted to type?
     *
     * @param type
     * @return boolean
     */
    private static boolean isJavaScriptStringConvertableTo(Class<?> type)
    {
        return String.class.isAssignableFrom(type) || char.class.equals(type) || Locale.class.equals(type) || Currency.class.equals(type);
    }

    /**
     *
     * @param type
     * @return boolean
     */
    private static boolean isJavaScriptDateConvertableTo(Class<?> type)
    {
        return (Date.class.isAssignableFrom(type));
    }

    /**
     * Get a timestamp for the earliest time that we know the JVM started
     * @return a JVM start time
     */
    public static long getSystemClassloadTime()
    {
        return CLASSLOAD_TIME;
    }

    /**
     * Package to add as prefix to DWR's default Java package (when remapping
     * DWR in classpath)
     */
    private static String packageNamePrefixAndDot;

    /**
     * Path to add as prefix to DWR's default resource path (when remapping DWR
     * in classpath)
     */
    private static String resourcePathPrefix;

    /**
     * The time on the script files
     */
    private static final long CLASSLOAD_TIME;

    private static Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();

    static
    {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(LocalUtil.class);

    /**
     * Initialize at start time
     */
    static
    {
        // Container start time (browsers are only accurate to the second)
        long now = System.currentTimeMillis();
        CLASSLOAD_TIME = now - (now % 1000);

        // Set up remapping paths
        String expectedPackage = DwrConstants.PACKAGE_NAME + ".util";
        String actualPackage = LocalUtil.class.getPackage().getName();
        if (!actualPackage.endsWith(expectedPackage))
        {
            log.error("Disallowed remapping of DWR classes - only change of prefix is allowed and the org.directwebremoting package tree must be kept intact.");
            throw new Error("Disallowed remapping of DWR classes.");
        }
        packageNamePrefixAndDot = actualPackage.substring(0, actualPackage.indexOf(expectedPackage));
        if (packageNamePrefixAndDot.length() > 0)
        {
            resourcePathPrefix = "/" + packageNamePrefixAndDot.replace('.', '/').substring(0, packageNamePrefixAndDot.length() - 1);
            log.info("Detected repackaging of DWR - using packageNamePrefix=" + packageNamePrefixAndDot + ", resourcePathPrefix=" +   resourcePathPrefix);
        }
        else
        {
            resourcePathPrefix = "";
        }
    }

    /**
     * Find the leaf cause of an exception chain.
     */
    public static Throwable getRootCause(Throwable ex)
    {
        if (ex.getCause() == null) {
            return ex;
        } else {
            return getRootCause(ex.getCause());
        }
    }

}
