package org.directwebremoting.ui.dwr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;

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
 */
public class Util
{
    /**
     * Set the value an HTML element to the specified value.
     * @param elementId The HTML element to update (by id)
     * @param value The text to insert into the HTML element
     */
    public static void setValue(String elementId, Object value)
    {
        setValue(elementId, value, false);
    }

    /**
     * Set the value an HTML element to the specified value.
     * @param elementId The HTML element to update (by id)
     * @param value The text to insert into the HTML element
     * @param escapeHtml Should we escape HTML characters?
     */
    public static void setValue(String elementId, Object value, boolean escapeHtml)
    {
        ScriptSessions.addFunctionCall("dwr.util.setValue", elementId, value, getEscapeOptions(escapeHtml));
    }

    /**
     * Given a map, call setValue() for all the entries in the map using the
     * entry key as an element id.
     * @param values The map of elementIds to values to alter
     * @param escapeHtml Should we escape HTML characters?
     */
    public static void setValues(Map<?, ?> values, boolean escapeHtml)
    {
        ScriptSessions.addFunctionCall("dwr.util.setValues", values, getEscapeOptions(escapeHtml));
    }

    /**
     * Add options to a list from an array or map.
     * @param elementId The HTML element to update (by id)
     * @param array An array of strings to use as both value and text of options
     */
    public static void addOptions(String elementId, String[] array)
    {
        ScriptSessions.addFunctionCall("dwr.util.addOptions", elementId, array);
    }

    /**
     * Add options to a list from an array or map.
     * @param elementId The HTML element to update (by id)
     * @param array And array of objects from which to create options
     * @param property The object property to use for the option value and text
     */
    public static void addOptions(String elementId, Collection<?> array, String property)
    {
        ScriptSessions.addFunctionCall("dwr.util.addOptions", elementId, array, property);
    }

    /**
     * Add options to a list from an array or map.
     * @param elementId The HTML element to update (by id)
     * @param array And array of objects from which to create options
     * @param valueProperty The object property to use for the option value
     * @param textProperty The object property to use for the option text
     */
    public static void addOptions(String elementId, Collection<?> array, String valueProperty, String textProperty)
    {
        ScriptSessions.addFunctionCall("dwr.util.addOptions", elementId, array, valueProperty, textProperty);
    }

    /**
     * Remove all the options from a select list (specified by id)
     * @param elementId The HTML element to update (by id)
     */
    public static void removeAllOptions(String elementId)
    {
        ScriptSessions.addFunctionCall("dwr.util.removeAllOptions", elementId);
    }

    /**
     * Create rows inside a the table, tbody, thead or tfoot element (given by id).
     * @param elementId The HTML element to update (by id)
     * @param data The cells to add to the table
     * @param options See link above for documentation on the options
     */
    @SuppressWarnings("unused")
    public static void addRows(String elementId, String[][] data, String options)
    {
        if (null == data || data.length == 0 || null == data[0]) {
            return;
        }
        int rowCount = data.length;
        int colCount = data[0].length;
        buildAddRowsScript(elementId, data, options, rowCount, colCount);
    }

    /**
     * Create rows inside a the table, tbody, thead or tfoot element (given by id).
     * @param elementId The HTML element to update (by id)
     * @param data The cells to add to the table
     */
    public static void addRows(String elementId, String[][] data)
    {
        addRows(elementId, data, null);
    }

    /**
     * Create rows inside a the table, tbody, thead or tfoot element (given by id).
     * @param elementId The HTML element to update (by id)
     * @param data The cells to add to the table
     * @param options See link above for documentation on the options
     */
    @SuppressWarnings("unused")
    public static void addRows(String elementId, Collection<Collection<String>> data, String options)
    {
        if (null == data || data.size() == 0 || !data.iterator().hasNext()) {
            return;
        }
        int rowCount = data.size();
        int colCount = data.iterator().next().size();
        buildAddRowsScript(elementId, data, options, rowCount, colCount);
    }

    /**
     * Create rows inside a the table, tbody, thead or tfoot element (given by id).
     * @param elementId The HTML element to update (by id)
     * @param data The cells to add to the table
     */
    public static void addRows(String elementId, Collection<Collection<String>> data)
    {
        addRows(elementId, data, null);
    }

    /**
     * Create rows inside a the table, tbody, thead or tfoot element (given by id).
     * @param elementId The HTML element to update (by id)
     * @param data The cells to add to the table
     * @param options See link above for documentation on the options
     * @param rowCount The number of rows that are being added
     * @param colCount The number of columns that are being added
     */
    private static void buildAddRowsScript(String elementId, Object data, String options, int rowCount, int colCount) {
        StringBuffer functions = new StringBuffer();
        for (int i=0; i < colCount; i++)
        {
            functions.append("function(data) { return data[").append(i).append("]},");
        }
        if (rowCount > 0)
        {
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
            ScriptSessions.addScript(script);
        }
    }

    /**
     * Remove all the children of a given node.
     * @param elementId The HTML element to update (by id)
     */
    public static void removeAllRows(String elementId)
    {
        ScriptSessions.addFunctionCall("dwr.util.removeAllRows", elementId);
    }

    /**
     * Clone a given node.
     * @param elementId The HTML element to update (by id)
     */
    public static void cloneNode(String elementId)
    {
        ScriptSessions.addFunctionCall("dwr.util.cloneNode", elementId);
    }

    /**
     * Clone a given node.
     * @param elementId The HTML element to update (by id)
     * @param idPrefix How do we prefix ids in the cloned version of the node tree
     * @param idSuffix How do we suffix ids in the cloned version of the node tree
     */
    public static void cloneNode(String elementId, String idPrefix, String idSuffix)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.util.cloneNode(")
              .appendData(elementId)
              .appendScript(", { idPrefix:")
              .appendData(idPrefix)
              .appendScript(", idSuffix:")
              .appendData(idSuffix)
              .appendScript("});");
        ScriptSessions.addScript(script);
    }

    /**
     * Sets a CSS style on an element
     * @param elementId The HTML element to update (by id)
     */
    public static void removeNode(String elementId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.util._temp = dwr.util.byId(")
              .appendData(elementId)
              .appendScript("); ")
              .appendScript("if (dwr.util._temp) { dwr.util._temp.parentNode.removeChild(dwr.util._temp); dwr.util._temp = null; }");
        ScriptSessions.addScript(script);
    }

    /**
     * $(ele).className = "X", that we can call from Java easily
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to set for the element
     */
    public static void setClassName(String elementId, String className)
    {
        ScriptSessions.addFunctionCall("dwr.util.setClassName", elementId, className);
    }

    /**
     * $(ele).className += "X", that we can call from Java easily.
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to add to the element
     */
    public static void addClassName(String elementId, String className)
    {
        ScriptSessions.addFunctionCall("dwr.util.addClassName", elementId, className);
    }

    /**
     * $(ele).className -= "X", that we can call from Java easily From code originally by Gavin Kistner
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to remove from the element
     */
    public static void removeClassName(String elementId, String className)
    {
        ScriptSessions.addFunctionCall("dwr.util.removeClassName", elementId, className);
    }

    /**
     * $(ele).className |= "X", that we can call from Java easily.
     * @param elementId The HTML element to update (by id)
     * @param className The CSS class to toggle on/off
     */
    public static void toggleClassName(String elementId, String className)
    {
        ScriptSessions.addFunctionCall("dwr.util.toggleClassName", elementId, className);
    }

    /**
     * Sets a CSS style on an element
     * @param elementId The HTML element to update (by id)
     * @param selector The CSS selector to update
     * @param value The new value for the CSS class on the given element
     */
    public static void setStyle(String elementId, String selector, String value)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.util.byId(")
              .appendData(elementId)
              .appendScript(").style.")
              .appendScript(selector)
              .appendScript("=")
              .appendData(value)
              .appendScript(";");
        ScriptSessions.addScript(script);
    }

    /**
     * Internal utility to fetch an options object that contains a single
     * setting for "escapeHtml"
     * @param escapeHtml Do we want the client to escape HTML?
     * @return An options object containing the setting.
     */
    private static Map<String, Boolean> getEscapeOptions(boolean escapeHtml)
    {
        Map<String, Boolean> options = new HashMap<String, Boolean>();
        options.put("escapeHtml", escapeHtml);
        return options;
    }
}
