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

import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.AbstractProxy;

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
public class DwrUtil extends AbstractProxy
{
    /**
     * Build a DwrUtil that acts on a single ScriptSession.
     * @param scriptSession The page to affect
     */
    public DwrUtil(ScriptSession scriptSession)
    {
        addScriptSession(scriptSession);
    }

    /**
     * Build a DwrUtil that acts on a number of ScriptSessions
     * @param dests A collection of ScriptSessions that we should act on.
     */
    public DwrUtil(Collection dests)
    {
        addScriptSessions(dests);
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
        OutboundVariable elementIdOv = getWebContext().toJavascript(elementId);
        OutboundVariable valueOv = getWebContext().toJavascript(value);
        String options = escapeHtml ? "{escapeHtml:true}" : "null"; //$NON-NLS-1$ //$NON-NLS-2$

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append(valueOv.getInitCode())
            .append("DWRUtil.setValue(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(',')
            .append(valueOv.getAssignCode())
            .append(',')
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
        OutboundVariable valuesOv = getWebContext().toJavascript(values);
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
        OutboundVariable elementIdOv = getWebContext().toJavascript(elementId);
        OutboundVariable arrayOv = getWebContext().toJavascript(array);

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
     * @param valueProperty The object property to use for the option value
     * @param textProperty The object property to use for the option text
     * @throws MarshallException If the data can not be marshalled
     */
    public void addOptions(String elementId, Collection array, String valueProperty, String textProperty) throws MarshallException
    {
        OutboundVariable elementIdOv = getWebContext().toJavascript(elementId);
        OutboundVariable arrayOv = getWebContext().toJavascript(array);
        OutboundVariable valuePropertyOv = getWebContext().toJavascript(valueProperty);
        OutboundVariable textPropertyOv = getWebContext().toJavascript(textProperty);

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
        OutboundVariable elementIdOv = getWebContext().toJavascript(elementId);

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
     * @param array 
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
        OutboundVariable elementIdOv = getWebContext().toJavascript(elementId);

        StringBuffer script = new StringBuffer();
        script.append(elementIdOv.getInitCode())
            .append("DWRUtil.removeAllRows(") //$NON-NLS-1$
            .append(elementIdOv.getAssignCode())
            .append(");"); //$NON-NLS-1$

        addScript(script.toString());
    }
}
