package uk.ltd.getahead.dwr.util;

import java.lang.reflect.Method;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

/**
 * Various utilities, mostly to make up for JDK 1.4 functionallity that is not
 * in JDK 1.3
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class LocalUtil
{
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
     * True if c1 is java.lang.Boolean and c2 is boolean
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
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");

        // Set to expire far in the past. Prevents caching at the proxy server
        resp.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
    }

    /**
     * URL dencode a value. This method gets around the lack of a
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
                decode14 = URLDecoder.class.getMethod("decode", new Class[] { String.class, String.class });
            }
            catch (Exception ex)
            {
                Log.warn("URLDecoder.decode(String, String) is not available. Falling back to 1.3 variant.");
            }

            testedDecoder = true;
        }

        if (decode14 != null)
        {
            try
            {
                return (String) decode14.invoke(null, new Object[] { value, "UTF-8" });
            }
            catch (Exception ex)
            {
                Log.warn("Failed to use JDK 1.4 decoder", ex);
            }
        }

        return URLDecoder.decode(value);
    }

    private static boolean testedDecoder = false;
    private static Method decode14 = null;
}
