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
package jsx3.gui.matrix;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * Column control for use as a child of a jsx3.gui.Matrix class
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Column extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public Column(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     */
    public Column(String strName)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Column", strName);
        setInitScript(script);
    }


    /**
     * 100
     */
    public static final int DEFAULT_WIDTH = 100;

    /**
     * text (default)
     */
    public static final String TYPE_TEXT = "text";

    /**
     * number
     */
    public static final String TYPE_NUMBER = "number";

    /**
     * top
     */
    public static final String DEFAULT_VALIGN = "top";


    /**
     * Gets the user-defined XSL template (xsl:template) that will override the defualt template defined by Column.TEMPLATES.default.
     * @param strDefault xsl:template
     */
    @SuppressWarnings("unchecked")
    public void getValueTemplate(String strDefault, org.directwebremoting.proxy.Callback<String> callback)
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
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the user-defined XSL template (xsl:template) that will override the defualt template defined by Column.DEFAULT_VALUE_TEMPLATE.
The path wildcard is as follows:

        
          {0} this will be replaced with the result of [instance].getPath(). For example: jsxtext
     * @param TEMPLATE Either a valid xsl:template or a named system template, including: @default, @empty, @unescape, and @image
     */
    public void setValueTemplate(String TEMPLATE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValueTemplate", TEMPLATE);
        getScriptProxy().addScript(script);
    }

    /**
     * Gets whether or not this column can be resized by the user. If not set, the column will be assumed resizable
     */
    @SuppressWarnings("unchecked")
    public void getResizable(org.directwebremoting.proxy.Callback<Boolean> callback)
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
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets whether or not this column can be resized by the user. If not set, the column will be assumed resizable. Note that if the parent Matrix
is set as NOT resizable, this setting is ignored and no child columns can be resized. Note that the header row is immediately repainted to reflect the change.
     * @param RESIZE 
     */
    public void setResizable(Boolean RESIZE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setResizable", RESIZE);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns one or more named attributes. When one of these attributes is updated by another column's edit mask iterface,
this column will called to repaint to reflect the updated value
     * @param callback Comma-delimited attribute list
     */
    @SuppressWarnings("unchecked")
    public void getTriggers(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTriggers");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets one or more named attributes. When one of these attributes is updated by another column's edit mask iterface,
this column will called to repaint to reflect the updated value
     * @param strTriggers Comma-delimited attribute list. For example: <code>jsxtext, ssn, phone</code>.
     */
    public void setTriggers(String strTriggers)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setTriggers", strTriggers);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the selection path for this column of data. Returns 'jsxid' if no path specified
     * @param callback selection path
     */
    @SuppressWarnings("unchecked")
    public void getPath(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPath");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the selection path for this column of data.
     * @param strPath The name of the attribute For example <code>jsxtext</code>
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the XSLT (such as path) require that the XSLT be regenerated.
However, the repaint can be suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     */
    public void setPath(String strPath, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPath", strPath, bSuppressRepaint);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the CDF attribute to use to sort on this column. If the sort path has not been set explicitly, this method
returns the value of this.getPath(). The data source of the matrix containing this column is
sorted on this attribute when the matrix is sorted on this column.
     */
    @SuppressWarnings("unchecked")
    public void getSortPath(org.directwebremoting.proxy.Callback<String> callback)
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
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the CDF attribute to use to sort on this column.
     * @param strPath 
     */
    public void setSortPath(String strPath)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSortPath", strPath);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the data type for this column of data (affects sorting if this column is used for sorting the data); valid types include: jsx3.gui.Matrix.Column.TYPE_TEXT and jsx3.gui.Matrix.Column.TYPE_NUMBER
     * @param callback data type for this column's data
     */
    @SuppressWarnings("unchecked")
    public void getDataType(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDataType");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the data type for this column of data (affects sorting if this column is used for sorting the data); returns ref to self
     * @param DATATYPE one of: jsx3.gui.Matrix.Column.TYPE_TEXT, jsx3.gui.Matrix.Column.TYPE_NUMBER
     */
    public void setDataType(String DATATYPE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDataType", DATATYPE);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the data type for this column of data (affects sorting if this column is used for sorting the data).
     * @param callback one of: jsx3.gui.Matrix.Column.TYPE_TEXT, jsx3.gui.Matrix.Column.TYPE_NUMBER
     */
    @SuppressWarnings("unchecked")
    public void getSortDataType(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getSortDataType");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the data type for this column of data (affects sorting if this column is used for sorting the data); returns ref to self
     * @param DATATYPE data type for this column's data. valid types include: jsx3.gui.Matrix.Column.TYPE_TEXT and jsx3.gui.Matrix.Column.TYPE_NUMBER
     */
    public void setSortDataType(String DATATYPE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setSortDataType", DATATYPE);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns whether the parent list/grid can be sorted on this column. If no value is provided, the column is assumed sortable unless
the parent control explicitly specifies that no column should sort.
     */
    @SuppressWarnings("unchecked")
    public void getCanSort(org.directwebremoting.proxy.Callback<Boolean> callback)
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
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets whether the parnet list/grid can be sorted on this column. Note that the header row is immediately repainted to reflect the change.
     * @param SORT 
     */
    public void setCanSort(Boolean SORT)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCanSort", SORT);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the identifier for which of the default column formatters should be implemented. A function literal can also be passed.
     * @param handler including @unescape, @lookup, @message,  @datetime, @date, @time, and @number. For example: <code>@unescape</code>.
<p><b>- or -</b></p>
Function literal with the signature, <code>function(element,cdfkey, matrix, column, rownumber,server)</code>. For example:
<p><pre>
function(element, cdfkey, matrix, column, rownumber, server) {
  var mf = new jsx3.util.MessageFormat("{0,number,currency}");
  element.innerHTML = mf.format(element.innerHTML);
};
</pre></p>
     */
    public void setFormatHandler(String handler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFormatHandler", handler);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the identifier for which of the default column formatters should be implemented. A function literal can also be passed.
     * @param handler including @unescape, @lookup, @message,  @datetime, @date, @time, and @number. For example: <code>@unescape</code>.
<p><b>- or -</b></p>
Function literal with the signature, <code>function(element,cdfkey, matrix, column, rownumber,server)</code>. For example:
<p><pre>
function(element, cdfkey, matrix, column, rownumber, server) {
  var mf = new jsx3.util.MessageFormat("{0,number,currency}");
  element.innerHTML = mf.format(element.innerHTML);
};
</pre></p>
     */
    public void setFormatHandler(org.directwebremoting.proxy.CodeBlock handler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFormatHandler", handler);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the identifier for which of the default column formatters should be implemented. A function literal can also be passed.
     * @param handler including @unescape, @lookup, @message,  @datetime, @date, @time, and @number. For example: <code>@unescape</code>.
<p><b>- or -</b></p>
Function literal with the signature, <code>function(element,cdfkey, matrix, column, rownumber,server)</code>. For example:
<p><pre>
function(element, cdfkey, matrix, column, rownumber, server) {
  var mf = new jsx3.util.MessageFormat("{0,number,currency}");
  element.innerHTML = mf.format(element.innerHTML);
};
</pre></p>
     */
    public void setFormatHandler(jsx3.gui.matrix.ColumnFormat handler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFormatHandler", handler);
        getScriptProxy().addScript(script);
    }

    /**
     * Gets the named object that will handle the reformatting of a given column's data cells. This object should
implment the interface, jsx3.gui.Matrix.ColumnFormat, or adhere to its APIs.
Can also return the function literal
     * @param callback named object or function literal
     */
    @SuppressWarnings("unchecked")
    public void getFormatHandler(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFormatHandler");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the zero-based index for ths column in relation to its siblings.  This is different from getChildIndex in that
it corresponds to the position of this column as rendered on-screen, meaning if a child of a lesser index is not
displayed (e.g., display = none), the value returned from this method will be less than what would be returned by getChildIndex.
Returns null if this object is not displayed.
     */
    @SuppressWarnings("unchecked")
    public void getDisplayIndex(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDisplayIndex");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns CSS property value for the data cell background-color.
     */
    @SuppressWarnings("unchecked")
    public void getCellBackgroundColor(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellBackgroundColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets CSS property value for the data cell background-color. Call repaint on the parent instance to update the view.
     * @param strColor valid CSS property value, (e.g., red, #ff0000, rgb(255,0,0))
     */
    public void setCellBackgroundColor(String strColor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellBackgroundColor", strColor);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns CSS property value for the data cell border.
     */
    @SuppressWarnings("unchecked")
    public void getCellBorder(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellBorder");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets CSS property value(s) for a border for the data cells. Updates MODEL and VIEW (unless repaint is suppressed).
     * @param strCSS valid CSS property value for border. For example: <code>solid 1px red;solid 0px;solid 0px;solid 1px white</code>
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the browser-specific box model (such as borders) are repainted
immediately to keep the box model abstraction in synch with the native view. However, the repaint can be
suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     */
    public void setCellBorder(String strCSS, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellBorder", strCSS, bSuppressRepaint);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns CSS property value for the data cell color.
     */
    @SuppressWarnings("unchecked")
    public void getCellColor(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellColor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets CSS property value for the data cell color. Call repaint on the parent instance to update the view.
     * @param strColor valid CSS property value, (e.g., red, #ffffff, rgb(255,0,0))
     */
    public void setCellColor(String strColor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellColor", strColor);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns CSS property value for the data cell cursor.
     */
    @SuppressWarnings("unchecked")
    public void getCellCursor(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellCursor");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets CSS property value for the data cell cursor. Call repaint on the parent instance to update the view.
     * @param strCursor CSS property value, (e.g., default, wait, col-resize)
     */
    public void setCellCursor(String strCursor)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellCursor", strCursor);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the CSS property value for the data cell font-family.
     */
    @SuppressWarnings("unchecked")
    public void getCellFontName(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellFontName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the CSS property value for the data cell font-family. Call repaint on the parent instance to update the view.
     * @param strFontName valid CSS font-family property value (e.g., Arial, Courier)
     */
    public void setCellFontName(String strFontName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellFontName", strFontName);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the CSS property value for the data cell font-size.
     */
    @SuppressWarnings("unchecked")
    public void getCellFontSize(org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellFontSize");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the CSS property value for the data cell font-size. Call repaint on the parent instance to update the view.
     * @param intPixelSize font-size (in pixels)
     */
    public void setCellFontSize(int intPixelSize)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellFontSize", intPixelSize);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the CSS property value for the data cell font-weight.
     */
    @SuppressWarnings("unchecked")
    public void getCellFontWeight(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellFontWeight");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the CSS property value for the data cell font-weight. Call repaint on the parent instance to update the view.
     * @param FONTWEIGHT one of: <code>jsx3.gui.Block.FONTBOLD</code>, <code>jsx3.gui.Block.FONTNORMAL</code>
     */
    public void setCellFontWeight(String FONTWEIGHT)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellFontWeight", FONTWEIGHT);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the CSS property value for the data cell padding.
     */
    @SuppressWarnings("unchecked")
    public void getCellPadding(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellPadding");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the CSS property value for the data cell padding. Updates MODEL and VIEW (unless repaint is suppressed).
     * @param strCSS valid CSS property value for padding. For example: <code>8 4 8 4</code>
     * @param bSuppressRepaint Pass <code>true</code> to stop the default repaint from occurring.
Typically property updates that affect the browser-specific box model (such as padding) are repainted
immediately to keep the box model abstraction in synch with the native view. However, the repaint can be
suppressed to avoid unnecessary reparsing of the XSLT during repeated property updates.
     */
    public void setCellPadding(String strCSS, boolean bSuppressRepaint)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellPadding", strCSS, bSuppressRepaint);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the CSS property value for the data cell text-align.
     */
    @SuppressWarnings("unchecked")
    public void getCellTextAlign(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellTextAlign");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the CSS property value for the data cell text-align. Call repaint on the parent instance to update the view.
     * @param ALIGN one of: <code>jsx3.gui.Block.ALIGNLEFT</code>, <code>jsx3.gui.Block.ALIGNRIGHT</code>, <code>jsx3.gui.Block.ALIGNCENTER</code>
     */
    public void setCellTextAlign(String ALIGN)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellTextAlign", ALIGN);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the CSS property value for the data cell vertical-align. If no value is provided, the data cells render top-aligned.
     */
    @SuppressWarnings("unchecked")
    public void getCellVAlign(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellVAlign");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets  the CSS property value for the data cell vertical-align. Call repaint on the parent instance to update the view.
     * @param VALIGN valid CSS value for vertical-align style.
     */
    public void setCellVAlign(String VALIGN)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellVAlign", VALIGN);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns whether or not the data cellc will support text-wrapping. If no value is specified, the text will not wrap
     * @param strDefault The default value to use if null
     */
    @SuppressWarnings("unchecked")
    public void getCellWrap(String strDefault, org.directwebremoting.proxy.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getCellWrap", strDefault);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets whether or not the data cellc will support text-wrapping. If no value is
specified, the text will not wrap. Call repaint to update the VIEW
     * @param WRAP <code>jsx3.Boolean.TRUE</code> or <code>jsx3.Boolean.FALSE</code>
     */
    public void setCellWrap(int WRAP)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setCellWrap", WRAP);
        getScriptProxy().addScript(script);
    }

    /**
     * Gets whether or not the header cell will support text-wrapping. If not specified, the cell will be painted with no wrapping.
     * @param strDefault The default value to use if null
     */
    @SuppressWarnings("unchecked")
    public void getWrap(String strDefault, org.directwebremoting.proxy.Callback<Boolean> callback)
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
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets whether or not the header cell will support text-wrapping. Repaints the header to immediately reflect this change.
     * @param WRAP 
     */
    public void setWrap(Boolean WRAP)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWrap", WRAP);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the CSS property value for the header cell vertical-align. If no value is provided, the header cell render top-aligned.
     */
    @SuppressWarnings("unchecked")
    public void getVAlign(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getVAlign");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the CSS property value for the header cell vertical-align. Repaints the header to immediately reflect this change.
     * @param VALIGN valid CSS value for vertical-align style.
     */
    public void setVAlign(String VALIGN)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setVAlign", VALIGN);
        getScriptProxy().addScript(script);
    }

}

