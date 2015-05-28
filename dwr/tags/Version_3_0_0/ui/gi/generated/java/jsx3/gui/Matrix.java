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
 * The Matrix control is the standard visual interface for the Common Data Format (CDF), providing grid and tree-grid functionality that mirrors the
the record and attribute structures used by the CDF. Instances of this class can be used to create editable grids, selectable lists, trees, tables, etc.
In addtion to providing layout, selection, and editing schemes, the Matrix also provides various paging models to help optimize
how (and how much) data is rendered on-screen. The Matrix class is always used in conjunction with jsx3.gui.Matrix.Column, which describes
how data for a given series should be rendered on-screen. While the Matrix manages data and user interactions, Column
manages the on-screen format for how the data is presented.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Matrix extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Matrix(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     */
    public Matrix(String strName)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Matrix", strName);
        setInitScript(script);
    }


    /**
     * 50
     */
    public static final int AUTO_SCROLL_INTERVAL = 50;

    /**
     * jsx:///images/matrix/select.gif
     */
    public static final String SELECTION_BG = "jsx:///images/matrix/select.gif";

    /**
     * jsx:///images/matrix/insert_before.gif
     */
    public static final String INSERT_BEFORE_IMG = null;

    /**
     * jsx:///images/matrix/append.gif
     */
    public static final String APPEND_IMG = null;

    /**
     * font-weight:bold
     */
    public static final String FOCUS_STYLE = "font-weight:bold";

    /**
     * jsx:///images/matrix/minus.gif (default)
     */
    public static final String ICON_MINUS = "jsx:///images/matrix/minus.gif";

    /**
     * jsx:///images/matrix/plus.gif (default)
     */
    public static final String ICON_PLUS = "jsx:///images/matrix/plus.gif";

    /**
     * jsx:///images/matrix/file.gif (default)
     */
    public static final String ICON = "jsx:///images/matrix/file.gif";

    /**
     * ascending
     */
    public static final String SORT_ASCENDING = "ascending";

    /**
     * descending
     */
    public static final String SORT_DESCENDING = "descending";

    /**
     * jsx:///images/matrix/sort_desc.gif (default)
     */
    public static final String SORT_DESCENDING_IMG = null;

    /**
     * jsx:///images/matrix/sort_asc.gif (default)
     */
    public static final String SORT_ASCENDING_IMG = null;

    /**
     * 4. minimum width of a column when minimized (set Display to 'none' to completely hide a column)
     */
    public static final int MINIMUM_COLUMN_WIDTH = 8;

    /**
     * 20
     */
    public static final int DEFAULT_HEADER_HEIGHT = 20;

    /**
     * 0
     */
    public static final int AUTOROW_NONE = 0;

    /**
     * 1
     */
    public static final int AUTOROW_LAST_ROW = 1;

    /**
     * 2
     */
    public static final int AUTOROW_FIRST_ROW = 2;

    /**
     * jsxpaintpage. Event to subscribe to each time a page of content is about to be painted on-screen
     */
    public static final String ON_PAINT_PAGE = "jsxpaintpage";

    /**
     * Default. All data is painted at once with the outer container.
     */
    public static final int PAGING_OFF = 0;

    /**
     * The outer container is first painted and then the entirety of the data is painted during a second pass
     */
    public static final int PAGING_2PASS = 1;

    /**
     * The outer container is first painted. Chunked sets of data are painted on-screen during repeated passes until
      all data is painted
     */
    public static final int PAGING_CHUNKED = 2;

    /**
     * The outer container is first painted. The first and last panels are painted during a second pass. As the user
      scrolls, relevant panels are added and unused panels are collected. (NOTE: Requires that row height be fixed.)
     */
    public static final int PAGING_PAGED = 3;

    /**
     * The outer container is painted along with any rows which are immediate children of the rendering context and those
      descendant rows that have an open path to the context node. All other rows will be fetched when the state for
      their on-screen parent row is toggled to open.
     */
    public static final int PAGING_STEPPED = 4;

    /**
     * 0
     */
    public static final int SELECTION_UNSELECTABLE = 0;

    /**
     * 1 (default)
     */
    public static final int SELECTION_ROW = 1;

    /**
     * 2
     */
    public static final int SELECTION_MULTI_ROW = 2;

    /**
     * 20
     */
    public static final int DEFAULT_ROW_HEIGHT = 20;

    /**
     * 18. number of panels allowed on-screen before destroying the panel most distant from the current panel index
     */
    public static final int DEFAULT_PANEL_POOL_COUNT = 5;

    /**
     * 50. number of rows in a given panel
     */
    public static final int DEFAULT_ROWS_PER_PANEL = 50;

    /**
     * 250. number of milliseconds between the time a new panel is added and the reaper checks for content exceeding Matrix.DEFAULT_PANEL_POOL_COUNT
     */
    public static final int DEFAULT_REAPER_INTERVAL = 250;

    /**
     * 3. number of panels in the paint queue. As new panels are added to the queue to be painted, older, less-relevant panels in the queue are removed
     */
    public static final int DEFAULT_PANEL_QUEUE_SIZE = 3;

    /**
     * 
     */
    public static final String DEFAULTXSLURL = null;


    /**
     * Ends any existing edit session and hides the active mask. This is a carryover method from grid.
     */
    public void resetMask()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "resetMask");
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
     * Repaints the header row for improved runtime efficiency. For example, a minor text change to a label in a header row shouldn't repaint the entirety of the instance
     */
    public void repaintHead()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "repaintHead");
        ScriptSessions.addScript(script);
    }

    /**
     * Sorts according to the current sort path. If no sort direction is specified, the value will be toggled.
     * @param intSortDir <code>jsx3.gui.Matrix.SORT_ASCENDING</code> or <code>jsx3.gui.Matrix.SORT_DESCENDING</code>.
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
     * Sets the name of the CDF attribute to sort on. The records in the data source of this matrix are sorted
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
     * Returns the data type to be used for sorting this list. This value is either the one explicitly set with
setSortType() or the data type of the current sort.
     * @param callback <code>jsx3.gui.Matrix.Column.TYPE_TEXT</code> or <code>jsx3.gui.Matrix.Column.TYPE_NUMBER</code>
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
     * Sets the data type for the list. This explicit value will override any column data type if set. If it is not set
the data type specific to the sort column is used for sorting.
     * @param DATATYPE data type for this column's data; ; valid types include: jsx3.gui.Matrix.Column.TYPE_TEXT and jsx3.gui.Matrix.Column.TYPE_NUMBER
     */
    public void setSortType(String DATATYPE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSortType", DATATYPE);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the direction (jsx3.gui.Matrix.SORT_ASCENDING or jsx3.gui.Matrix.SORT_DESCENDING) for the sorted column; if no direction specified, ascending is returned
     * @param callback one of: jsx3.gui.Matrix.SORT_ASCENDING or jsx3.gui.Matrix.SORT_DESCENDING
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
     * @param intSortDir one of: jsx3.gui.Matrix.SORT_ASCENDING or jsx3.gui.Matrix.SORT_DESCENDING
     */
    public void setSortDirection(String intSortDir)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSortDirection", intSortDir);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether the list will render with sortable columns. If null or jsx3.Boolean.TRUE, the instance is sortable.
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
     * Sets whether the list will render with sortable columns.
     * @param SORT one of <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */
    public void setCanSort(int SORT)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCanSort", SORT);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether the column children can be reordered via user interaction. If no value is supplied
the isntance will allow child columns to be reordered.
     * @param callback one of: jsx3.Boolean.TRUE or jsx3.Boolean.FALSE
     */

    public void getCanReorder(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCanReorder");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether the columns in the list can be re-ordered via user interaction with the VIEW
     * @param REORDER one of: jsx3.Boolean.TRUE or jsx3.Boolean.FALSE
     */
    public void setCanReorder(int REORDER)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCanReorder", REORDER);
        ScriptSessions.addScript(script);
    }

    /**
     * Applies focus to the on-screen row that corresponds to the element in the CDF source document identified by
strCdfId. Note that since only cells can receive focus, this method will apply focus to the
first cell child in the row.
     * @param strCdfId jsxid property for CDF record
     */
    public void focusRowById(String strCdfId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "focusRowById", strCdfId);
        ScriptSessions.addScript(script);
    }

    /**
     * Applies focus to the on-screen cell that corresponds to the intersection of the element in the CDF source
document identified by strCdfId, and the first column mapped to strAttName.
     * @param strCdfId jsxid property for CDF record
     * @param strAttName attribute name on the CDF record. For example, <code>jsxtext</code>
     */
    public void focusCellById(String strCdfId, String strAttName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "focusCellById", strCdfId, strAttName);
        ScriptSessions.addScript(script);
    }

    /**
     * Applies focus to the on-screen cell that corresponds to the intersection of
the element in the CDF source document identified by strCdfId, and the cell at the given index.
     * @param strCdfId jsxid property for CDF record
     * @param intCellIndex zero-based index of cell (on-screen).
     */
    public void focusCellByIndex(String strCdfId, int intCellIndex)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "focusCellByIndex", strCdfId, intCellIndex);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS string to apply to a Row/Cell when it has focus
     * @param strDefault The default value to use if null (Matrix.FOCUS_STYLE)
     */

    public void getFocusStyle(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFocusStyle", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the CSS string to apply to a Row/Cell when it has focus. NOTE: Passing
styles that affect position, left, top, width, height, border, background-image, padding, and margin
(those reserved by the class) can have undesired effects.
     * @param strCSS Valid CSS. For example: font-weight:bold;color:orange;
     */
    public void setFocusStyle(String strCSS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFocusStyle", strCSS);
        ScriptSessions.addScript(script);
    }

    /**
     * Removes the focus style from whichever cell is designated to have focus context. Once focus context
is applied to the active cell in a matrix, the focus style will continue to be applied to
the active cell until another cell is set as the active cell or the Matrix is repainted via
a call to repaint or repaintData.
     * @param strId
     */
    public void resetFocusContext(String strId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "resetFocusContext", strId);
        ScriptSessions.addScript(script);
    }

    /**
     * Evaluates the JavaScript code in the jsxexecute attribute of one CDF record of this list.
     * @param strRecordId the jsxid of the CDF record to execute.
     */
    public void executeRecord(String strRecordId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "executeRecord", strRecordId);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the CSS string to apply to a Row/Cell when it has focus
     * @param strDefault The default value to use if null (Matrix.SELECTION_BG)
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
     * Deselects a CDF record within the Matrix. Both the view and the data model (CDF) will be updated
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
     * Returns an object array of name/value pairs representing the current auto row session. When the session is committed, this
object will be converted into a CDF Record for the instance.
     */

    public jsx3.lang.Object getAutoRowSession()
    {
        String extension = "getAutoRowSession().";
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
     * Returns an object array of name/value pairs representing the current auto row session. When the session is committed, this
object will be converted into a CDF Record for the instance.
     * @param returnType The expected return type
     */

    public <T> T getAutoRowSession(Class<T> returnType)
    {
        String extension = "getAutoRowSession().";
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
     * Commits any active autorow session.
     * @param objEvent If passed, Allows Model Event to fire.
     * @param intCellIndex Focus will be applied to the autorow cell at this index (zero-based)
     */
    public void commitAutoRowSession(jsx3.gui.Event objEvent, int intCellIndex)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "commitAutoRowSession", objEvent, intCellIndex);
        ScriptSessions.addScript(script);
    }

    /**
     * Toggles the open/closed state for a node in the Matrix
     * @param strRecordId the 'jsxid' attribute on the given CDF record to toggle
     * @param bOpen if null, the open state will be toggled
     */
    public void toggleItem(String strRecordId, boolean bOpen)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "toggleItem", strRecordId, bOpen);
        ScriptSessions.addScript(script);
    }

    /**
     * Reveals a record by toggling parent nodes open (if rendering hierarcally) and scrolling the record into view.
     * @param strRecordId the id of the record to reveal
     */
    public void revealRecord(String strRecordId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "revealRecord", strRecordId);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether or not this column can be resized by the user. If not set, the column will be assumed resizable
     */

    public void getResizable(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getResizable");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether or not this column can be resized by the user. If not set, the column will be assumed resizable. Note that if the parent Matrix
is set as NOT resizable, this setting is ignored and no child columns can be resized
     * @param RESIZE
     */
    public void setResizable(int RESIZE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setResizable", RESIZE);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether or not an 'auto append' row will be rendered, allowing the user to automatically add new rows to the instance.
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getAutoRow(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getAutoRow");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether or not an 'auto append' row will be rendered, allowing the user to automatically add new rows to the instance. Note
that if the rendering model is hierarchical or the paging model is jsx3.gui.Matrix.PAGING_PAGED, the auto row
feature is disabled.  The CSS style for the auto row (a TR element) can be modified via the XSL Parameters palette, via the XSL
parameter, jsx_autorow_style
     * @param intBoolean jsx3.Boolean.TRUE if the column widths should be adjusted to fully fit within the viewport
     */
    public void setAutoRow(int intBoolean)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setAutoRow", intBoolean);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the HTML element that represents the intersection of the row identified
by strCdfId and the first column mapped to the named CDF attribute, strAttName.
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
     * Returns an array of all jsxid attributes in the source CDF in the order they would appear if painted on-screen
     */

    public void getSortedIds(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSortedIds");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

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
     * @param intType one of Matrix: .SELECTION_UNSELECTABLE, .SELECTION_ROW, .SELECTION_MULTI_ROW
     */
    public void setSelectionModel(int intType)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSelectionModel", intType);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the row height
     * @param strDefault The default value to use if null
     */

    public void getRowHeight(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRowHeight", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the on-screen row height. If row height is null, the default row height will be used (Matrix.DEFAULT_ROW_HEIGHT).
If row height is 0, the row height is flexible and the row's height will expand to fit the content.
     * @param intHeight height in pixels
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the browser-specific box model (such as padding) are repainted
immediately to keep the box model abstraction in synch with the native view. However, the repaint can be
suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     */
    public void setRowHeight(int intHeight, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRowHeight", intHeight, bSuppressRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the number of rows each panel should contain.  If null, the default value will be used: jsx3.gui.Matrix.DEFAULT_ROWS_PER_PANEL
     * @param strDefault The default value to use if null
     */

    public void getRowsPerPanel(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRowsPerPanel", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the number of rows each panel should contain.
     * @param intCount
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
     */
    public void setRowsPerPanel(int intCount, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRowsPerPanel", intCount, bSuppressRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the number of panels that are allowed in the queue waiting to be painted. If null, the default value will be used: jsx3.gui.Matrix.DEFAULT_PANEL_QUEUE_SIZE
Note that this is different from the number of painted panels allowed on screen (e.g., getPanelPoolSize()).
     * @param strDefault The default value to use if null
     */

    public void getPanelQueueSize(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPanelQueueSize", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the number of panels that are allowed in the queue waiting to be painted. Can be tuned up or down to optimize performance given the amount of data, connection speed, etc
     * @param intCount
     */
    public void setPanelQueueSize(int intCount)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPanelQueueSize", intCount);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the the number of milliseconds to wait before checking for inactive panels to garbage collect.  If null, the default value will be used: jsx3.gui.Matrix.DEFAULT_REAPER_INTERVAL
     * @param strDefault The default value to use if null
     */

    public void getReaperInterval(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getReaperInterval", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the the number of milliseconds to wait before checking for inactive panels to garbage collect.
     * @param intInterval number of milliseconds
     */
    public void setReaperInterval(int intInterval)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setReaperInterval", intInterval);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the number panels (a panel contains a collection of rows--getRowsPerPanel()) that should be part of the pool.  If a panel count greater
than this value exists, the panels furthest away (as calculated by the scroll position) from the active panel will be destroyed. If this value is null,
the value defined by the constant, Matrix.DEFAULT_PANEL_POOL_COUNT, will be used.
     * @param strDefault The default value to use if null
     */

    public void getPanelPoolSize(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPanelPoolSize", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the number panels (a panel contains a collection of rows--getRowsPerPanel()) that should be part of the pool.
     * @param intCount
     */
    public void setPanelPoolSize(int intCount)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPanelPoolSize", intCount);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns how data should be painted on-screen.  If no value is specified, Matrix.PAGING_OFF will be applied. Note that the rendering model limits the available paging models:

        Matrix.PAGING_OFF: Paint everthing to screen at once (container and data) (rendering model: all)

Matrix.PAGING_2PASS: Paint outer container and then perform a second pass to paint the data.  (rendering model: deep, shallow)

Matrix.PAGING_CHUNKED: Paint outer container and then perform repeated paints until all data has been painted, regardless of scroll position.  (rendering model: deep, shallow)

Matrix.PAGING_PAGED: Paint outer container. Paint First and last panels during second pass.  Paint relevant panels when user scrolls to a given position. Discard excess panels. (rendering model: deep, shallow)

Matrix.PAGING_STEPPED: Paint root nodes and any open descendants. Paint others as they are toggled open. (rendering model: hierarchical)
     * @param strDefault The default value to use if null
     */

    public void getPagingModel(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPagingModel", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets how data should be painted on-screen.  If no value is specified, Matrix.PAGING_OFF will be applied.
     * @param intModel one of: Matrix. PAGING_OFF, PAGING_2PASS, PAGING_CHUNKED, PAGING_PAGED, PAGING_STEPPED
     */
    public void setPagingModel(int intModel)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPagingModel", intModel);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the height of the header row in pixels. If this value is not set (null), the list will render with
the default value of jsx3.gui.Matrix.DEFAULT_HEADER_HEIGHT.
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
     * Returns the info label to display when scrolling a paged instance, in order to show the scroll position.
     * @param strDefault The default value to use if null
     */

    public void getScrollInfoLabel(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getScrollInfoLabel", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the standard info label to display when scrolling to show the scroll position.  If no label is supplied
an appropriate localized value will be used. Set to an empty string to suppress any label from displaying.
     * @param strLabel valid HTML/Text.  Set to an empty string to suppress any label from displaying.
<br/>Wildcards are as follows:
<ul><li><b>{0}</b> The index position of the first visible on-screen row</li>
<li><b>{1}</b> The index position of the last visible on-screen row</li>
<li><b>{2}</b> Total count of all records in the list</li></ul>
     */
    public void setScrollInfoLabel(String strLabel)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setScrollInfoLabel", strLabel);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the horizontal scroll position of the list.
     * @param callback a non-negative number
     */

    public void getScrollLeft(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getScrollLeft");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the horizontal scroll position.
     * @param intScrollLeft a non-negative number
     */
    public void setScrollLeft(int intScrollLeft)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setScrollLeft", intScrollLeft);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the vertical scroll position.
     * @param callback a non-negative number
     */

    public void getScrollTop(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getScrollTop");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the vertical scroll position.
     * @param intScrollTop a non-negative number
     * @param objGUI
     */
    public void setScrollTop(int intScrollTop, String objGUI)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setScrollTop", intScrollTop, objGUI);
        ScriptSessions.addScript(script);
    }

    /**
     * Updates the scroll height and scroll position of the vertical scrollbar. When a Matrix instance has
a display property of none (or is contained by an ancestor with a display of none) and the Matrix is repainted (repaint/repaintData),
the browser will misreport how large the content actually is.  When the Matrix is then restored the scrollbars will be disabled.
By calling this method after the view has been restored (i.e., when display is set to block), the scrollbars will reflect the accurate height.
     */
    public void synchronizeVScroller()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "synchronizeVScroller");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether or not the column widths should be adjusted (decremented) such that all columns fit within the viewport.
If null or jsx3.Boolean.FALSE, scale width will not be used and the column widths will render
fully, displaying a horizontal scrollbar when necessary. In such a case, all wildcard columns (e.g., *) will be resolved to the value,
jsx3.gui.Matrix.Column.DEFAULT_WIDTH.
     */

    public void getScaleWidth(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getScaleWidth");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether or not the column widths should be adjusted such that all columns visually display within the viewport.
Defaults to jsx3.Boolean.FALSE if not set, meaning a horizontal scrollbar will appear if the aggregate column widths
exceed the available width of the viewport.
     * @param intBoolean One of: <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */
    public void setScaleWidth(int intBoolean)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setScaleWidth", intBoolean);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns CSS property value(s) for a border (border: solid 1px #000000)
     */

    public void getHeaderBorder(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHeaderBorder");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets CSS property value(s) for a border on the header row (border: solid 1px #000000). Updates both model and view.
     * @param strCSS valid CSS property value for a border (border: solid 1px #000000)
     */
    public void setHeaderBorder(String strCSS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHeaderBorder", strCSS);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns CSS property value(s) for a border (border: solid 1px #000000)
     */

    public void getBodyBorder(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getBodyBorder");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets CSS property value(s) for a border (border: solid 1px #000000). Updates MODEL and VIEW (unless repaint is suppressed).
     * @param strCSS valid CSS property value for a border (border: solid 1px #000000)
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the browser-specific box model (such as borders) are repainted
immediately to keep the box model abstraction in synch with the native view. However, the repaint can be
suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     * @return this object
     */

    public jsx3.gui.Block setBodyBorder(String strCSS, boolean bSuppressRepaint)
    {
        String extension = "setBodyBorder(\"" + strCSS + "\", \"" + bSuppressRepaint + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.gui.Block> ctor = jsx3.gui.Block.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.gui.Block.class.getName());
        }
    }

    /**
     * Sets CSS property value(s) for a border (border: solid 1px #000000). Updates MODEL and VIEW (unless repaint is suppressed).
     * @param strCSS valid CSS property value for a border (border: solid 1px #000000)
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the browser-specific box model (such as borders) are repainted
immediately to keep the box model abstraction in synch with the native view. However, the repaint can be
suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T setBodyBorder(String strCSS, boolean bSuppressRepaint, Class<T> returnType)
    {
        String extension = "setBodyBorder(\"" + strCSS + "\", \"" + bSuppressRepaint + "\").";
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
     * Returns an array of selected values (or empty array) if the selection model is Matrix.SELECTION_MULTI_ROW. Returns a string (or null)
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
     * 
     * @param callback <code>jsx3.gui.Form.STATEINVALID</code> or <code>jsx3.gui.Form.STATEVALID</code>.
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
     * Returns the rendering model (how rows will be painted on-screen). If not set, the instance will render deep, meaning all descendants
of the rendering context will be painted on-screen.
     * @param strDefault The default value to use if null
     */

    public void getRenderingModel(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRenderingModel", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the rendering model (how rows will be painted on-screen).
     * @param MODEL one of: shallow, deep, or hierarchical
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
     */
    public void setRenderingModel(String MODEL, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRenderingModel", MODEL, bSuppressRepaint);
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
     * Returns whether or not to supress display of the horizontal scrollbar. When not set, the scrollbar will display as needed.
     * @param strDefault The default value to use if null
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getSuppressHScroller(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSuppressHScroller", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether or not to supress display of the horizontal scrollbar. Updates both model and view
     * @param intTrueFalse <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */
    public void setSuppressHScroller(int intTrueFalse)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSuppressHScroller", intTrueFalse);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether or not to supress display of the vertical scrollbar. When not set, the scrollbar will display as needed.
     * @param strDefault The default value to use if null
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getSuppressVScroller(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSuppressVScroller", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether or not to supress display of the vertical scrollbar. Updates both model and view.
     * @param intTrueFalse <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the browser-specific box model (such as rendering the vertical scrollbar) are repainted
immediately to keep the box model abstraction in synch with the native view. However, the repaint can be
suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     */
    public void setSuppressVScroller(int intTrueFalse, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSuppressVScroller", intTrueFalse, bSuppressRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the zero-based index of the on-screen column(s), to the left of which will be fixed and cannot be reordered.  For example, if this value
is set to 1, the first column can never be reordered and will always remain the first column.  If this value is set to 2,
the first two columns will be fixed.  Setting this value to 0 is effectively the same as setting it to null
     * @param strDefault The default value to use if null
     * @param callback positive integer
     */

    public void getFixedColumnIndex(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFixedColumnIndex", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the zero-based index of the on-screen column(s), to the left of which will be fixed and cannot be reordered.
     * @param intIndex positive integer
     */
    public void setFixedColumnIndex(int intIndex)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFixedColumnIndex", intIndex);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns whether or not to render the navigation controls that are applied to the first column when rendering model is hierarchical.  When not set the navigators are rendered.
     * @param strDefault The default value to use if null
     * @param callback <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */

    public void getRenderNavigators(String strDefault, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getRenderNavigators", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets whether or not to render the navigation controls on the first column when being rendered in hierarchical mode.
     * @param intTrueFalse <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the browser-specific box model (such as rendering the navigational controls) are repainted
immediately to keep the box model abstraction in synch with the native view. However, the repaint can be
suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     */
    public void setRenderNavigators(int intTrueFalse, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setRenderNavigators", intTrueFalse, bSuppressRepaint);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the icon to use for those CDF records that do not explicitly specify an icon via the jsximg attribute
     * @param strDefault The default value to use if null
     * @param callback URL for icon to use. If null, <code>jsx3.gui.Matrix.ICON</code> will be applied when rendered.
     */

    public void getIcon(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getIcon", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the icon to use for those CDF records that do not explicitly specify an icon via the jsximg attribute
     * @param strURL URL for icon to use
     */
    public void setIcon(String strURL)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setIcon", strURL);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the icon to use when the given tree node is in an open state.
     * @param strDefault The default value to use if null
     * @param callback URL for icon. If null, <code>jsx3.gui.Matrix.ICON_MINUS</code> will be applied when rendered.
     */

    public void getIconMinus(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getIconMinus", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the icon to use when the given tree node is in an open state.
     * @param strURL URL (preferably relative)
     */
    public void setIconMinus(String strURL)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setIconMinus", strURL);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the icon to use when the given tree node is in a closed state.
     * @param strDefault The default value to use if null
     * @param callback URL for icon to use. If null, <code>jsx3.gui.Matrix.ICON_PLUS</code> will be applied when rendered.
     */

    public void getIconPlus(String strDefault, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getIconPlus", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the icon to use when the given tree node is in a closed state.
     * @param strURL URL (preferably relative)
     */
    public void setIconPlus(String strURL)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setIconPlus", strURL);
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

    /**
     * Updates the on-screen cell to reflect the value in the CDF document to which the cell is mapped.
     * @param strRecordId <code>jsxid</code> value for the record node (according to the CDF) corresponding to the on-screen row to update
     * @param objColumn Column instance to update. Any sibling Columns that map to the same named attribute as <b>objColumn</b>
(e.g., <code>[objColumn].getPath()</code>) as well as all sibling Columns that are triggered by the named attribute
(e.g., <code>[objColumn].getTriggers()</code>) will also be redrawn.
     * @param bSuppressTriggers if true, no other sibling Columns will be updated, even if they share a common path or designate the path as one of their triggers.
     */
    public void redrawCell(String strRecordId, jsx3.gui.matrix.Column objColumn, boolean bSuppressTriggers)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "redrawCell", strRecordId, objColumn, bSuppressTriggers);
        ScriptSessions.addScript(script);
    }

    /**
     * Updates the on-screen cell to reflect the value in the CDF document to which the cell is mapped.
     * @param strRecordId The <code>jsxid</code> value for the record node (according to the CDF) corresponding to the on-screen row to update
     * @param strAttName Named attribute on the CDF record. All Column children that map to this named attribute
(e.g., <code>[objColumn].getPath()</code>) as well as all Column children that are triggered by the named attribute
(e.g., <code>[objColumn].getTriggers()</code>) will be redrawn.
     */
    public void redrawMappedCells(String strRecordId, String strAttName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "redrawMappedCells", strRecordId, strAttName);
        ScriptSessions.addScript(script);
    }

    /**
     * Matrix implementation.
     * @param strRecordId <code>jsxid</code> value for the record node (according to the CDF) to redraw
     * @param intAction <code>jsx3.xml.CDF.INSERT</code>, <code>jsx3.xml.CDF.INSERTBEFORE</code>, <code>jsx3.xml.CDF.DELETE</code>, or <code>jsx3.xml.CDF.UPDATE</code>.
     * @param bRecurse if != false, any necessary recursion for flattened inserts will be automatically handled
     */
    public void redrawRecord(String strRecordId, int intAction, boolean bRecurse)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "redrawRecord", strRecordId, intAction, bRecurse);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the value of this matrix. Deselects all existing selections. Scrolls the first record into view.
     * @param strId jsxid attribute for the CDF record(s) to select
     * @return this object.
     */
    public jsx3.gui.Matrix setValue(Object[] strId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValue", strId);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Sets the value of this matrix. Deselects all existing selections. Scrolls the first record into view.
     * @param strId jsxid attribute for the CDF record(s) to select
     * @return this object.
     */
    public jsx3.gui.Matrix setValue(String strId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValue", strId);
        ScriptSessions.addScript(script);
        return this;
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

}

