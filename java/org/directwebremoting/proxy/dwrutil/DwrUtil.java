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
package org.directwebremoting.proxy.dwrutil;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;

import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * DwrUtil is a server-side proxy that allows Java programmers to call client
 * side Javascript from Java.
 * <p>
 * Each DwrUtil object is associated with a list of ScriptSessions and the
 * proxy code is creates will be dynamically forwarded to all those browsers.
 * <p>
 * Currently this class contains only the write-only DOM manipulation functions
 * from DwrUtil. It is possible that we could add the read methods, however
 * the complexity in the callback and the fact that you are probably not going
 * to need it means that we'll leave it for another day. Specifically,
 * <code>getValue</code>, <code>getValues</code> and <code>getText</code> have
 * been left out as being read functions and <code>useLoadingMessage</code> etc
 * have been left out as not being DOM related.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrUtil extends ScriptProxy
{
    /**
     * Http thread constructor, that affects no browsers.
     * Calls to {@link DwrUtil#addScriptSession(ScriptSession)} or to
     * {@link DwrUtil#addScriptSessions(Collection)} will be needed  
     */
    public DwrUtil()
    {
        super();
    }

    /**
     * Non-http thread constructor, that affects no browsers.
     * Calls to {@link DwrUtil#addScriptSession(ScriptSession)} or to
     * {@link DwrUtil#addScriptSessions(Collection)} will be needed
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public DwrUtil(ServletContext sctx)
    {
        super(sctx);
    }

    /**
     * Http thread constructor that alters a single browser
     * @param scriptSession The browser to alter
     */
    public DwrUtil(ScriptSession scriptSession)
    {
        super(scriptSession);
    }

    /**
     * Non-http thread constructor that alters a single browser
     * @param scriptSession The browser to alter
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public DwrUtil(ScriptSession scriptSession, ServletContext sctx)
    {
        super(scriptSession, sctx);
    }

    /**
     * Http thread constructor that alters a number of browsers
     * @param scriptSessions A collection of ScriptSessions that we should act on.
     */
    public DwrUtil(Collection scriptSessions)
    {
        super(scriptSessions);
    }

    /**
     * Non-http thread constructor that alters a number of browsers
     * @param scriptSessions The browsers to alter
     * @param sctx The servlet context to allow us to locate a webapp
     */
    public DwrUtil(Collection scriptSessions, ServletContext sctx)
    {
        super(scriptSessions, sctx);
    }

    /**
     * Set the value an HTML element to the specified value.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/util/setvalue">More</a>.
     * @param elementId The HTML element to update (by id)
     * @param value The text to insert into the HTML element
     * @throws MarshallException 
     */
    public void setValue(String elementId, String value) throws MarshallException
    {
        setValue(elementId, value, false);
    }

    /**
     * Set the value an HTML element to the specified value.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/util/setvalue">More</a>.
     * @param elementId The HTML element to update (by id)
     * @param value The text to insert into the HTML element
     * @param escapeHtml Should we escape HTML characters?
     * @throws MarshallException If the data can not be marshalled
     */
    public void setValue(String elementId, String value, boolean escapeHtml) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);
        OutboundVariable valueOv = getServerContext().toJavascript(value);
        String options = escapeHtml ? ", {escapeHtml:true}" : ""; //$NON-NLS-1$ //$NON-NLS-2$

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append(valueOv.getInitCode())
            .append("DWRUtil.setValue(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(',')
            .append(valueOv.getAssignCode())
            .append(options)
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * Given a map, call setValue() for all the entries in the map using the
     * entry key as an element id.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/util/setvalues">More</a>.
     * @param values The map of elementIds to values to alter
     * @param escapeHtml Should we escape HTML characters?
     * @throws MarshallException If the data can not be marshalled
     */
    public void setValues(Map values, boolean escapeHtml) throws MarshallException
    {
        OutboundVariable valuesOv = getServerContext().toJavascript(values);
        String options = escapeHtml ? "{escapeHtml:true}" : "null"; //$NON-NLS-1$ //$NON-NLS-2$

        StringBuffer script = new StringBuffer();
        script.append(valuesOv.getInitCode())
            .append("DWRUtil.setValues(") //$NON-NLS-1$
            .append(valuesOv.getAssignCode())
            .append(',')
            .append(options)
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * Add options to a list from an array or map.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/lists">More</a>.
     * @param elementId The HTML element to update (by id)
     * @param array An array of strings to use as both value and text of options
     * @throws MarshallException If the data can not be marshalled
     */
    public void addOptions(String elementId, String[] array) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);
        OutboundVariable arrayOv = getServerContext().toJavascript(array);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append(arrayOv.getInitCode())
            .append("DWRUtil.addOptions(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(',')
            .append(arrayOv.getAssignCode())
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * Add options to a list from an array or map.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/lists">More</a>.
     * @param elementId The HTML element to update (by id)
     * @param array And array of objects from which to create options
     * @param property The object property to use for the option value and text
     * @throws MarshallException If the data can not be marshalled
     */
    public void addOptions(String elementId, Collection array, String property) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);
        OutboundVariable arrayOv = getServerContext().toJavascript(array);
        OutboundVariable propertyOv = getServerContext().toJavascript(property);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append(arrayOv.getInitCode())
            .append("DWRUtil.addOptions(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(',')
            .append(arrayOv.getAssignCode())
            .append(',')
            .append(propertyOv.getAssignCode())
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * Add options to a list from an array or map.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/lists">More</a>.
     * @param elementId The HTML element to update (by id)
     * @param array And array of objects from which to create options
     * @param valueProperty The object property to use for the option value
     * @param textProperty The object property to use for the option text
     * @throws MarshallException If the data can not be marshalled
     */
    public void addOptions(String elementId, Collection array, String valueProperty, String textProperty) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);
        OutboundVariable arrayOv = getServerContext().toJavascript(array);
        OutboundVariable valuePropertyOv = getServerContext().toJavascript(valueProperty);
        OutboundVariable textPropertyOv = getServerContext().toJavascript(textProperty);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append(arrayOv.getInitCode())
            .append("DWRUtil.addOptions(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(',')
            .append(arrayOv.getAssignCode())
            .append(',')
            .append(valuePropertyOv.getAssignCode())
            .append(',')
            .append(textPropertyOv.getAssignCode())
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * Remove all the options from a select list (specified by id)
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/lists">More</a>.
     * @param elementId The HTML element to update (by id)
     * @throws MarshallException If the data can not be marshalled
     */
    public void removeAllOptions(String elementId) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append("DWRUtil.removeAllOptions(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * Create rows inside a the table, tbody, thead or tfoot element (given by id).
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/tables">More</a>.
     * @param elementId The HTML element to update (by id)
     * @param row The cells to add to the table
     * @throws MarshallException If the data can not be marshalled
     */
    public void addRow(String elementId, Row row) throws MarshallException
    {
        
    }

    /**
     * Remove all the children of a given node.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/tables">More</a>.
     * @param elementId The HTML element to update (by id)
     * @throws MarshallException If the data can not be marshalled
     */
    public void removeAllRows(String elementId) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append("DWRUtil.removeAllRows(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * Clone a given node.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/TODO">More</a>.
     * @param elementId The HTML element to update (by id)
     * @throws MarshallException If the data can not be marshalled
     */
    public void cloneNode(String elementId) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append("DWRUtil.cloneNode(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * Clone a given node.
     * <p>
     * <a href="http://getahead.ltd.uk/dwr/browser/TODO">More</a>.
     * @param elementId The HTML element to update (by id)
     * @param idPrefix How do we prefix ids in the cloned version of the node tree
     * @param idSuffix How do we suffix ids in the cloned version of the node tree
     * @throws MarshallException If the data can not be marshalled
     */
    public void cloneNode(String elementId, String idPrefix, String idSuffix) throws MarshallException
    {
        OutboundVariable elementIdOv = getServerContext().toJavascript(elementId);

        StringBuffer options = new StringBuffer();
        options.append("{"); //$NON-NLS-1$
        if (idPrefix != null)
        {
            options.append("idPrefix:'").append(idPrefix).append("'");  //$NON-NLS-1$//$NON-NLS-2$
        }
        if (idPrefix != null && idSuffix != null)
        {
            options.append(","); //$NON-NLS-1$
        }
        if (idSuffix != null)
        {
            options.append("idSuffix:'").append(idSuffix).append("'");  //$NON-NLS-1$//$NON-NLS-2$
        }
        options.append("}"); //$NON-NLS-1$

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append("DWRUtil.cloneNode(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(", ") //$NON-NLS-1$
            .append(options)
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }

    /**
     * $(ele).className = "X", that we can call from Java easily
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to set for the element
     */
    public void setClassName(String elementId, String className)
    {
        addScript("DWRUtil.setClassName('" + elementId + "', '" + className + "');"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * $(ele).className += "X", that we can call from Java easily.
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to add to the element
     */
    public void addClassName(String elementId, String className)
    {
        addScript("DWRUtil.addClassName('" + elementId + "', '" + className + "');"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * $(ele).className -= "X", that we can call from Java easily From code originally by Gavin Kistner
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to remove from the element
     */
    public void removeClassName(String elementId, String className)
    {
        addScript("DWRUtil.removeClassName('" + elementId + "', '" + className + "');"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * $(ele).className |= "X", that we can call from Java easily.
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to toggle on/off
     */
    public void toggleClassName(String elementId, String className)
    {
        addScript("DWRUtil.toggleClassName('" + elementId + "', '" + className + "');"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * Sets a CSS style on an element
     * @param elementId The HTML element to update (by id)
     * @param selector The CSS selector to update
     * @param value The new value for the CSS class on the given element
     */
    public void setStyle(String elementId, String selector, String value)
    {
        addScript("$('" + elementId + "').style." + selector + " = '" + value + "';"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
