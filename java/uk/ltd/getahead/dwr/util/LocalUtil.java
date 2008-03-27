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
package uk.ltd.getahead.dwr.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.ConversionConstants;

/**
 * Various utilities, mostly to make up for JDK 1.4 functionallity that is not
 * in JDK 1.3
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
     * Prevent instansiation
     */
    private LocalUtil()
    {
    }

    /**
     * Replacement for String#replaceAll(String, String) in JDK 1.4+
     * @param text source text
     * @param repl the stuff to get rid of
     * @param with the stuff to replace it with
     * @return replaced text or null if any args are null
     */
    public static String replace(String text, String repl, String with)
    {
        if (text == null || repl == null || with == null || repl.length() == 0)
        {
            return text;
        }

        StringBuffer buf = new StringBuffer(text.length());
        int searchFrom = 0;
        while (true)
        {
            int foundAt = text.indexOf(repl, searchFrom);
            if (foundAt == -1)
            {
                break;
            }

            buf.append(text.substring(searchFrom, foundAt)).append(with);
            searchFrom = foundAt + repl.length();
        }
        buf.append(text.substring(searchFrom));

        return buf.toString();
    }

    /**
     * True if c1 is java.lang.Boolean and c2 is boolean, etc.
     * @param c1 the first class to test
     * @param c2 the second class to test
     * @return true if the classes are equivalent
     */
    public static boolean isEquivalent(Class c1, Class c2)
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
     * @return The non-privitive version of the class
     */
    public static Class getNonPrimitiveType(Class type)
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
     * Add headers to prevent browers and proxies from caching this reply.
     * @param resp The response to add headers to
     */
    public static void addNoCacheHeaders(HttpServletResponse resp)
    {
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); //$NON-NLS-1$ //$NON-NLS-2$

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0"); //$NON-NLS-1$ //$NON-NLS-2$

        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$

        // Set to expire far in the past. Prevents caching at the proxy server
        resp.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Is this class one that we auto fill, so the user can ignore?
     * @param paramType The type to test
     * @return true if the type is a Servlet type
     */
    public static boolean isServletClass(Class paramType)
    {
        return paramType == HttpServletRequest.class ||
               paramType == HttpServletResponse.class ||
               paramType == ServletConfig.class ||
               paramType == ServletContext.class ||
               paramType == HttpSession.class;
    }

    /**
     * URL decode a value. This method gets around the lack of a
     * decode(String, String) method in JDK 1.3.
     * @param value The string to decode
     * @return The decoded string
     */
    public static String decode(String value)
    {
        if (!testedDecoder)
        {
            try
            {
                decode14 = URLDecoder.class.getMethod("decode", new Class[] { String.class, String.class }); //$NON-NLS-1$
            }
            catch (Exception ex)
            {
                if (!warn13)
                {
                    log.warn("URLDecoder.decode(String, String) is not available. Falling back to 1.3 variant."); //$NON-NLS-1$
                    warn13 = true;
                }
            }

            testedDecoder = true;
        }

        if (decode14 != null)
        {
            try
            {
                return (String) decode14.invoke(null, new Object[] { value, "UTF-8" }); //$NON-NLS-1$
            }
            catch (Exception ex)
            {
                log.warn("Failed to use JDK 1.4 decoder", ex); //$NON-NLS-1$
            }
        }

        return URLDecoder.decode(value);
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
        Class real = object.getClass();

        String setterName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1); //$NON-NLS-1$
        Method method = real.getMethod(setterName, new Class[] { value.getClass() });

        if (method == null)
        {
            throw new NoSuchMethodException("Missing property: "  + key); //$NON-NLS-1$
        }

        method.invoke(object, new Object[] { value });
    }

    /**
     * The javascript outbound marshaller prefixes the toString value with a
     * colon and the original type information. This undoes that.
     * @param data The string to be split up
     * @return A string array containing the split data
     */
    public static String[] splitInbound(String data)
    {
        String[] reply = new String[2];
    
        int colon = data.indexOf(ConversionConstants.INBOUND_TYPE_SEPARATOR);
        if (colon == -1)
        {
            log.error("Missing : in conversion data (" + data + ')'); //$NON-NLS-1$
            reply[LocalUtil.INBOUND_INDEX_TYPE] = ConversionConstants.TYPE_STRING;
            reply[LocalUtil.INBOUND_INDEX_VALUE] = data;
        }
        else
        {
            reply[LocalUtil.INBOUND_INDEX_TYPE] = data.substring(0, colon);
            reply[LocalUtil.INBOUND_INDEX_VALUE] = data.substring(colon + 1);
        }
    
        return reply;
    }

    /**
     * Get the short class name (i.e. without the package part)
     * @param clazz the class to get the short name of
     * @return the class name of the class without the package name
     */
    public static String getShortClassName(Class clazz)
    {
        String className = clazz.getName();

        char[] chars = className.toCharArray();
        int lastDot = 0;
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] == '.')
            {
                lastDot = i + 1;
            }
            else if (chars[i] == '$')
            {
                chars[i] = '.';
            }
        }

        return new String(chars, lastDot, chars.length - lastDot);
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(LocalUtil.class);

    /**
     * Have we given a warning about URLDecoder.decode() in jdk 1.3
     */
    private static boolean warn13 = false;

    /**
     * Have we tested for the correct URLDecoder.decode()
     */
    private static boolean testedDecoder = false;

    /**
     * Are we using the jdk 1.4 version of URLDecoder.decode()
     */
    private static Method decode14 = null;
}
