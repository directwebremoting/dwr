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
package org.directwebremoting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.directwebremoting.servlet.DwrWebContextFilter;

/**
 * A ScriptBuffer is like a StringBuffer except that it is used to create
 * Javascript commands. There are 2 version of the <code>append()</code> method:
 * {@link #appendScript(String)} (and the {@link #appendScript(char)} variant)
 * which assume that the parameter is to be inserted literally into the output.
 * And {@link #appendData(String)} (and variants for Object and primitive types)
 * which assumes that the parameter is a variable which should be properly
 * converted, escaped and quoted.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ScriptBuffer
{
    /**
     * Constructor for use from a WebContext enabled thread - that is one that
     * has either been started from a DWR call, or that has been started from a
     * normal HTTP request that has been wrapped with a {@link DwrWebContextFilter}.
     * This allows us to get ourselves a {@link ConverterManager}.
     */
    public ScriptBuffer()
    {
        init(WebContextFactory.get().getContainer());
    }

    /**
     * Constructor for use from a WebContext enabled thread - that is one that
     * has either been started from a DWR call, or that has been started from a
     * normal HTTP request that has been wrapped with a {@link DwrWebContextFilter}
     * This allows us to get ourselves a {@link ConverterManager}.
     * <p>This is a convenience constructor that takes a string which is passed
     * to {@link #appendScript(String)}.
     * @param str The initial string to place in the buffer
     */
    public ScriptBuffer(String str)
    {
        init(WebContextFactory.get().getContainer());
        appendScript(str);
    }

    /**
     * Constructor for use from a non-WebContext enabled thread.
     * The {@link ServletContext} allows us to look up a {@link ServerContext}
     * From which we get ourselves a {@link ConverterManager}.
     * @param context The web-app that this DWR is a part of.
     */
    public ScriptBuffer(ServletContext context)
    {
        init(ServerContextFactory.get(context).getContainer());
    }

    /**
     * Constructor for use from a non-WebContext enabled thread.
     * The {@link ServletContext} allows us to look up a {@link ServerContext}
     * From which we get ourselves a {@link ConverterManager}.
     * <p>This is a convenience constructor that takes a string which is passed
     * to {@link #appendScript(String)}.
     * @param context The web-app that this DWR is a part of
     * @param str The initial string to place in the buffer
     */
    public ScriptBuffer(ServletContext context, String str)
    {
        init(ServerContextFactory.get(context).getContainer());
        appendScript(str);
    }

    /**
     * Constructor for use from a non-WebContext enabled thread.
     * The {@link ServerContext} allows us to look up a {@link Container}
     * From which we get ourselves a {@link ConverterManager}.
     * @param serverContext The way we convert objects to Javascript
     */
    public ScriptBuffer(ServerContext serverContext)
    {
        init(serverContext.getContainer());
    }

    /**
     * Constructor for use from a non-WebContext enabled thread.
     * The {@link ServerContext} allows us to look up a {@link Container}
     * From which we get ourselves a {@link ConverterManager}.
     * <p>This is a convenience constructor that takes a string which is passed
     * to {@link #appendScript(String)}.
     * @param serverContext The way we convert objects to Javascript
     * @param str The initial string to place in the buffer
     */
    public ScriptBuffer(ServerContext serverContext, String str)
    {
        init(serverContext.getContainer());
        appendScript(str);
    }

    /**
     * Constructor for use from a non-WebContext enabled thread.
     * The {@link ServletContext} allows us to look up a {@link ServerContext}
     * From which we get ourselves a {@link ConverterManager}.
     * @param converterManager The way we convert objects to Javascript
     */
    public ScriptBuffer(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * Constructor for use from a non-WebContext enabled thread.
     * The {@link ServletContext} allows us to look up a {@link ServerContext}
     * From which we get ourselves a {@link ConverterManager}.
     * <p>This is a convenience constructor that takes a string which is passed
     * to {@link #appendScript(String)}.
     * @param converterManager The way we convert objects to Javascript
     * @param str The initial string to place in the buffer
     */
    public ScriptBuffer(ConverterManager converterManager, String str)
    {
        this.converterManager = converterManager;
        appendScript(str);
    }

    /**
     * Constructor for use from a non-WebContext enabled thread.
     * The {@link Container} allows us to look up a {@link ConverterManager}
     * @param container From which we extract a {@link ConverterManager}
     */
    public ScriptBuffer(Container container)
    {
        init(container);
    }

    /**
     * Constructor for use from a non-WebContext enabled thread.
     * The {@link Container} allows us to look up a {@link ConverterManager}
     * @param container From which we extract a {@link ConverterManager}
     * @param str The initial string to place in the buffer
     */
    public ScriptBuffer(Container container, String str)
    {
        init(container);
        appendScript(str);
    }

    /**
     * Check that we've got everything
     * @param container From which we extract a {@link ConverterManager}
     */
    private void init(Container container)
    {
        init((ConverterManager) container.getBean(ConverterManager.class.getName()));
    }

    /**
     * Check that we've got everything
     * @param cMgr Used to convert objects to Javascript
     */
    private void init(ConverterManager cMgr)
    {
        this.converterManager = cMgr;
        if (cMgr == null)
        {
            throw new IllegalStateException("");
        }
    }

    /**
     * @param b The boolean to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(boolean)
     */
    public synchronized ScriptBuffer appendData(boolean b)
    {
        Boolean data = new Boolean(b);
        parts.add(data);
        return this;
    }

    /**
     * @param c The char to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(char)
     */
    public synchronized ScriptBuffer appendData(char c)
    {
        parts.add(new Character(c));
        return this;
    }

    /**
     * @param d The double to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(double)
     */
    public synchronized ScriptBuffer appendData(double d)
    {
        parts.add(new Double(d));
        return this;
    }

    /**
     * @param f The float to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(float)
     */
    public synchronized ScriptBuffer appendData(float f)
    {
        parts.add(new Float(f));
        return this;
    }

    /**
     * @param i The int to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(int)
     */
    public synchronized ScriptBuffer appendData(int i)
    {
        parts.add(new Integer(i));
        return this;
    }

    /**
     * @param l The long to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(long)
     */
    public synchronized ScriptBuffer appendData(long l)
    {
        parts.add(new Long(l));
        return this;
    }

    /**
     * @param obj The Object to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(java.lang.Object)
     */
    public synchronized ScriptBuffer appendData(Object obj)
    {
        parts.add(obj);
        return this;
    }

    /**
     * @param str The String to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(java.lang.String)
     */
    public synchronized ScriptBuffer appendData(String str)
    {
        parts.add(str);
        return this;
    }

    /**
     * @param str The String to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(java.lang.String)
     */
    public synchronized ScriptBuffer appendScript(String str)
    {
        parts.add(new StringCodeWrapper(str));
        return this;
    }

    /**
     * @param c The char to add to the script
     * @return this. To allow sv.append(x).append(y).append(z);
     * @see java.lang.StringBuffer#append(char)
     */
    public synchronized ScriptBuffer appendScript(char c)
    {
        parts.add(new CharCodeWrapper(c));
        return this;
    }

    /**
     * Return a string ready for output.
     * @return Some Javascript to be eval()ed by a browser.
     */
    public synchronized String createOutput()
    {
        OutboundContext context = new OutboundContext();
        
        StringBuffer output = new StringBuffer();
        
        // First we look for the initialization code
        for (Iterator it = parts.iterator(); it.hasNext();)
        {
            Object element = it.next();
            if (!(element instanceof CodeWrapper))
            {
                OutboundVariable ov = converterManager.convertOutbound(element, context);
                output.append(ov.getInitCode());
            }
        }
        
        // Then we output everything else
        for (Iterator it = parts.iterator(); it.hasNext();)
        {
            Object element = it.next();
            if (element instanceof StringCodeWrapper)
            {
                StringCodeWrapper str = (StringCodeWrapper) element;
                output.append(str.data);
            }
            else if (element instanceof CharCodeWrapper)
            {
                CharCodeWrapper str = (CharCodeWrapper) element;
                output.append(str.data);
            }
            else
            {
                OutboundVariable ov = converterManager.convertOutbound(element, context);
                output.append(ov.getAssignCode());
            }
        }
        
        String exported = output.toString();
        return exported;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return createOutput();
    }

    /**
     * A marker interface so we can detect the 'implementations' below.
     */
    private interface CodeWrapper
    {
    }

    /**
     * A wrapper around a string to distinguish a string entered into this
     * buffer as code and a string entered as data
     */
    private class StringCodeWrapper implements CodeWrapper
    {
        StringCodeWrapper(String data)
        {
            this.data = data;
        }

        String data;
    }

    /**
     * A wrapper around a char to distinguish a char entered into this buffer
     * as code and a char entered as data
     */
    private class CharCodeWrapper implements CodeWrapper
    {
        CharCodeWrapper(char data)
        {
            this.data = data;
        }

        char data;
    }

    /**
     * What we use to convert data to Javascript
     */
    private ConverterManager converterManager;

    /**
     * This is where we store all the script components waiting to be serialized
     */
    private List parts = new ArrayList();
}
