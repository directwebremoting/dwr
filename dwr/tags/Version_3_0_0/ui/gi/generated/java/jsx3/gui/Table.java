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
package jsx3.gui;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * A lightweight control that displays CDF data in an HTML table.  Supports both single- and multi-selection
modes. Data can be sorted by clicking on column labels.  Output and output formatting can be customized using
a combination of XSLT, inline CSS properties, or named CSS rules.  The columns
for this control are defined within the object model and are not defined
in the DOM as child objects.

The Table class by default supports the following CDF attributes:


        jsxid


        jsxselected


        jsxstyle


        jsxclass


        jsximg


        jsxtip


        jsxunselectable


        jsxexecute


This class publishes the following model events:


        EXECUTE Ð

        MENU Ð

        CHANGE Ð

        SPYGLASS Ð
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Table extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Table(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     */
    public Table(String strName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Table", strName);
        setInitScript(script);
    }


    /**
     * 
     */
    public static final String DEFAULTXSLURL = null;

    /**
     * text (default)
     */
    public static final String TYPE_TEXT = "text";

    /**
     * number
     */
    public static final String TYPE_NUMBER = "number";

    /**
     * jsx:///images/table/select.gif
     */
    public static final String SELECTION_BG = "jsx:///images/table/select.gif";

    /**
     * Enum value for the multiSelect property indicating an unselectable table.
     */
    public static final int SELECTION_UNSELECTABLE = 0;

    /**
     * Enum value for the multiSelect property indicating a multi-select table.
     */
    public static final int SELECTION_ROW = 1;

    /**
     * Enum value for the multiSelect property indicating a single-select table.
     */
    public static final int SELECTION_MULTI_ROW = 2;

    /**
     * ascending
     */
    public static final String SORT_ASCENDING = "ascending";

    /**
     * descending
     */
    public static final String SORT_DESCENDING = "descending";

    /**
     * jsx:///images/table/sort_desc.gif (default)
     */
    public static final String SORT_DESCENDING_IMG = null;

    /**
     * jsx:///images/table/sort_asc.gif (default)
     */
    public static final String SORT_ASCENDING_IMG = null;

    /**
     * 20
     */
    public static final int DEFAULT_HEADER_HEIGHT = 20;


    /**
     * validates the Table; if the Table is set to 'required', a selection must be made to pass validation. Otherwise, a Table will always pass validation
     * @param callback one of: jsx3.gui.Form.STATEINVALID or jsx3.gui.Form.STATEVALID
     */

    public void doValidate(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "doValidate");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns an array of selected values (or empty array) if the selection model is Table.SELECTION_MULTI_ROW. Returns a string (or null)
for the other selection models
     */

    public void getValue(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getValue");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the value of this table. Deselects all existing selections. Scrolls the first record into view.
     * @param strId jsxid attribute for the CDF record(s) to select
     * @return this object.
     */
    public jsx3.gui.Table setValue(String strId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValue", strId);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Sets the value of this table. Deselects all existing selections. Scrolls the first record into view.
     * @param strId jsxid attribute for the CDF record(s) to select
     * @return this object.
     */
    public jsx3.gui.Table setValue(Object[] strId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValue", strId);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the on-screen cell that represents the intersection of the row identified
by strCdfId and the first cell mapped to the named CDF attribute, strAttName.
     * @param strCdfId jsxid property for CDF record
     * @param strAttName attribute name on the CDF record. For example, <code>jsxtext</code>
     */

    public void getContentElement(String strCdfId, String strAttName, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getContentElement", strCdfId, strAttName);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Applies focus to the on-screen row indentified by the CDF record id that generated it
     * @param strCdfId jsxid property for the corresponding CDF record
     */
    public void focusRowById(String strCdfId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "focusRowById", strCdfId);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the selection model. If no selection type is specified, the instance will employ single row selection (SELECTION_ROW)
     * @param strDefault The default value to use if null
     */

    public void getSelectionModel(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSelectionModel", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the selection model
     * @param intType one of Table: .SELECTION_UNSELECTABLE, .SELECTION_ROW, .SELECTION_MULTI_ROW
     */
    public void setSelectionModel(int intType)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSelectionModel", intType);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS string to apply to a Row/Cell when it has focus
     * @param strDefault The default value to use if null (Table.SELECTION_BG)
     */

    public void getSelectionBG(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSelectionBG", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the URL for the image to use (as the repeating background image) to denote selection.
     * @param strURL
     */
    public void setSelectionBG(String strURL)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSelectionBG", strURL);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the collection of selected records.
     */

    @SuppressWarnings("unchecked")
    public void getSelectedNodes(org.directwebremoting.ui.Callback<java.util.List> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSelectedNodes");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, java.util.List.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the jsxid(s) for the selected record(s). Equivalent to this.getValue() except that the return value is always an Array.
     * @param callback JavaScript array of stings
     */

    public void getSelectedIds(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSelectedIds");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Selects a CDF record of this list. The item will be highlighted in the view and the CDF data will be updated
accordingly. If this list is a multi-select list then this selection will be added to any previous selection.
     * @param strRecordId the jsxid of the record to select.
     */
    public void selectRecord(String strRecordId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "selectRecord", strRecordId);
        ScriptSessions.addScript(script);
    }

    /**
     * Deselects a CDF record within the Table. Both the view and the data model (CDF) will be updated
     * @param strRecordId the jsxid of the record to deselect.
     */
    public void deselectRecord(String strRecordId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "deselectRecord", strRecordId);
        ScriptSessions.addScript(script);
    }

    /**
     * Deselects all selected CDF records.
     */
    public void deselectAllRecords()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "deselectAllRecords");
        ScriptSessions.addScript(script);
    }

    /**
     * Sorts according to the current sort path. If no sort direction is specified, the sort direction will be toggled.
     * @param intSortDir <code>jsx3.gui.Table.SORT_ASCENDING</code> or <code>jsx3.gui.Table.SORT_DESCENDING</code>.
     */
    public void doSort(String intSortDir)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "doSort", intSortDir);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the name of the CDF attribute to sort on. If no value is set an empty string is returned by default.
     */

    public void getSortPath(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSortPath");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the name of the CDF attribute to sort on. The records in the data source of this table are sorted
on this attribute before being painted to screen.
     * @param strAttr
     */
    public void setSortPath(String strAttr)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSortPath", strAttr);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the data type to be used for sorting this list.
     * @param callback <code>jsx3.gui.Table.TYPE_TEXT</code> or <code>jsx3.gui.Table.TYPE_NUMBER</code>
     */

    public void getSortType(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSortType");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the data type for the list.
     * @param DATATYPE data type for this column's data. Valid types include: jsx3.gui.Table.TYPE_TEXT and jsx3.gui.Table.TYPE_NUMBER
     */
    public void setSortType(String DATATYPE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSortType", DATATYPE);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the direction (jsx3.gui.Table.SORT_ASCENDING or jsx3.gui.Table.SORT_DESCENDING) for the sorted column; if no direction specified, ascending is returned
     * @param callback one of: jsx3.gui.Table.SORT_ASCENDING or jsx3.gui.Table.SORT_DESCENDING
     */

    public void getSortDirection(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSortDirection");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the direction (ascending or descending) for the sorted column.
     * @param intSortDir one of: jsx3.gui.Table.SORT_ASCENDING or jsx3.gui.Table.SORT_DESCENDING
     */
    public void setSortDirection(String intSortDir)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSortDirection", intSortDir);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether the table is sortable. If null or jsx3.Boolean.TRUE, the instance is sortable.
     */

    public void getCanSort(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCanSort");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether the table is sortable.
     * @param SORT one of <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */
    public void setCanSort(int SORT)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCanSort", SORT);
        ScriptSessions.addScript(script);
    }

    /**
     * This method implements redraw support by repainting the entire control.
     * @param strRecordId
     * @param ACTION
     * @return this object
     */

    public jsx3.gui.Table redrawRecord(String strRecordId, java.lang.Object ACTION)
    {
        String extension = "redrawRecord(\"" + strRecordId + "\", \"" + ACTION + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Table> ctor = jsx3.gui.Table.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Table.class.getName());
        }
    }


    /**
     * Paints only the header row.  Call for quick repainting of the header row and not the data rows.
     */
    public void repaintHead()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "repaintHead");
        ScriptSessions.addScript(script);
    }

    /**
     * Paints only the data rows.  Call for quick repainting of the data rows when only the source data
has changed. Does not recalculate and reprofile the box profile and resulting XSLT. Retains scroll position when possible.
     */
    public void repaintData()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "repaintData");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS style for the HTML row containing the column headers.
     * @param strDefault
     */

    public void getHeaderStyle(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHeaderStyle", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS style properties for the HTML row containing the column headers. Multiple properties are supported.
For example: background-image:url(JSXAPPS/myproject/images/bg.gif);font-family:Arial;.
The following CSS properties (those affecting layout and position) are not allowed: width, height,
left, top, position, overflow, border, padding, margin.
     * @param strCSS
     */
    public void setHeaderStyle(String strCSS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHeaderStyle", strCSS);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS rule for the HTML row containing the column headers.
     * @param strDefault
     */

    public void getHeaderClass(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHeaderClass", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS rule for the HTML row containing the column headers.  Multiple rules are supported.
For example: boldText titleText.
The following CSS properties (those affecting layout and position) are not allowed for the rule: width, height,
left, top, position, overflow, border, padding, margin.
     * @param strRuleName
     */
    public void setHeaderClass(String strRuleName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHeaderClass", strRuleName);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS properties for the HTML row elements(s) containing the table data.
     */

    public void getRowStyle(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRowStyle");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS properties for the HTML row element(s) containing the table data. Every row will
apply the properties defined by this value, unless an alternate row style is used, in which case, the properties are alternated
between this value and the value applied by setAlternateRowStyle.  Multiple properties are supported.
For example: background-color:white;font-family:Arial;.
     * @param strCSS
     */
    public void setRowStyle(String strCSS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRowStyle", strCSS);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS properties for the HTML row element(s) containing the alternating table data rows.
     * @param strDefault
     */

    public void getAlternateRowStyle(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAlternateRowStyle", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS properties for the HTML row element(s) containing the alternating table data rows. Multiple properties are supported.
For example: background-color:red;font-family:Arial;.
     * @param strCSS
     */
    public void setAlternateRowStyle(String strCSS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAlternateRowStyle", strCSS);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS properties that will be inlined on every HTML cell in the body of the table.
     */

    public void getCellStyle(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellStyle");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS properties that will be inlined on every HTML cell in the body of the table. Multiple properties are supported.
For example: text-align:right;background-color:#eeeeee;border-bottom:solid 1px #aeaeae;.
     * @param strCSS
     */
    public void setCellStyle(String strCSS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellStyle", strCSS);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS rule for the HTML row element(s) containing the table data.
     */

    public void getRowClass(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRowClass");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS rule for the HTML row element(s) containing the table data. Every row will
apply the rule defined by this value, unless an alternate row rule is used, in which case, the rule (classname) is alternated
between this value and the value applied by setAlternateRowClass.  Multiple rules are supported.
For example: bodyText normalText.
     * @param strRuleName
     */
    public void setRowClass(String strRuleName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRowClass", strRuleName);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS rule for the HTML row element(s) containing the alternating table data rows.
     * @param strDefault
     */

    public void getAlternateRowClass(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAlternateRowClass", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS rule for the HTML row element(s) containing the alternating table data rows. Multiple rules are supported.
For example: bodyText, normalText.
     * @param strRuleName
     */
    public void setAlternateRowClass(String strRuleName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAlternateRowClass", strRuleName);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS rule that will be applied to every HTML cell in the body of the table.
     */

    public void getCellClass(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellClass");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS rule that will be applied to every HTML cell in the body of the table.
Multiple rules are supported.  For example: boldText titleText.
     * @param strRuleName
     */
    public void setCellClass(String strRuleName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellClass", strRuleName);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether or not the table's data cells support text-wrapping and expand vertically to display their wrapped content. If this
property is not set, the cell content will not wrap.
     * @param strDefault
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getWrap(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getWrap", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether or not the table's data cells support text-wrapping and expand vertically to display their wrapped content.
     * @param WRAP <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */
    public void setWrap(int WRAP)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWrap", WRAP);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the text/HTML to display on-screen when the xml/xsl transformation for this object results in a null or empty result set
     * @param callback text/HTML
     */

    public void getNoDataMessage(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getNoDataMessage");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns XSLT for the Table, prioritizing the acquisition in the following order: 1) check cache; 2) check jsxxsl; 3) check jsxxslurl; 4) use default
     * @return jsx3.xml.Document instance containing valid XSL stylesheet
     */

    public jsx3.xml.CdfDocument getXSL()
    {
        String extension = "getXSL().";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Returns XSLT for the Table, prioritizing the acquisition in the following order: 1) check cache; 2) check jsxxsl; 3) check jsxxslurl; 4) use default
     * @param returnType The expected return type
     * @return jsx3.xml.Document instance containing valid XSL stylesheet
     */

    public <T> T getXSL(Class<T> returnType)
    {
        String extension = "getXSL().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Gets the user-defined XSL template (xsl:template) that will override the defualt template defined by Table.DEFAULT_CELL_VALUE_TEMPLATE.
     * @param strDefault xsl:template
     */

    public void getValueTemplate(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getValueTemplate", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the user-defined XSL template that will override the defualt template defined by Table.DEFAULT_CELL_VALUE_TEMPLATE.
The template must resolve to a valid XSL Template when parsed.  The template should match on a record (match="record").  The template
will be passed a single XSL param (xsl:param) named attname.
     * @param TEMPLATE valid xsl:template
     */
    public void setValueTemplate(String TEMPLATE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValueTemplate", TEMPLATE);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns a clone of the CDF document used internally to define the Columns (text, order, mapped attributes, etc).
The order of the records in this document reflects the order of the columns in the Table.  If the column profile document defined
by getColumnProfile is not a valid XML document, an empty CDF Document will be returned instead.
Note that if you make changes to the Document returned by this method, those
changes will only be reflected by calling  setColumnProfile (to update the model),
followed by a call to repaint (to update the view).
     */

    public jsx3.xml.CdfDocument getColumnProfileDocument()
    {
        String extension = "getColumnProfileDocument().";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Returns a clone of the CDF document used internally to define the Columns (text, order, mapped attributes, etc).
The order of the records in this document reflects the order of the columns in the Table.  If the column profile document defined
by getColumnProfile is not a valid XML document, an empty CDF Document will be returned instead.
Note that if you make changes to the Document returned by this method, those
changes will only be reflected by calling  setColumnProfile (to update the model),
followed by a call to repaint (to update the view).
     * @param returnType The expected return type
     */

    public <T> T getColumnProfileDocument(Class<T> returnType)
    {
        String extension = "getColumnProfileDocument().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns the string of XML in CDF format representing the Column Profile Document.
     */

    public void getColumnProfile(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getColumnProfile");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets a string of XML (in CDF format) or an actual jsx3.xml.CDF.Document instance representing the Column Profile Document.
Each record in this document defines the profile for a column in the Table.  The following attributes are supported on each record:

  jsxid: The unique ID for the record (REQUIRED).
  jsxtext: HTML or text content to use as the column label.
  jsxwidth: The width of the column (pixel units are implied). For example: 300, or 25%.
  jsxpath: The name of the attribute to which this column maps (REQUIRED).
  jsxpathtype: The data type for the attribute. One of: text (default) or number.


      For example:

  <data jsxid="jsxroot">
    <record jsxid="a1" jsxtext="&lt;b&gt;Column 1&lt;/b&gt;" jsxpath="jsxtext"/>
    <record jsxid="a2" jsxtext="Column 2" jsxwidth="100" jsxpath="value" jsxpathtype="number"/>
  </data>
     * @param objCDF
     */
    public void setColumnProfile(jsx3.xml.CdfDocument objCDF)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColumnProfile", objCDF);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets a string of XML (in CDF format) or an actual jsx3.xml.CDF.Document instance representing the Column Profile Document.
Each record in this document defines the profile for a column in the Table.  The following attributes are supported on each record:

  jsxid: The unique ID for the record (REQUIRED).
  jsxtext: HTML or text content to use as the column label.
  jsxwidth: The width of the column (pixel units are implied). For example: 300, or 25%.
  jsxpath: The name of the attribute to which this column maps (REQUIRED).
  jsxpathtype: The data type for the attribute. One of: text (default) or number.


      For example:

  <data jsxid="jsxroot">
    <record jsxid="a1" jsxtext="&lt;b&gt;Column 1&lt;/b&gt;" jsxpath="jsxtext"/>
    <record jsxid="a2" jsxtext="Column 2" jsxwidth="100" jsxpath="value" jsxpathtype="number"/>
  </data>
     * @param objCDF
     */
    public void setColumnProfile(String objCDF)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setColumnProfile", objCDF);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the jsxid of the CDF record that will serve as the origin when rendering the data on-screen. If not set, the
id, jsxroot, (which is the id for the root node, <data>) will be used.
     * @param strDefault The default value to use if null
     */

    public void getRenderingContext(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRenderingContext", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the jsxid of the CDF record that will serve as the origin when rendering the data on-screen.
     * @param strJsxId jsxid property for the CDF record to use as the contextual root when rendering data on-screen.
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
     */
    public void setRenderingContext(String strJsxId, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRenderingContext", strJsxId, bSuppressRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the height of the header row in pixels. If this value is not set (null), the list will render with
the default value of jsx3.gui.Table.DEFAULT_HEADER_HEIGHT.
     * @param strDefault The default value to use if null
     */

    public void getHeaderHeight(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHeaderHeight", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the height of the header row in pixels. Set to zero (0) to hide the header row and only render the body rows.
     * @param intHeight
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the browser-specific box model (such as height) are repainted
immediately to keep the box model abstraction in sync with the native view. However, the repaint can be
suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     */
    public void setHeaderHeight(int intHeight, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHeaderHeight", intHeight, bSuppressRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Binds the given key sequence to a callback function. Any object that has a key binding (specified with
setKeyBinding()) will call this method when painted to register the key sequence with an appropriate
ancestor of this form control. Any key down event that bubbles up to the ancestor without being intercepted
and matches the given key sequence will invoke the given callback function.

As of 3.2: The hot key will be registered with the first ancestor found that is either a
jsx3.gui.Window, a jsx3.gui.Dialog, or the root block of a jsx3.app.Server.
     * @param fctCallback JavaScript function to execute when the given sequence is keyed by the user.
     * @param strKeys a plus-delimited ('+') key sequence such as <code>ctrl+s</code> or
  <code>ctrl+shift+alt+h</code> or <code>shift+a</code>, etc. Any combination of shift, ctrl, and alt are
  supported, including none. Also supported as the final token are <code>enter</code>, <code>esc</code>,
  <code>tab</code>, <code>del</code>, and <code>space</code>. To specify the final token as a key code, the
  last token can be the key code contained in brackets, <code>[13]</code>.
     * @return the registered hot key.
     */

    public jsx3.gui.HotKey doKeyBinding(org.directwebremoting.ui.CodeBlock fctCallback, String strKeys)
    {
        String extension = "doKeyBinding(\"" + fctCallback + "\", \"" + strKeys + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.HotKey> ctor = jsx3.gui.HotKey.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.HotKey.class.getName());
        }
    }


    /**
     * Resets the validation state of this control.
     * @return this object.
     */

    public jsx3.gui.Form doReset()
    {
        String extension = "doReset().";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Resets the validation state of this control.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T doReset(Class<T> returnType)
    {
        String extension = "doReset().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns the background color of this control when it is disabled.
     * @param callback valid CSS property value, (i.e., red, #ff0000)
     */

    public void getDisabledBackgroundColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDisabledBackgroundColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the font color to use when this control is disabled.
     * @param callback valid CSS property value, (i.e., red, #ff0000)
     */

    public void getDisabledColor(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDisabledColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the state for the form field control. If no enabled state is set, this method returns
STATEENABLED.
     * @param callback <code>STATEDISABLED</code> or <code>STATEENABLED</code>.
     */

    public void getEnabled(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getEnabled");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the key binding that when keyed will fire the execute event for this control.
     * @param callback plus-delimited (e.g.,'+') key sequence such as ctrl+s or ctrl+shift+alt+h or shift+a, etc
     */

    public void getKeyBinding(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getKeyBinding");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether or not this control is required. If the required property has never been set, this method returns
OPTIONAL.
     * @param callback <code>REQUIRED</code> or <code>OPTIONAL</code>.
     */

    public void getRequired(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRequired");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the validation state of this control. If the validationState property has never been set, this method returns
STATEVALID.
     * @param callback <code>STATEINVALID</code> or <code>STATEVALID</code>.
     */

    public void getValidationState(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getValidationState");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the background color of this form control when it is disabled.
     * @param strColor valid CSS property value, (i.e., red, #ff0000)
     * @return this object.
     */

    public jsx3.gui.Form setDisabledBackgroundColor(String strColor)
    {
        String extension = "setDisabledBackgroundColor(\"" + strColor + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets the background color of this form control when it is disabled.
     * @param strColor valid CSS property value, (i.e., red, #ff0000)
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setDisabledBackgroundColor(String strColor, Class<T> returnType)
    {
        String extension = "setDisabledBackgroundColor(\"" + strColor + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets the font color to use when this control is disabled.
     * @param strColor valid CSS property value, (i.e., red, #ff0000)
     * @return this object.
     */

    public jsx3.gui.Form setDisabledColor(String strColor)
    {
        String extension = "setDisabledColor(\"" + strColor + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets the font color to use when this control is disabled.
     * @param strColor valid CSS property value, (i.e., red, #ff0000)
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setDisabledColor(String strColor, Class<T> returnType)
    {
        String extension = "setDisabledColor(\"" + strColor + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets whether this control is enabled. Disabled controls do not respond to user interaction.
     * @param intEnabled <code>STATEDISABLED</code> or <code>STATEENABLED</code>. <code>null</code> is
   equivalent to <code>STATEENABLED</code>.
     * @param bRepaint if <code>true</code> this control is immediately repainted to reflect the new setting.
     */
    public void setEnabled(int intEnabled, boolean bRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setEnabled", intEnabled, bRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the key binding that when keyed will fire the bound execute (jsx3.gui.Interactive.EXECUTE)
event for this control.
     * @param strSequence plus-delimited (e.g.,'+') key sequence such as ctrl+s or ctrl+shift+alt+h or shift+a, etc
     * @return this object.
     */

    public jsx3.gui.Form setKeyBinding(String strSequence)
    {
        String extension = "setKeyBinding(\"" + strSequence + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets the key binding that when keyed will fire the bound execute (jsx3.gui.Interactive.EXECUTE)
event for this control.
     * @param strSequence plus-delimited (e.g.,'+') key sequence such as ctrl+s or ctrl+shift+alt+h or shift+a, etc
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setKeyBinding(String strSequence, Class<T> returnType)
    {
        String extension = "setKeyBinding(\"" + strSequence + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets whether or not this control is required.
     * @param required {int} <code>REQUIRED</code> or <code>OPTIONAL</code>.
     * @return this object.
     */

    public jsx3.gui.Form setRequired(int required)
    {
        String extension = "setRequired(\"" + required + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets whether or not this control is required.
     * @param required {int} <code>REQUIRED</code> or <code>OPTIONAL</code>.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setRequired(int required, Class<T> returnType)
    {
        String extension = "setRequired(\"" + required + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets the validation state of this control. The validation state of a control is not serialized.
     * @param intState <code>STATEINVALID</code> or <code>STATEVALID</code>.
     * @return this object.
     */

    public jsx3.gui.Form setValidationState(int intState)
    {
        String extension = "setValidationState(\"" + intState + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Form> ctor = jsx3.gui.Form.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Form.class.getName());
        }
    }

    /**
     * Sets the validation state of this control. The validation state of a control is not serialized.
     * @param intState <code>STATEINVALID</code> or <code>STATEVALID</code>.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setValidationState(int intState, Class<T> returnType)
    {
        String extension = "setValidationState(\"" + intState + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Resets the XML source document stored in the server cache under the XML ID of this object to an empty CDF
document.
     */
    public void clearXmlData()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "clearXmlData");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this object removes its XML and XSL source documents from the cache of its server when it
is destroyed.
     * @param callback <code>CLEANUPRESOURCES</code> or <code>SHARERESOURCES</code>.
     */

    public void getShareResources(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getShareResources");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XML source document of this object. The XML document is determined by the following steps:

  If an XML document exists in the server cache under an ID equal to the XML ID of this object, that
    document is returned.
  If the XML string of this object is not empty, a new document is created by parsing this string.
  If the XML URL of this object is not empty, a new document is created by parsing the file at the location
    specified by the URL resolved against the server owning this object.
  Otherwise, an empty CDF document is returned.

If a new document is created for this object (any of the steps listed above except for the first one), the
following actions are also taken:

  If creating the document resulted in an error (XML parsing error, file not found error, etc) the offending
    document is returned immediately.
  Otherwise, setSourceXML is called on this object, passing in the created document.
     */

    public jsx3.xml.CdfDocument getXML()
    {
        String extension = "getXML().";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Returns the XML source document of this object. The XML document is determined by the following steps:

  If an XML document exists in the server cache under an ID equal to the XML ID of this object, that
    document is returned.
  If the XML string of this object is not empty, a new document is created by parsing this string.
  If the XML URL of this object is not empty, a new document is created by parsing the file at the location
    specified by the URL resolved against the server owning this object.
  Otherwise, an empty CDF document is returned.

If a new document is created for this object (any of the steps listed above except for the first one), the
following actions are also taken:

  If creating the document resulted in an error (XML parsing error, file not found error, etc) the offending
    document is returned immediately.
  Otherwise, setSourceXML is called on this object, passing in the created document.
     * @param returnType The expected return type
     */

    public <T> T getXML(Class<T> returnType)
    {
        String extension = "getXML().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns the XML ID of this object.
     * @param callback the XML ID.
     */

    public void getXMLId(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXMLId");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XML string of this object.
     */

    public void getXMLString(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXMLString");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the list of XML transformers of this object.
     */

    public void getXMLTransformers(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXMLTransformers");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XML URL of this object.
     */

    public void getXMLURL(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXMLURL");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the XSL ID of this object.
     */

    public void getXSLId(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXSLId");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns a map containing all the parameters to pass to the XSL stylesheet during transformation.
     */

    public jsx3.lang.Object getXSLParams()
    {
        String extension = "getXSLParams().";
        try
        {
            java.lang.reflect.Constructor<jsx3.lang.Object> ctor = jsx3.lang.Object.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.lang.Object.class.getName());
        }
    }

    /**
     * Returns a map containing all the parameters to pass to the XSL stylesheet during transformation.
     * @param returnType The expected return type
     */

    public <T> T getXSLParams(Class<T> returnType)
    {
        String extension = "getXSLParams().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns whether the XML data source of this object is loaded asynchronously.
     * @param callback <code>0</code> or <code>1</code>.
     */

    public void getXmlAsync(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXmlAsync");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether this object is bound to the XML document stored in the data cache.
     * @param callback <code>0</code> or <code>1</code>.
     */

    public void getXmlBind(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getXmlBind");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * This method is called in two situations:

  When the datasource of this object finishes loading (success, error, or timeout), if the
      xmlAsync property of this object is true, its datasource is specified as an
       XML URL, and the first time doTransform() was called the datasource was still loading.
  Any time the value stored in the server XML cache under the key equal to the XML Id of this object
      changes, if the xmlBind property of this object is true.

Any methods overriding this method should begin with a call to jsxsupermix().
     * @param objEvent the event published by the cache.
     */
    public void onXmlBinding(jsx3.lang.Object objEvent)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onXmlBinding", objEvent);
        ScriptSessions.addScript(script);
    }

    /**
     * Removes a parameter from the list of parameters to pass to the XSL stylesheet during transformation.
     * @param strName the name of the XSL parameter to remove.
     * @return this object.
     */

    public jsx3.xml.Cacheable removeXSLParam(String strName)
    {
        String extension = "removeXSLParam(\"" + strName + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Removes a parameter from the list of parameters to pass to the XSL stylesheet during transformation.
     * @param strName the name of the XSL parameter to remove.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T removeXSLParam(String strName, Class<T> returnType)
    {
        String extension = "removeXSLParam(\"" + strName + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Removes all parameters from the list of parameters to pass to the XSL stylesheet during transformation.
     * @return this object.
     */

    public jsx3.xml.Cacheable removeXSLParams()
    {
        String extension = "removeXSLParams().";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Removes all parameters from the list of parameters to pass to the XSL stylesheet during transformation.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T removeXSLParams(Class<T> returnType)
    {
        String extension = "removeXSLParams().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Removes the XML and XSL source documents from the server cache.
     * @param objServer the server owning the cache to modify. This is a required argument only if
   <code>this.getServer()</code> does not returns a server instance.
     */
    public void resetCacheData(jsx3.app.Server objServer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "resetCacheData", objServer);
        ScriptSessions.addScript(script);
    }

    /**
     * Removes the XML source document stored under the XML ID of this object from the server cache.
     * @param objServer the server owning the cache to modify. This is a required argument only if
   <code>this.getServer()</code> does not returns a server instance.
     */
    public void resetXmlCacheData(jsx3.app.Server objServer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "resetXmlCacheData", objServer);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether this object removes its XML and XSL source documents from the cache of its server when it
is destroyed.
     * @param intShare <code>CLEANUPRESOURCES</code> or <code>SHARERESOURCES</code>. <code>CLEANUPRESOURCES</code>
  is the default value if the property is <code>null</code>.
     * @return this object.
     */

    public jsx3.xml.Cacheable setShareResources(int intShare)
    {
        String extension = "setShareResources(\"" + intShare + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets whether this object removes its XML and XSL source documents from the cache of its server when it
is destroyed.
     * @param intShare <code>CLEANUPRESOURCES</code> or <code>SHARERESOURCES</code>. <code>CLEANUPRESOURCES</code>
  is the default value if the property is <code>null</code>.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setShareResources(int intShare, Class<T> returnType)
    {
        String extension = "setShareResources(\"" + intShare + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets the source document of this object as though objDoc were retrieved from the XML URL or XML
string of this object. This method executes the following steps:

  The document is transformed serially by each XML transformers of this object.
  The XML document is saved in the server cache under the XML ID of this object.
  If this object is an instance of jsx3.xml.CDF and the root node is a <data> element
    and its jsxassignids attribute is equal to 1, all <record> elements without a
    jsxid attribute are assigned a unique jsxid.
  If this object is an instance of jsx3.xml.CDF, convertProperties() is called
    on this object.
     * @param objDoc
     * @param objCache
     * @return the document stored in the server cache as the data source of this object. If
  transformers were run, this value will not be equal to the <code>objDoc</code> parameter.
     */

    public jsx3.xml.CdfDocument setSourceXML(jsx3.xml.CdfDocument objDoc, jsx3.app.Cache objCache)
    {
        String extension = "setSourceXML(\"" + objDoc + "\", \"" + objCache + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Sets the source document of this object as though objDoc were retrieved from the XML URL or XML
string of this object. This method executes the following steps:

  The document is transformed serially by each XML transformers of this object.
  The XML document is saved in the server cache under the XML ID of this object.
  If this object is an instance of jsx3.xml.CDF and the root node is a <data> element
    and its jsxassignids attribute is equal to 1, all <record> elements without a
    jsxid attribute are assigned a unique jsxid.
  If this object is an instance of jsx3.xml.CDF, convertProperties() is called
    on this object.
     * @param objDoc
     * @param objCache
     * @param returnType The expected return type
     * @return the document stored in the server cache as the data source of this object. If
  transformers were run, this value will not be equal to the <code>objDoc</code> parameter.
     */

    public <T> T setSourceXML(jsx3.xml.CdfDocument objDoc, jsx3.app.Cache objCache, Class<T> returnType)
    {
        String extension = "setSourceXML(\"" + objDoc + "\", \"" + objCache + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets the XML ID of this object. This value is the key under which the XML source document of this object is
saved in the cache of the server owning this object. The developer may specify either a unique or shared value.
If no value is specified, a unique id is generated.
     * @param strXMLId
     * @return this object.
     */

    public jsx3.xml.Cacheable setXMLId(String strXMLId)
    {
        String extension = "setXMLId(\"" + strXMLId + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets the XML ID of this object. This value is the key under which the XML source document of this object is
saved in the cache of the server owning this object. The developer may specify either a unique or shared value.
If no value is specified, a unique id is generated.
     * @param strXMLId
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXMLId(String strXMLId, Class<T> returnType)
    {
        String extension = "setXMLId(\"" + strXMLId + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets the XML string of this object. Setting this value to the string serialization of an XML document is one
way of specifying the source XML document of this object.
     * @param strXML <code>null</code> or a well-formed serialized XML element.
     * @return this object.
     */

    public jsx3.xml.Cacheable setXMLString(String strXML)
    {
        String extension = "setXMLString(\"" + strXML + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets the XML string of this object. Setting this value to the string serialization of an XML document is one
way of specifying the source XML document of this object.
     * @param strXML <code>null</code> or a well-formed serialized XML element.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXMLString(String strXML, Class<T> returnType)
    {
        String extension = "setXMLString(\"" + strXML + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets the list of XML transformers of this object. The XML source document of this object is transformed
serially by each of these transformers before it is placed in the XML cache.

Each transformer is either the URI of an XSLT document (which will be resolved against the
the server of this object) or the cache id of a XSLT document in the XML cache of the server
of this object. When any transformer is loaded from a URI it is placed in the server cache under the id
equal to its resolved URI. Any transformer that does not correspond to a valid XSLT document will be skipped
without throwing an error.
     * @param arrTrans
     */
    public void setXMLTransformers(Object[] arrTrans)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setXMLTransformers", arrTrans);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the XML URL of this object. Settings this value to the URI of an XML document is one way of specifying the
source XML document of this object.
     * @param strXMLURL <code>null</code> or a URI that when resolved against the server owning this object
  specifies a valid XML document.
     * @return this object.
     */

    public jsx3.xml.Cacheable setXMLURL(String strXMLURL)
    {
        String extension = "setXMLURL(\"" + strXMLURL + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets the XML URL of this object. Settings this value to the URI of an XML document is one way of specifying the
source XML document of this object.
     * @param strXMLURL <code>null</code> or a URI that when resolved against the server owning this object
  specifies a valid XML document.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXMLURL(String strXMLURL, Class<T> returnType)
    {
        String extension = "setXMLURL(\"" + strXMLURL + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Adds a name/value pair to the list of parameters to pass to the XSL stylesheet during transformation. If
strValue is null the parameter is removed.
     * @param strName the name of the XSL parameter to add.
     * @param strValue the value of the XSL parameter to add.
     * @return this object.
     */

    public jsx3.xml.Cacheable setXSLParam(String strName, String strValue)
    {
        String extension = "setXSLParam(\"" + strName + "\", \"" + strValue + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Adds a name/value pair to the list of parameters to pass to the XSL stylesheet during transformation. If
strValue is null the parameter is removed.
     * @param strName the name of the XSL parameter to add.
     * @param strValue the value of the XSL parameter to add.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXSLParam(String strName, String strValue, Class<T> returnType)
    {
        String extension = "setXSLParam(\"" + strName + "\", \"" + strValue + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets whether the XML data source of this object is loaded asynchronously. This setting only applies to
data sources loaded from an XML URL.
     * @param bAsync
     * @return this object.
     */

    public jsx3.xml.Cacheable setXmlAsync(boolean bAsync)
    {
        String extension = "setXmlAsync(\"" + bAsync + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Cacheable> ctor = jsx3.xml.Cacheable.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Cacheable.class.getName());
        }
    }

    /**
     * Sets whether the XML data source of this object is loaded asynchronously. This setting only applies to
data sources loaded from an XML URL.
     * @param bAsync
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setXmlAsync(boolean bAsync, Class<T> returnType)
    {
        String extension = "setXmlAsync(\"" + bAsync + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Sets whether this object is bound to the XML document stored in the data cache. If this object is bound to the
cache, then the onXmlBinding() method of this object is called any time the document stored in
the cache under the XML Id of this object changes.
     * @param bBind
     * @param callback <code>0</code> or <code>1</code>.
     */

    public void setXmlBind(boolean bBind, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "setXmlBind", bBind);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Transfers a CDF record from another object to this object. If no XML data source exists
yet for this object, an empty one is created before adding the new record. This method always updates the
on-screen view of both the source and destination objects.

This method fails quietly if any of the following conditions apply:

there is no object with id equal to strSourceId

there is no record in the source object with jsxid equal to strRecordId


          strParentRecordId is specified and there is no record in this object with
   jsxid equal to strParentRecordId

the this object already has a record with jsxid equal to the record to adopt
     * @param strSourceId <span style="text-decoration: line-through;">either the id of the source object or the</span> source object itself.
     * @param strRecordId the <code>jsxid</code> attribute of the data record in the source object to transfer.
     * @param strParentRecordId the unique <code>jsxid</code> of an existing record. If this optional parameter
   is provided, the adopted record will be added as a child of this record. Otherwise, the adopted record will
   be added to the root <code>data</code> element.
     * @param bRedraw forces suppression of the insert event
     * @return the adopted record.
     */

    public jsx3.xml.Node adoptRecord(jsx3.xml.CdfDocument strSourceId, String strRecordId, String strParentRecordId, boolean bRedraw)
    {
        String extension = "adoptRecord(\"" + strSourceId + "\", \"" + strRecordId + "\", \"" + strParentRecordId + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


    /**
     * Transfers a CDF record from another object to this object. If no XML data source exists
yet for this object, an empty one is created before adding the new record. This method always updates the
on-screen view of both the source and destination objects.

This method fails quietly if any of the following conditions apply:

there is no object with id equal to strSourceId

there is no record in the source object with jsxid equal to strRecordId


          strParentRecordId is specified and there is no record in this object with
   jsxid equal to strParentRecordId

the this object already has a record with jsxid equal to the record to adopt
     * @param strSourceId <span style="text-decoration: line-through;">either the id of the source object or the</span> source object itself.
     * @param strRecordId the <code>jsxid</code> attribute of the data record in the source object to transfer.
     * @param strParentRecordId the unique <code>jsxid</code> of an existing record. If this optional parameter
   is provided, the adopted record will be added as a child of this record. Otherwise, the adopted record will
   be added to the root <code>data</code> element.
     * @param bRedraw forces suppression of the insert event
     * @return the adopted record.
     */

    public jsx3.xml.Node adoptRecord(String strSourceId, String strRecordId, String strParentRecordId, boolean bRedraw)
    {
        String extension = "adoptRecord(\"" + strSourceId + "\", \"" + strRecordId + "\", \"" + strParentRecordId + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


    /**
     * Equivalent to adoptRecord, except that the to-be relationship is as a previousSibling to the CDF record identified by the parameter, strSiblingRecordId

This method fails quietly if any of the following conditions apply:

there is no record with a jsxid equal to strSourceId

there is no record in the source object with a jsxid equal to strRecordId


          strSiblingRecordId is specified and there is no record in this object with a
   jsxid equal to strParentRecordId

this object already has a record with jsxid equal to the record to adopt
     * @param strSourceId <span style="text-decoration: line-through;">either the id of the source object or the</span> source object itself.
     * @param strRecordId the <code>jsxid</code> attribute of the data record in the source object to transfer.
     * @param strSiblingRecordId the unique <code>jsxid</code> of an existing record in front of
which the record identified by strSourceId will be placed
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the deleted record.
     * @return the adopted record.
     */

    public jsx3.xml.Node adoptRecordBefore(jsx3.xml.CdfDocument strSourceId, String strRecordId, String strSiblingRecordId, boolean bRedraw)
    {
        String extension = "adoptRecordBefore(\"" + strSourceId + "\", \"" + strRecordId + "\", \"" + strSiblingRecordId + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


    /**
     * Equivalent to adoptRecord, except that the to-be relationship is as a previousSibling to the CDF record identified by the parameter, strSiblingRecordId

This method fails quietly if any of the following conditions apply:

there is no record with a jsxid equal to strSourceId

there is no record in the source object with a jsxid equal to strRecordId


          strSiblingRecordId is specified and there is no record in this object with a
   jsxid equal to strParentRecordId

this object already has a record with jsxid equal to the record to adopt
     * @param strSourceId <span style="text-decoration: line-through;">either the id of the source object or the</span> source object itself.
     * @param strRecordId the <code>jsxid</code> attribute of the data record in the source object to transfer.
     * @param strSiblingRecordId the unique <code>jsxid</code> of an existing record in front of
which the record identified by strSourceId will be placed
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the deleted record.
     * @return the adopted record.
     */

    public jsx3.xml.Node adoptRecordBefore(String strSourceId, String strRecordId, String strSiblingRecordId, boolean bRedraw)
    {
        String extension = "adoptRecordBefore(\"" + strSourceId + "\", \"" + strRecordId + "\", \"" + strSiblingRecordId + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


    /**
     * Converts all attributes in this CDF document that are property keys of the form {key} to
the value of the property.
     * @param objProps the properties repository to query.
     * @param arrProps if provided, these attributes are converted rather than the default set of
   attributes.
     * @param bUnion if <code>true</code>, <code>arrProps</code> is combined with the default set of
   attributes and those attributes are converted.
     */
    public void convertProperties(java.util.Properties objProps, Object[] arrProps, boolean bUnion)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "convertProperties", objProps, arrProps, bUnion);
        ScriptSessions.addScript(script);
    }

    /**
     * Removes a record from the XML data source of this object.
     * @param strRecordId the <code>jsxid</code> attribute of the data record to remove.
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the deleted record.
     * @return the record removed from the data source or <code>null</code> if no such record found.
     */

    public jsx3.xml.Node deleteRecord(String strRecordId, boolean bRedraw)
    {
        String extension = "deleteRecord(\"" + strRecordId + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


    /**
     * Removes a specific property from a record. If no such record exists in the XML document, this method fails quietly.
     * @param strRecordId the <code>jsxid</code> attribute of the data record to modify.
     * @param strPropName the name of the property to remove from the record.
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the deleted property.
     */
    public void deleteRecordProperty(String strRecordId, String strPropName, boolean bRedraw)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "deleteRecordProperty", strRecordId, strPropName, bRedraw);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns an object containing the attributes of a particular CDF record as property/value pairs. The object returned by this
method is a copy of the underlying data. Therefore, updates to this object will not affect the underlying data.

The following two lines of code evaluate to the same value:

objCDF.getRecord(strId).propName;
objCDF.getRecordNode(strId).getAttribute("propName");
     * @param strRecordId the <code>jsxid</code> attribute of the data record to return.
     * @return the object representation of a CDF node or <code>null</code> if no such record found.
     */

    public jsx3.lang.Object getRecord(String strRecordId)
    {
        String extension = "getRecord(\"" + strRecordId + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.lang.Object> ctor = jsx3.lang.Object.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.lang.Object.class.getName());
        }
    }

    /**
     * Returns an object containing the attributes of a particular CDF record as property/value pairs. The object returned by this
method is a copy of the underlying data. Therefore, updates to this object will not affect the underlying data.

The following two lines of code evaluate to the same value:

objCDF.getRecord(strId).propName;
objCDF.getRecordNode(strId).getAttribute("propName");
     * @param strRecordId the <code>jsxid</code> attribute of the data record to return.
     * @param returnType The expected return type
     * @return the object representation of a CDF node or <code>null</code> if no such record found.
     */

    public <T> T getRecord(String strRecordId, Class<T> returnType)
    {
        String extension = "getRecord(\"" + strRecordId + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns a record from the XML data source of this object. This returned value is a handle to the record and
not a clone. Therefore, any updates made to the returned value with update the XML document of this object.
To reflect such changes in the on-screen view of this object, call
redrawRecord(strRecordId, jsx3.xml.CDF.UPDATE); on this object.
     * @param strRecordId the <code>jsxid</code> attribute of the data record to return.
     * @return the record node or <code>null</code> if none exists with a <code>jsxid</code>
   attribute equal to <code>strRecordId</code>.
     */

    public jsx3.xml.Node getRecordNode(String strRecordId)
    {
        String extension = "getRecordNode(\"" + strRecordId + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


    /**
     * Inserts a new record into the XML data source of this object. If no XML data source exists
yet for this object, an empty one is created before adding the new record.
If a record already exists with an id equal to the jsxid property of objRecord,
the operation is treated as an update, meaning the existing record is completely removed and a new record with
the given jsxid is inserted.
     * @param objRecord a JavaScript object containing property/value pairs that define the
   attributes of the XML entity to create. Note that most classes that implement this interface require that all
   records have an attribute named <code>jsxid</code> that is unique across all records in the XML document.
   All property values will be treated as strings. Additionally, the following 3 characters are escaped:
   <code>" &gt; &lt;</code>.
     * @param strParentRecordId the unique <code>jsxid</code> of an existing record. If this optional parameter
   is provided and a record exists with a matching <code>jsxid</code> attribute, the new record will be added as a child of
   this record. Otherwise, the new record will be added to the root <code>data</code> element. However, if a
   record already exists with a <code>jsxid</code> attribute equal to the <code>jsxid</code> property of
   <code>objRecord</code>, this parameter will be ignored. In this case <code>adoptRecord()</code> must be called
   to change the parent of the record.
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the additional record.
     * @return the newly created or updated entity.
     */

    public jsx3.xml.Node insertRecord(jsx3.lang.Object objRecord, String strParentRecordId, boolean bRedraw)
    {
        String extension = "insertRecord(\"" + objRecord + "\", \"" + strParentRecordId + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


    /**
     * Creates a new CDF record and inserts it into the CDF data source of this object, before the record identified by strSiblingRecordId.

This method fails quietly if any of the following conditions apply:

there is no existing record with a jsxid equal to strSiblingRecordId

there is an existing record with jsxid equal to objRecord.jsxid
     * @param objRecord a JavaScript object containing property/value pairs that define the
   attributes of the XML entity to create. Note that most classes that implement this interface require that all
   records have an attribute named <code>jsxid</code> that is unique across all records in the XML document.
   All property values will be treated as strings. Additionally, the following 3 characters are escaped:
   <code>" &gt; &lt;</code>.
     * @param strSiblingRecordId the unique <code>jsxid</code> of an existing record before which the new record will be inserted.
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the additional record.
     * @return the newly created entity.
     */

    public jsx3.xml.Node insertRecordBefore(jsx3.lang.Object objRecord, String strSiblingRecordId, boolean bRedraw)
    {
        String extension = "insertRecordBefore(\"" + objRecord + "\", \"" + strSiblingRecordId + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.Node> ctor = jsx3.xml.Node.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.Node.class.getName());
        }
    }


    /**
     * Inserts a new record into the XML data source of this object. This method is the same as
insertRecord() except that its first parameter is of type jsx3.xml.Entity rather than
Object.
     * @param objRecordNode an XML element of name <code>record</code>. Note that most classes that
   implement this interface require that all records have an attribute named <code>jsxid</code> that is unique
   across all records in the XML document.
     * @param strParentRecordId the unique <code>jsxid</code> of an existing record. If this optional parameter
   is provided and a record exists with a matching <code>jsxid</code> attribute, the new record will be added as a child of
   this record. Otherwise, the new record will be added to the root <code>data</code> element.
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the additional record.
     */
    public void insertRecordNode(jsx3.xml.Node objRecordNode, String strParentRecordId, boolean bRedraw)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "insertRecordNode", objRecordNode, strParentRecordId, bRedraw);
        ScriptSessions.addScript(script);
    }

    /**
     * Inserts a new property into an existing record with jsxid equal to strRecordId.
If the property already exists, the existing property value will be updated. If no such record exists
in the XML document, this method fails quietly.
     * @param strRecordId the <code>jsxid</code> attribute of the data record to modify.
     * @param strPropName the name of the property to insert into the record.
     * @param strPropValue the value of the property to insert.
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the inserted property.
     * @return this object.
     */

    public jsx3.xml.CdfDocument insertRecordProperty(String strRecordId, String strPropName, String strPropValue, boolean bRedraw)
    {
        String extension = "insertRecordProperty(\"" + strRecordId + "\", \"" + strPropName + "\", \"" + strPropValue + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Inserts a new property into an existing record with jsxid equal to strRecordId.
If the property already exists, the existing property value will be updated. If no such record exists
in the XML document, this method fails quietly.
     * @param strRecordId the <code>jsxid</code> attribute of the data record to modify.
     * @param strPropName the name of the property to insert into the record.
     * @param strPropValue the value of the property to insert.
     * @param bRedraw if <code>true</code> or <code>null</code>, the on-screen view of this object is
   immediately updated to reflect the inserted property.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T insertRecordProperty(String strRecordId, String strPropName, String strPropValue, boolean bRedraw, Class<T> returnType)
    {
        String extension = "insertRecordProperty(\"" + strRecordId + "\", \"" + strPropName + "\", \"" + strPropValue + "\", \"" + bRedraw + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

}

