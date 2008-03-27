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
package jsx3.xml;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * Wrapper of the native browser XSLT processor.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Template extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public Template(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param objXSL 
     */
    public Template(jsx3.xml.CdfDocument objXSL)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Template", objXSL);
        setInitScript(script);
    }



    /**
     * 
     * @param strName 
     * @param objValue 
     */
    public void setParam(String strName, jsx3.lang.Object objValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setParam", strName, objValue);
        getScriptProxy().addScript(script);
    }

    /**
     * 
     */
    public void reset()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "reset");
        getScriptProxy().addScript(script);
    }

    /**
     * 
     * @param objParams JavaScript object array of name/value pairs. If this parameter is
   not empty, the transformation will use a paramaterized stylesheet to perform the transformation.
     */
    public void setParams(jsx3.lang.Object objParams)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setParams", objParams);
        getScriptProxy().addScript(script);
    }

    /**
     * Performs an XSLT merge. If an error occurs while performing the transform, this method sets the error
property of this processor and returns null.
     * @param objXML 
     * @param bObject 
     * @param callback the result of the transformation
     */
    @SuppressWarnings("unchecked")
    public void transform(jsx3.xml.Node objXML, boolean bObject, org.directwebremoting.proxy.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "transform", objXML, bObject);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

    /**
     * Performs an XSLT merge. If an error occurs while performing the transform, this method sets the error
property of this processor and returns null.
     * @param objXML 
     * @return if a valid result tree is formed as a result of the transformation
     */
    @SuppressWarnings("unchecked")
    public jsx3.xml.CdfDocument transformToObject(jsx3.xml.Node objXML)
    {
        String extension = "transformToObject(\"" + objXML + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.xml.CdfDocument> ctor = jsx3.xml.CdfDocument.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.xml.CdfDocument.class.getName());
        }
    }

    /**
     * Performs an XSLT merge. If an error occurs while performing the transform, this method sets the error
property of this processor and returns null.
     * @param objXML 
     * @param returnType The expected return type
     * @return if a valid result tree is formed as a result of the transformation
     */
    @SuppressWarnings("unchecked")
    public <T> T transformToObject(jsx3.xml.Node objXML, Class<T> returnType)
    {
        String extension = "transformToObject(\"" + objXML + "\").";
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
     * Returns an error object (a plain JavaScript object) with two properties that the developer can query for:

code � an integer error code, 0 for no error.
description � a text description of the error that occurred.
     */
    @SuppressWarnings("unchecked")
    public jsx3.lang.Object getError()
    {
        String extension = "getError().";
        try
        {
            java.lang.reflect.Constructor<jsx3.lang.Object> ctor = jsx3.lang.Object.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.lang.Object.class.getName());
        }
    }

    /**
     * Returns an error object (a plain JavaScript object) with two properties that the developer can query for:

code � an integer error code, 0 for no error.
description � a text description of the error that occurred.
     * @param returnType The expected return type
     */
    @SuppressWarnings("unchecked")
    public <T> T getError(Class<T> returnType)
    {
        String extension = "getError().";
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
     * Returns true if the last operation on this XML entity caused an error.
     */
    @SuppressWarnings("unchecked")
    public void hasError(org.directwebremoting.proxy.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "hasError");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }

}

