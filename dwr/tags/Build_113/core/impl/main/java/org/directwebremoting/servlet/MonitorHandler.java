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
package org.directwebremoting.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * A monitoring system so we can see what is going on inside DWR
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class MonitorHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (!debug)
        {
            log.warn("Failed attempt to access test pages outside of debug mode. Set the debug init-parameter to true to enable.");
            throw new SecurityException("Access to debug pages is denied.");
        }

        response.setContentType(MimeConstants.MIME_HTML);
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>DWR - System Monitor</title></head><body>");

        WebContext webContext = WebContextFactory.get();

        out.print("<h1>DWR - System Monitor</h1>");
        out.print("<h2>Global Settings:</h2>");

        String contextPath = webContext.getContextPath();
        out.print("<p>ContextPath: " + contextPath + "</p>");
        out.print("<p>Current Page: " + webContext.getCurrentPage() + "</p>");

        //ScriptSession scriptSession = webContext.getScriptSession();

        Container container = webContext.getContainer();
        SortedMap<String, Object> beans = new TreeMap<String, Object>();
        SortedMap<String, String> settings = new TreeMap<String, String>();
        SortedMap<String, String> urls = new TreeMap<String, String>();
        for (String name : container.getBeanNames())
        {
            Object bean = container.getBean(name);
            if (name.startsWith("url:"))
            {
                urls.put(name.substring(4), bean.getClass().getName());
            }
            else if (bean instanceof String)
            {
                settings.put(name, bean.toString());
            }
            else
            {
                beans.put(name, bean);
            }
        }

        // Add all the beans to an ID map for <a name=ID>
        IdManager ids = new IdManager();

        // Remove the URL re-writers from the Settings map
        for (Map.Entry<String, String> urlEntry : urls.entrySet())
        {
            for (Iterator<Map.Entry<String, String>> it = settings.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry<String, String> settingEntry = it.next();
                if (urlEntry.getKey().equals(settingEntry.getValue()))
                {
                    it.remove();
                    urls.put(urlEntry.getKey(), urlEntry.getValue() + " (" + settingEntry.getKey() + ")");
                }
            }
        }

        out.print("<h2>Beans:</h2>");
        for (Map.Entry<String, Object> entry : beans.entrySet())
        {
            String name = entry.getKey();
            Object object = entry.getValue();

            digWhatever(out, ids, name, object);
        }

        out.print("<h2>Settings:</h2>");
        for (Map.Entry<String, String> entry : settings.entrySet())
        {
            out.print("<p>" + entry.getKey() + ": \"" + entry.getValue() + "\"</p>");
        }

        out.print("<h2>URLs:</h2>");
        String prefix = contextPath + webContext.getHttpServletRequest().getServletPath();
        for (Map.Entry<String, String> entry : urls.entrySet())
        {
            out.print("<p><a href='" + prefix + entry.getKey() + "'>" + entry.getKey() + "</a>: " + entry.getValue() + "</p>");
        }

        webContext.getContextPath();
        out.print("</body></html>");
    }

    /**
     * @param out
     * @param ids
     * @param name
     * @param object
     */
    private void digWhatever(PrintWriter out, IdManager ids, String name, Object object)
    {
        if (object == null || object instanceof Number || object instanceof Boolean)
        {
            digSimple(out, name, object);
        }
        else if (object instanceof Collection<?>)
        {
            Collection<?> collection = (Collection<?>) object;
            boolean simple = true;
            for (Object child : collection)
            {
                if (!(child instanceof Number) &&
                     !(child instanceof Boolean) &&
                     !(child instanceof String))
                {
                    simple = false;
                }
            }
            if (simple)
            {
                digSimple(out, name, object);
            }
            else
            {
                digCollection(out, ids, name, collection);
            }
        }
        else if (object instanceof Map<?, ?>)
        {
            digMap(out, ids, name, (Map<?, ?>) object);
        }
        else if (object.getClass().getName().startsWith("java") ||
                object.getClass().getName().startsWith("com.sun"))
        {
            digSimple(out, name, object);
        }
        else if (object instanceof String)
        {
            digString(out, name, (String) object);
        }
        else
        {
            digObject(out, ids, name, object);
        }
    }

    /**
     * @param out
     * @param name
     * @param object
     */
    private void digSimple(PrintWriter out, String name, Object object)
    {
        out.print("<div class='section'>");
        out.print("<div class='title'>" + name + ": " + object.toString() + "</div>");
        out.print("</div>");
    }

    /**
     * @param out
     * @param name
     * @param object
     */
    private void digString(PrintWriter out, String name, String object)
    {
        out.print("<div class='section'>");
        out.print("<div class='title'>" + name + ": \"" + object + "\"</div>");
        out.print("</div>");
    }

    /**
     * 
     */
    private void digCollection(PrintWriter out, IdManager ids, String name, Collection<?> collection)
    {
        out.print("<div class='section'>");
        String id = ids.getIfExists(collection);
        if (id != null)
        {
            out.print("<div class='title'>" + name + ": <a href='#id" + id + "'>Map</a></div>");
        }
        else
        {
            id = ids.allocate(collection);

            out.print("<div class='title'><a name='id" + id + "'>" + name + "</a>: Map</div>");
            out.print("<ul>");
            int i = 0;
            for (Object entry : collection)
            {
                digWhatever(out, ids, "" + i, entry.toString());
                i++;
            }
            out.print("</ul>");
        }
    }

    /**
     * 
     */
    private void digMap(PrintWriter out, IdManager ids, String name, Map<?, ?> map)
    {
        out.print("<div class='section'>");
        String id = ids.getIfExists(map);
        if (id != null)
        {
            out.print("<div class='title'>" + name + ": <a href='#id" + id + "'>Map</a></div>");
        }
        else
        {
            id = ids.allocate(map);

            out.print("<div class='title'><a name='id" + id + "'>" + name + "</a>: Map</div>");
            out.print("<ul>");
            for (Map.Entry<?, ?> entry : map.entrySet())
            {
                Object value = entry.getValue();
                digWhatever(out, ids, entry.getValue().toString(), value);
            }
            out.print("</ul>");
        }
    }

    /**
     * 
     */
    private void digException(PrintWriter out, String name, Throwable ex)
    {
        out.print("<div class='section'>");
        out.print("<div class='title'>" + name + ": " + ex.getClass().getSimpleName() + "(" + ex.getMessage() + ")</div>");
        out.print("</div>");
    }

    /**
     * 
     */
    private void digObject(PrintWriter out, IdManager ids, String name, Object object)
    {
        Class<?> type = object.getClass();
        if (object instanceof Log)
        {
            return;
        }

        out.print("<div class='section'>");
        String id = ids.getIfExists(object);
        if (id != null)
        {
            out.print("<div class='title'>" + name + ": <a href='#id" + id + "'>" + type.getSimpleName() + "</a></div>");
        }
        else
        {
            id = ids.allocate(object);

            out.print("<div class='title'><a name='id" + id + "'>" + name + "</a>: " + type.getName() + "</div>");
            out.print("<ul>");
            for (Field field : LocalUtil.getAllFields(type))
            {
                try
                {
                    if (Modifier.isStatic(field.getModifiers()))
                    {
                        continue;
                    }

                    field.setAccessible(true);
                    Object value = field.get(object);
                    digWhatever(out, ids, field.getName(), value);
                }
                catch (Exception ex)
                {
                    digException(out, field.getName(), ex);
                }
            }
            out.print("</ul>");
        }
    }

    protected static class IdManager
    {
        /**
         * 
         */
        public String getIfExists(Object object)
        {
            return allocated.get(object);
        }

        protected String allocate(Object object)
        {
            if (allocated.containsKey(object))
            {
                throw new IllegalStateException("object already exists");
            }

            String id = "id" + nextId;
            nextId++;
            allocated.put(object, id);

            return id;
        }

        private int nextId;

        private Map<Object, String> allocated = new HashMap<Object, String>();
    }

    /**
     * Set the debug status
     * @param debug The new debug setting
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * Are we in debug mode?
     */
    protected boolean debug = false;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(MonitorHandler.class);
}
