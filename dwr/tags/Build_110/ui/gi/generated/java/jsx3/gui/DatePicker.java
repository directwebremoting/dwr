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
 * A form element that allows for the selection of an arbitrary date by showing a navigable calendar.

This control is localized to the server locale. Users with computers set to other locales may see a version of the
control appropriate for their locale.

This class uses the jsx3.util.DateFormat class to both format and parse dates. Dates are formatted
before they are displayed in the text box of the date picker. Dates are parsed after a user manually changes the
text in the text box. Parsing with DateFormat is fairly strict. If parsing fails, the value of
the date picker is unchanged and the displayed value refreshes.

The calendar calculates dates in the time of the local machine, not UTC (Greenwich Mean Time). Date values chosen
with the calendar are always 0h:00m local time.

Date pickers publish the following model events:


        CHANGE Ñ when the value of the control is changed by the user selecting a date in the
  calendar or editing the formatted date in the text box.

        SHOW Ñ whenever the calendar is shown.

        HIDE Ñ whenever the calendar is hidden.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class DatePicker extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public DatePicker(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, String intLeft, String intTop, int intWidth, int intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, int intLeft, String intTop, int intWidth, int intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, String intLeft, int intTop, int intWidth, String intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, int intLeft, String intTop, String intWidth, int intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, String intLeft, String intTop, String intWidth, int intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, String intLeft, int intTop, String intWidth, int intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, int intLeft, String intTop, String intWidth, String intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, int intLeft, int intTop, int intWidth, String intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, String intLeft, int intTop, String intWidth, String intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, String intLeft, int intTop, int intWidth, int intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, int intLeft, String intTop, int intWidth, String intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, int intLeft, int intTop, String intWidth, String intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, String intLeft, String intTop, int intWidth, String intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, String intLeft, String intTop, String intWidth, String intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, int intLeft, int intTop, int intWidth, int intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }

    /**
     * The instance initializer.
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param intLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param intHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     */
    public DatePicker(String strName, int intLeft, int intTop, String intWidth, int intHeight)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new DatePicker", strName, intLeft, intTop, intWidth, intHeight);
        setInitScript(script);
    }



    /**
     * Shows the calendar for this date picker and places focus within the calendar as though the user had clicked
on the show calendar button. The calendar hides automatically when focus leaves the calendar.
     */
    public void focusCalendar()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "focusCalendar");
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the jsxformat field
     * @param callback jsxformat
     */

    public void getFormat(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFormat");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the format of this date picker. The format should conform to the syntax of jsx3.util.DatePicker.
The provided format may also be an integer, in which case it is intepreted as one of of the fields of
DatePicker - SHORT, MEDIUM, LONG, or FULL - and
the displayed format will be localized accordingly.
     * @param jsxformat the new format.
     */
    public void setFormat(int jsxformat)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFormat", jsxformat);
        ScriptSessions.addScript(script);
    }

    /**
     * Sets the format of this date picker. The format should conform to the syntax of jsx3.util.DatePicker.
The provided format may also be an integer, in which case it is intepreted as one of of the fields of
DatePicker - SHORT, MEDIUM, LONG, or FULL - and
the displayed format will be localized accordingly.
     * @param jsxformat the new format.
     */
    public void setFormat(String jsxformat)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFormat", jsxformat);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the jsxfirstweekday field
     * @param callback jsxfirstweekday
     */

    public void getFirstDayOfWeek(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getFirstDayOfWeek");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the jsxfirstweekday field
     * @param jsxfirstweekday the new value for jsxfirstweekday
     */
    public void setFirstDayOfWeek(int jsxfirstweekday)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFirstDayOfWeek", jsxfirstweekday);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the current value of this form field as a JavaScript Date object.
     */

    public void getDate(org.directwebremoting.ui.Callback<java.util.Date> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDate");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, java.util.Date.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the value of this form field (the string displayed in the text box).
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
     * set the value of this form field
     * @param date may be null to clear the value of the form field
     */
    public void setDate(java.util.Date date)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDate", date);
        ScriptSessions.addScript(script);
    }

    /**
     * 
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
     * Sets the value of this control.
     * @param vntValue string/int value for the component
     * @return this object.
     */

    public jsx3.gui.Form setValue(Integer vntValue)
    {
        String extension = "setValue(\"" + vntValue + "\").";
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
     * Sets the value of this control.
     * @param vntValue string/int value for the component
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setValue(Integer vntValue, Class<T> returnType)
    {
        String extension = "setValue(\"" + vntValue + "\").";
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
     * Sets the value of this control.
     * @param vntValue string/int value for the component
     * @return this object.
     */

    public jsx3.gui.Form setValue(String vntValue)
    {
        String extension = "setValue(\"" + vntValue + "\").";
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
     * Sets the value of this control.
     * @param vntValue string/int value for the component
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T setValue(String vntValue, Class<T> returnType)
    {
        String extension = "setValue(\"" + vntValue + "\").";
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

