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
package jsx3.html;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * Represents an HTML element. Provides an object oriented way of painting to screen.

This class is available only when the Charting add-in is enabled.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Tag extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public Tag(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param strTagNS 
     * @param strTagName 
     */
    public Tag(String strTagNS, String strTagName)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Tag", strTagNS, strTagName);
        setInitScript(script);
    }



    /**
     * Sdds a child to the list of this tag's children; may be vetoed by onAppendChild().
     * @param child the child to add, must not already have a parent
     */
    public void appendChild(jsx3.html.Tag child)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "appendChild", child);
        getScriptProxy().addScript(script);
    }

    /**
     * Removes a child from the list of this tag's children; may be vetoed by onRemoveChild().
     * @param child the child to remove, must exist in the list of children
     */
    public void removeChild(jsx3.html.Tag child)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "removeChild", child);
        getScriptProxy().addScript(script);
    }

    /**
     * Replaces a child of this tag.
     * @param child the new child.
     * @param oldChild the child to replace.
     */
    public void replaceChild(jsx3.html.Tag child, jsx3.html.Tag oldChild)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "replaceChild", child, oldChild);
        getScriptProxy().addScript(script);
    }

    /**
     * Removes all the children of this tag.
     */
    public void removeChildren()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "removeChildren");
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the parent tag.
     * @return parent
     */
    @SuppressWarnings("unchecked")
    public jsx3.html.Tag getParent()
    {
        String extension = "getParent().";
        try
        {
            java.lang.reflect.Constructor<jsx3.html.Tag> ctor = jsx3.html.Tag.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.html.Tag.class.getName());
        }
    }

    /**
     * Returns the parent tag.
     * @param returnType The expected return type
     * @return parent
     */
    @SuppressWarnings("unchecked")
    public <T> T getParent(Class<T> returnType)
    {
        String extension = "getParent().";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns the children tags.
     * @param callback children
     */
    @SuppressWarnings("unchecked")
    public void getChildren(org.directwebremoting.proxy.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getChildren");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the id field.
     * @param callback id
     */
    @SuppressWarnings("unchecked")
    public void getId(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getId");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the id field.
     * @param id the new value for id
     */
    public void setId(String id)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setId", id);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the cssClass field.
     * @param callback cssClass
     */
    @SuppressWarnings("unchecked")
    public void getClassName(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getClassName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets the cssClass field, the HTML 'class' attribute.
     * @param cssClass the new value for cssClass
     */
    public void setClassName(String cssClass)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setClassName", cssClass);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the extraStyles field, this string is prepended as-is to the generated value for the style attribute of the tag.
     * @param extraStyles the new value for extraStyles
     */
    public void setExtraStyles(String extraStyles)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setExtraStyles", extraStyles);
        getScriptProxy().addScript(script);
    }

    /**
     * Releases all bi-directional references between this instance and its children.
     */
    public void release()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "release");
        getScriptProxy().addScript(script);
    }

    /**
     * Called before appending a child.
     * @param child 
     * @param callback <code>true</code> to allow the append, <code>false</code> to veto.
     */
    @SuppressWarnings("unchecked")
    public void onAppendChild(jsx3.html.Tag child, org.directwebremoting.proxy.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "onAppendChild", child);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Called before removing a child.
     * @param child 
     * @param callback <code>true</code> to allow the removal, <code>false</code> to veto.
     */
    @SuppressWarnings("unchecked")
    public void onRemoveChild(jsx3.html.Tag child, org.directwebremoting.proxy.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "onRemoveChild", child);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Sets an attribute of this HTML element. This method may be called with a variable number of arguments, which are
interpreted as name/value pairs, i.e.: tag.setProperty(n1, p1, n2, p2);.
     * @param strName the name of the attribute.
     * @param strValue the value of the attribute. If <code>null</code>, the attribute is removed.
     */
    public void setProperty(String strName, String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setProperty", strName, strValue);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns an attribute of this HTML element.
     * @param strName the name of the attribute.
     * @param callback the value of the attribute.
     */
    @SuppressWarnings("unchecked")
    public void getProperty(String strName, org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getProperty", strName);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Removes any number of properties from this HTML element.
     * @param strName the names of the attributes.
     */
    public void removeProperty(String strName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "removeProperty", strName);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets a style of this HTML element. This method may be called with a variable number of arguments, which are
interpreted as name/value pairs, i.e.: tag.setStyle(n1, s1, n2, s2);.
     * @param strName the name of the style.
     * @param strValue the value of the style.
     */
    public void setStyle(String strName, String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStyle", strName, strValue);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns a style of this HTML element.
     * @param strName the name of the style.
     * @param callback the value of the style.
     */
    @SuppressWarnings("unchecked")
    public void getStyle(String strName, org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getStyle", strName);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Removes any number of styles from this HTML element.
     * @param strName the names of the styles.
     */
    public void removeStyle(String strName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "removeStyle", strName);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the name of this HTML element, such as "table" or "div".
     * @param callback the tag name
     */
    @SuppressWarnings("unchecked")
    public void getTagName(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTagName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Returns the namespace of this HTML element.
     * @param callback the tag name
     */
    @SuppressWarnings("unchecked")
    public void getTagNS(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTagNS");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Serializes this HTML element to an HTML string using various overridable methods in this class.
This method is only available in the VML version of this class.
     * @param callback this tag serialized to HTML.
     */
    @SuppressWarnings("unchecked")
    public void paint(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "paint");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Prepares this HTML element for insertion into the live browser DOM and returns the underlying native HTML element.
This method is only available in the SVG version of this class.
     * @param callback the native browser html element.
     */
    @SuppressWarnings("unchecked")
    public void paintDom(org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "paintDom");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * This method is called on each HTML tag before it is painted to screen. Methods in subclasses of this class that
override this method should begin with a call to jsxsuper().
     */
    public void paintUpdate()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "paintUpdate");
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the first child tag of type type.
     * @param type the fully-qualified class name or the class constructor function.
     */
    @SuppressWarnings("unchecked")
    public jsx3.html.Tag getFirstChildOfType(String type)
    {
        String extension = "getFirstChildOfType(\"" + type + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.html.Tag> ctor = jsx3.html.Tag.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.html.Tag.class.getName());
        }
    }

    /**
     * Returns the first child tag of type type.
     * @param type the fully-qualified class name or the class constructor function.
     * @param returnType The expected return type
     */
    @SuppressWarnings("unchecked")
    public <T> T getFirstChildOfType(String type, Class<T> returnType)
    {
        String extension = "getFirstChildOfType(\"" + type + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    /**
     * Returns the first child tag of type type.
     * @param type the fully-qualified class name or the class constructor function.
     */
    @SuppressWarnings("unchecked")
    public jsx3.html.Tag getFirstChildOfType(org.directwebremoting.proxy.CodeBlock type)
    {
        String extension = "getFirstChildOfType(\"" + type + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.html.Tag> ctor = jsx3.html.Tag.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.html.Tag.class.getName());
        }
    }

    /**
     * Returns the first child tag of type type.
     * @param type the fully-qualified class name or the class constructor function.
     * @param returnType The expected return type
     */
    @SuppressWarnings("unchecked")
    public <T> T getFirstChildOfType(org.directwebremoting.proxy.CodeBlock type, Class<T> returnType)
    {
        String extension = "getFirstChildOfType(\"" + type + "\").";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

}

