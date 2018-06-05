package org.directwebremoting.proxy.dwr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * Util is a server-side proxy that allows Java programmers to call client
 * side Javascript from Java.
 * <p>
 * Each Util object is associated with a list of ScriptSessions and the
 * proxy code is creates will be dynamically forwarded to all those browsers.
 * <p>
 * Currently this class contains only the write-only DOM manipulation functions
 * from Util. It is possible that we could add the read methods, however
 * the complexity in the callback and the fact that you are probably not going
 * to need it means that we'll leave it for another day. Specifically,
 * <code>getValue</code>, <code>getValues</code> and <code>getText</code> have
 * been left out as being read functions and <code>useLoadingMessage</code> etc
 * have been left out as not being DOM related.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Jorge Martin Cuervo [darthkorr at gmail dot com]
 * @deprecated Use org.directwebremoting.ui.dwr.Util
 * @see org.directwebremoting.ui.dwr.Util
 */
@Deprecated
public class Util extends ScriptProxy
{
    /**
     * Http thread constructor, that affects no browsers.
     * Calls to {@link Util#addScriptSession(ScriptSession)} or to
     * {@link Util#addScriptSessions(Collection)} will be needed
     */
    public Util()
    {
    }

    /**
     * Http thread constructor that alters a single browser
     * @param scriptSession The browser to alter
     */
    public Util(ScriptSession scriptSession)
    {
        super(scriptSession);
    }

    /**
     * Http thread constructor that alters a number of browsers
     * @param scriptSessions A collection of ScriptSessions that we should act on.
     */
    public Util(Collection<ScriptSession> scriptSessions)
    {
        super(scriptSessions);
    }

    /**
     * Set the value an HTML element to the specified value.
     * @param elementId The HTML element to update (by id)
     * @param value The text to insert into the HTML element
     */
    public void setValue(String elementId, Object value)
    {
        setValue(elementId, value, false);
    }

    /**
     * Set the value an HTML element to the specified value.
     * @param elementId The HTML element to update (by id)
     * @param value The text to insert into the HTML element
     * @param escapeHtml Should we escape HTML characters?
     */
    public void setValue(String elementId, Object value, boolean escapeHtml)
    {
        addFunctionCall("dwr.util.setValue", elementId, value, getEscapeOptions(escapeHtml));
    }

    /**
     * Given a map, call setValue() for all the entries in the map using the
     * entry key as an element id.
     * @param values The map of elementIds to values to alter
     * @param escapeHtml Should we escape HTML characters?
     */
    public void setValues(Map<?, ?> values, boolean escapeHtml)
    {
        addFunctionCall("dwr.util.setValues", values, getEscapeOptions(escapeHtml));
    }

    /**
     * Add options to a list from an array or map.
     * @param elementId The HTML element to update (by id)
     * @param array An array of strings to use as both value and text of options
     */
    public void addOptions(String elementId, String[] array)
    {
        addFunctionCall("dwr.util.addOptions", elementId, array);
    }

    /**
     * Add options to a list from an array or map.
     * @param elementId The HTML element to update (by id)
     * @param array And array of objects from which to create options
     * @param property The object property to use for the option value and text
     */
    public void addOptions(String elementId, Collection<?> array, String property)
    {
        addFunctionCall("dwr.util.addOptions", elementId, array, property);
    }

    /**
     * Add options to a list from an array or map.
     * @param elementId The HTML element to update (by id)
     * @param array And array of objects from which to create options
     * @param valueProperty The object property to use for the option value
     * @param textProperty The object property to use for the option text
     */
    public void addOptions(String elementId, Collection<?> array, String valueProperty, String textProperty)
    {
        addFunctionCall("dwr.util.addOptions", elementId, array, valueProperty, textProperty);
    }

    /**
     * Remove all the options from a select list (specified by id)
     * @param elementId The HTML element to update (by id)
     */
    public void removeAllOptions(String elementId)
    {
        addFunctionCall("dwr.util.removeAllOptions", elementId);
    }

    /**
     * Create rows inside a the table, tbody, thead or tfoot element (given by id).
     * @param elementId The HTML element to update (by id)
     * @param data The cells to add to the table
     * @param options See link above for documentation on the options
     */
    public void addRows(String elementId, String[][] data, String options)
    {
        if (data.length > 0)
        {
            StringBuffer functions = new StringBuffer();
            for (int i = 0; i < data[0].length; i++)
            {
                functions.append("function(data) { return data[")
                         .append(i)
                         .append("]},");
            }

            functions.deleteCharAt(functions.length() - 1);

            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("dwr.util.addRows(")
                  .appendData(elementId)
                  .appendScript(",")
                  .appendData(data)
                  .appendScript(",")
                  .appendScript("[" + functions.toString() + "]")
                  .appendScript(options == null ? "" : ", " + options)
                  .appendScript(");");

            addScript(script);
        }
    }

    /**
     * Create rows inside a the table, tbody, thead or tfoot element (given by id).
     * @param elementId The HTML element to update (by id)
     * @param data The cells to add to the table
     */
    public void addRows(String elementId, String[][] data)
    {
        addRows(elementId, data, null);
    }

    /**
     * Remove all the children of a given node.
     * @param elementId The HTML element to update (by id)
     */
    public void removeAllRows(String elementId)
    {
        addFunctionCall("dwr.util.removeAllRows", elementId);
    }

    /**
     * Clone a given node.
     * @param elementId The HTML element to update (by id)
     */
    public void cloneNode(String elementId)
    {
        addFunctionCall("dwr.util.cloneNode", elementId);
    }

    /**
     * Clone a given node.
     * @param elementId The HTML element to update (by id)
     * @param idPrefix How do we prefix ids in the cloned version of the node tree
     * @param idSuffix How do we suffix ids in the cloned version of the node tree
     */
    public void cloneNode(String elementId, String idPrefix, String idSuffix)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.util.cloneNode(")
              .appendData(elementId)
              .appendScript(", { idPrefix:")
              .appendData(idPrefix)
              .appendScript(", idSuffix:")
              .appendData(idSuffix)
              .appendScript("});");
        addScript(script);
    }

    /**
     * Sets a CSS style on an element
     * @param elementId The HTML element to update (by id)
     */
    public void removeNode(String elementId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.util._temp = dwr.util.byId(")
              .appendData(elementId)
              .appendScript("); ")
              .appendScript("if (dwr.util._temp) { dwr.util._temp.parentNode.removeChild(dwr.util._temp); dwr.util._temp = null; }");
        addScript(script);
    }

    /**
     * $(ele).className = "X", that we can call from Java easily
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to set for the element
     */
    public void setClassName(String elementId, String className)
    {
        addFunctionCall("dwr.util.setClassName", elementId, className);
    }

    /**
     * $(ele).className += "X", that we can call from Java easily.
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to add to the element
     */
    public void addClassName(String elementId, String className)
    {
        addFunctionCall("dwr.util.addClassName", elementId, className);
    }

    /**
     * $(ele).className -= "X", that we can call from Java easily From code originally by Gavin Kistner
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to remove from the element
     */
    public void removeClassName(String elementId, String className)
    {
        addFunctionCall("dwr.util.removeClassName", elementId, className);
    }

    /**
     * $(ele).className |= "X", that we can call from Java easily.
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to toggle on/off
     */
    public void toggleClassName(String elementId, String className)
    {
        addFunctionCall("dwr.util.toggleClassName", elementId, className);
    }

    /**
     * Sets a CSS style on an element
     * @param elementId The HTML element to update (by id)
     * @param selector The CSS selector to update
     * @param value The new value for the CSS class on the given element
     */
    public void setStyle(String elementId, String selector, String value)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.util.byId(")
              .appendData(elementId)
              .appendScript(").style.")
              .appendScript(selector)
              .appendScript("=")
              .appendData(value)
              .appendScript(";");
        addScript(script);
    }

    /**
     * Internal utility to fetch an options object that contains a single
     * setting for "escapeHtml"
     * @param escapeHtml Do we want the client to escape HTML?
     * @return An options object containing the setting.
     */
    private Map<String, Boolean> getEscapeOptions(boolean escapeHtml)
    {
        Map<String, Boolean> options = new HashMap<String, Boolean>();
        options.put("escapeHtml", escapeHtml);
        return options;
    }
}
