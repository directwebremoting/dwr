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
 * This jsx3.gui.TextBox class allows integration of a standard HTML text input into the JSX DOM. This allows the text box to be treated like a JSX object, including positioning, serialization, updates, and data mapping/binding, etc.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class TextBox extends jsx3.gui.Block
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public TextBox(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, String vntLeft, int vntTop, int vntWidth, String vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, String vntLeft, int vntTop, String vntWidth, int vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, int vntLeft, String vntTop, int vntWidth, String vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, String vntLeft, String vntTop, String vntWidth, int vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, int vntLeft, int vntTop, String vntWidth, int vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, String vntLeft, String vntTop, String vntWidth, String vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, int vntLeft, String vntTop, int vntWidth, int vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, String vntLeft, String vntTop, int vntWidth, int vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, int vntLeft, String vntTop, String vntWidth, String vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, int vntLeft, int vntTop, int vntWidth, String vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, String vntLeft, int vntTop, String vntWidth, String vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, int vntLeft, String vntTop, String vntWidth, int vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, String vntLeft, int vntTop, int vntWidth, int vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, int vntLeft, int vntTop, String vntWidth, String vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, int vntLeft, int vntTop, int vntWidth, int vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }

    /**
     * instance initializer
     * @param strName unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param vntLeft either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntTop either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntWidth either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param vntHeight either a number (i.e, 12, 30, etc) or a number and a unit value (i.e., "25%", "36pt", etc); if a number is passed, pixels will be the assumed unit when painted to screen
     * @param strValue this value to appear in the textbox/textarea. This value will be set as the defaultValue for the text box when it is initialized; if edits are made by the user, these edits can be accessed via [object].getValue(); if the initial value is needed, use [object].getDefaultValue();
     * @param TYPE one of two valid types: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA. If null is passed, jsx3.gui.TextBox.DEFAULTTYPE is used
     */
    public TextBox(String strName, String vntLeft, String vntTop, int vntWidth, String vntHeight, String strValue, String TYPE)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new TextBox", strName, vntLeft, vntTop, vntWidth, vntHeight, strValue, TYPE);
        setInitScript(script);
    }


    /**
     * texbox type 0
     */
    public static final int TYPETEXT = 0;

    /**
     * text area type 1
     */
    public static final int TYPETEXTAREA = 1;

    /**
     * password type 2
     */
    public static final int TYPEPASSWORD = 2;

    /**
     * wrap text (default)
     */
    public static final int WRAPYES = 1;

    /**
     * no wrap
     */
    public static final int WRAPNO = 0;

    /**
     * none
     */
    public static final String OVERFLOWNORMAL = "";

    /**
     * as needed by content
     */
    public static final String OVERFLOWAUTO = "auto";

    /**
     * CSS color property
     */
    public static final String DEFAULTBACKGROUNDCOLOR = "#ffffff";

    /**
     * none
     */
    public static final String VALIDATIONNONE = "none";

    /**
     * us ssn
     */
    public static final String VALIDATIONSSN = "ssn";

    /**
     * telephone
     */
    public static final String VALIDATIONPHONE = "phone";

    /**
     * email
     */
    public static final String VALIDATIONEMAIL = "email";

    /**
     * numbers only
     */
    public static final String VALIDATIONNUMBER = "number";

    /**
     * letter
     */
    public static final String VALIDATIONLETTER = "letter";

    /**
     * uszip
     */
    public static final String VALIDATIONUSZIP = "uszip";

    /**
     * jsx30textbox
     */
    public static final String DEFAULTCLASSNAME = "jsx30textbox";


    /**
     * if TYPE for the object is jsx3.gui.TextBox.TYPETEXT, and this returns a positive integer, the maxlen property for the textbox will be set to this value; returns null if no value found
     * @param callback positive integer
     */

    public void getMaxLength(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMaxLength");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * if TYPE for the object is jsx3.gui.TextBox.TYPETEXT, setting this to a positive integer affects the maxlen property for the textbox;
         returns a reference to self to facilitate method chaining
     * @param intMaxLength positive integer, maxlen for the number of characters accepted by the textbox
     * @return this object
     */
    public jsx3.gui.TextBox setMaxLength(int intMaxLength)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMaxLength", intMaxLength);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the type of TextBox. Default: jsx3.gui.TextBox.TYPETEXT
     * @param callback one of: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA, jsx3.gui.TextBox.TYPEPASSWORD
     */

    public void getType(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getType");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the type of jsx3.gui.TextBox
         returns a reference to self to facilitate method chaining
     * @param TYPE one of: jsx3.gui.TextBox.TYPETEXT, jsx3.gui.TextBox.TYPETEXTAREA, jsx3.gui.TextBox.TYPEPASSWORD
     * @return this object
     */
    public jsx3.gui.TextBox setType(int TYPE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setType", TYPE);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * logically tries to determine the value for the text box by 1) checking for value of on-screen VIEW; 2) checking for 'value' property for in-memory MODEL 3) checking getDefaultValue() for value when object was first initialized. Default: [empty string]
     * @param callback value for object
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
     * Returns value of textbox object when it was first initialized. Default: [empty string]
     * @param callback default value for object
     */

    public void getDefaultValue(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDefaultValue");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * updates value property for both on-screen VIEW (if object is painted) and in-memory MODEL;
           returns a reference to self to facilitate method chaining
     * @param strValue value for the textbox/textarea
     * @return this object
     */
    public jsx3.gui.TextBox setValue(String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValue", strValue);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * set during object initialization; useful for tracking edits/updates to an object by doing a string comparison between getValue() and getDefaultValue();
           returns a reference to self to facilitate method chaining
     * @param strValue default value for the textbox/textarea
     * @return this object
     */
    public jsx3.gui.TextBox setDefaultValue(String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDefaultValue", strValue);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the text-wrapping/ word-breaking property for object if object type is jsx3.gui.TextBox.TYPETEXTAREA. Default: jsx3.gui.TextBox.WRAPYES
     * @param callback one of: jsx3.gui.TextBox.WRAPYES jsx3.gui.TextBox.WRAPNO
     */

    public void getWrap(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getWrap");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the text-wrapping/ word-breaking property for object if object type is jsx3.gui.TextBox.TYPETEXTAREA.
     * @param bWrap one of: jsx3.gui.TextBox.WRAPYES jsx3.gui.TextBox.WRAPNO
     * @return this object.
     */
    public jsx3.gui.TextBox setWrap(boolean bWrap)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setWrap", bWrap);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the ID (CONSTANT) for one of the pre-canned regular expression filters that can be used to validate the user's response. Default: jsx3.gui.TextBox.VALIDATIONNONE
     * @param callback one of: jsx3.gui.TextBox.VALIDATIONNONE, jsx3.gui.TextBox.VALIDATIONSSN, jsx3.gui.TextBox.VALIDATIONPHONE, jsx3.gui.TextBox.VALIDATIONEMAIL, jsx3.gui.TextBox.VALIDATIONNUMBER, jsx3.gui.TextBox.VALIDATIONLETTER, jsx3.gui.TextBox.VALIDATIONUSZIP
     */

    public void getValidationType(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getValidationType");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * when set, applies one of the pre-canned regular expression filters that can be used to validate the user's response;
           returns a reference to self to facilitate method chaining.
     * @param VALIDATIONTYPE one of: jsx3.gui.TextBox.VALIDATIONNONE, jsx3.gui.TextBox.VALIDATIONSSN, jsx3.gui.TextBox.VALIDATIONPHONE, jsx3.gui.TextBox.VALIDATIONEMAIL, jsx3.gui.TextBox.VALIDATIONNUMBER, jsx3.gui.TextBox.VALIDATIONLETTER, jsx3.gui.TextBox.VALIDATIONUSZIP
     * @return this object
     */
    public jsx3.gui.TextBox setValidationType(String VALIDATIONTYPE)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValidationType", VALIDATIONTYPE);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Returns the custom validation expression (a regular expression pattern to match). If this method returns
a valid regular expression (as a string), it will be applied instead of the pre-canned regular expression. Default: null
returned by the method, getValidationType; null is returned if the expression is not found
     * @param callback valid regular expression such as ^\d{3}-\d{2}-\d{4}$
     */

    public void getValidationExpression(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getValidationExpression");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the custom validation expression (a regular expression pattern to match).
             returns a reference to self to facilitate method chaining.
     * @param strValidationExpression valid regular expression such as ^\d{3}-\d{2}-\d{4}$
             If null is passed custom value is removed and [object].getValidationType() is used instead
     * @return this object
     */
    public jsx3.gui.TextBox setValidationExpression(String strValidationExpression)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setValidationExpression", strValidationExpression);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * validates form field, ensuring it contains the correct data set
     * @param callback true if field contains a valid value given @VALIDATIONTYPE
     */

    public void doValidate(org.directwebremoting.ui.Callback<Boolean> callback)
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
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * call to designate an error or alert the user's attention to the textbox on-screen. Causes the textbox to 'flash/blink'
     */
    public void beep()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "beep");
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

}

