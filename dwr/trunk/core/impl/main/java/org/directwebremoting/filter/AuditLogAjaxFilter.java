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
package org.directwebremoting.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

/**
 * An example filter that logs method calls and their replies
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AuditLogAjaxFilter implements AjaxFilter, LogAjaxFilter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.AjaxFilter#doFilter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], org.directwebremoting.AjaxFilterChain)
     */
    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception
    {
        StringBuilder call = new StringBuilder();
        call.append(obj.getClass().getSimpleName());
        call.append('.');
        call.append(method.getName());
        call.append('(');
        for (int i = 0; i < params.length; i++)
        {
            if (i != 0)
            {
                call.append(", ");
            }
            call.append(toString(params[i]));
        }
        call.append(')');

        try
        {
            Object reply = chain.doFilter(obj, method, params);
            log.info(call.toString() + " = " + toString(reply));
            return reply;
        }
        catch (InvocationTargetException ex)
        {
            log.info(call.toString() + " = " + ex.getTargetException(), ex.getTargetException());
            throw ex;
        }
        catch (Exception ex)
        {
            log.info(call.toString() + " = " + ex, ex);
            throw ex;
        }
        catch (Error ex)
        {
            log.warn(call.toString() + " = " + ex, ex);
            throw ex;
        }
    }

    /**
     * Similar to {@link Object#toString()} except that we ensure we don't
     * return long strings, and format strings with quotes. We're probably more
     * interested in what it is rather than the exact detail.
     */
    private String toString(Object reply)
    {
        if (reply instanceof String)
        {
            String str = (String) reply;
            if (str.length() > 10)
            {
                return '"' + str.substring(0, 7) + "...\"";
            }
            else
            {
                return '"' + str + '"';
            }
        }

        if (reply == null)
        {
            return "null";
        }

        if (toStringClasses.contains(reply.getClass()))
        {
            return reply.toString();
        }

        return reply.getClass().getSimpleName() + "(...)";
    }

    /**
     * A list of the classes that we just do a direct toString() on
     */
    private static final List<Class<?>> toStringClasses = new ArrayList<Class<?>>();
    static
    {
        toStringClasses.add(java.lang.Boolean.TYPE);
        toStringClasses.add(java.lang.Character.TYPE);
        toStringClasses.add(java.lang.Byte.TYPE);
        toStringClasses.add(java.lang.Short.TYPE);
        toStringClasses.add(java.lang.Integer.TYPE);
        toStringClasses.add(java.lang.Long.TYPE);
        toStringClasses.add(java.lang.Float.TYPE);
        toStringClasses.add(java.lang.Double.TYPE);
        toStringClasses.add(java.lang.Void.TYPE);

        toStringClasses.add(java.lang.Boolean.class);
        toStringClasses.add(java.lang.Character.class);
        toStringClasses.add(java.lang.Byte.class);
        toStringClasses.add(java.lang.Short.class);
        toStringClasses.add(java.lang.Integer.class);
        toStringClasses.add(java.lang.Long.class);
        toStringClasses.add(java.lang.Float.class);
        toStringClasses.add(java.lang.Double.class);
        toStringClasses.add(java.lang.Void.class);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(AuditLogAjaxFilter.class);
}
