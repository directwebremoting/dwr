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
package jsx3.vector;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * Represents a vector shape element.

The vector shape is the principal vector tag. The path field can contain an EPS-like path the defines a
complex vector shape.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Shape extends jsx3.vector.Tag
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param context The script that got us to where we are now
     */
    public Shape(Context context, String extension, ScriptProxy scriptProxy)
    {
        super(context, extension, scriptProxy);
    }

    /**
     * The instance initializer.
     * @param strTagName 
     * @param left left position (in pixels) of the object relative to its parent container
     * @param top top position (in pixels) of the object relative to its parent container
     * @param width width (in pixels) of the object
     * @param height height (in pixels) of the object
     */
    public Shape(String strTagName, int left, int top, int width, int height)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Shape", strTagName, left, top, width, height);
        setInitScript(script);
    }



    /**
     * Returns the path field.
     * @param callback path
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
     * Sets the path field.
     * @param path the new value for path
     */
    public void setPath(String path)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPath", path);
        getScriptProxy().addScript(script);
    }

    /**
     * 
     * @param x 
     * @param y 
     * @param bRel 
     * @return this object.
     */
    @SuppressWarnings("unchecked")
    public jsx3.vector.Shape pathMoveTo(int x, int y, boolean bRel)
    {
        String extension = "pathMoveTo(\"" + x + "\", \"" + y + "\", \"" + bRel + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.vector.Shape> ctor = jsx3.vector.Shape.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.vector.Shape.class.getName());
        }
    }

    /**
     * 
     * @param x 
     * @param y 
     * @param bRel 
     * @param returnType The expected return type
     * @return this object.
     */
    @SuppressWarnings("unchecked")
    public <T> T pathMoveTo(int x, int y, boolean bRel, Class<T> returnType)
    {
        String extension = "pathMoveTo(\"" + x + "\", \"" + y + "\", \"" + bRel + "\").";
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
     * 
     * @param x 
     * @param y 
     * @param bRel 
     * @return this object.
     */
    @SuppressWarnings("unchecked")
    public jsx3.vector.Shape pathLineTo(int x, int y, boolean bRel)
    {
        String extension = "pathLineTo(\"" + x + "\", \"" + y + "\", \"" + bRel + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.vector.Shape> ctor = jsx3.vector.Shape.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.vector.Shape.class.getName());
        }
    }

    /**
     * 
     * @param x 
     * @param y 
     * @param bRel 
     * @param returnType The expected return type
     * @return this object.
     */
    @SuppressWarnings("unchecked")
    public <T> T pathLineTo(int x, int y, boolean bRel, Class<T> returnType)
    {
        String extension = "pathLineTo(\"" + x + "\", \"" + y + "\", \"" + bRel + "\").";
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
     * 
     * @param cx 
     * @param cy 
     * @param rx 
     * @param ry 
     * @param x1 
     * @param y1 
     * @param x2 
     * @param y2 
     * @param bCW 
     * @return this object.
     */
    @SuppressWarnings("unchecked")
    public jsx3.vector.Shape pathArcTo(int cx, int cy, int rx, int ry, int x1, int y1, int x2, int y2, boolean bCW)
    {
        String extension = "pathArcTo(\"" + cx + "\", \"" + cy + "\", \"" + rx + "\", \"" + ry + "\", \"" + x1 + "\", \"" + y1 + "\", \"" + x2 + "\", \"" + y2 + "\", \"" + bCW + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.vector.Shape> ctor = jsx3.vector.Shape.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.vector.Shape.class.getName());
        }
    }

    /**
     * 
     * @param cx 
     * @param cy 
     * @param rx 
     * @param ry 
     * @param x1 
     * @param y1 
     * @param x2 
     * @param y2 
     * @param bCW 
     * @param returnType The expected return type
     * @return this object.
     */
    @SuppressWarnings("unchecked")
    public <T> T pathArcTo(int cx, int cy, int rx, int ry, int x1, int y1, int x2, int y2, boolean bCW, Class<T> returnType)
    {
        String extension = "pathArcTo(\"" + cx + "\", \"" + cy + "\", \"" + rx + "\", \"" + ry + "\", \"" + x1 + "\", \"" + y1 + "\", \"" + x2 + "\", \"" + y2 + "\", \"" + bCW + "\").";
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
     * 
     * @return this object.
     */
    @SuppressWarnings("unchecked")
    public jsx3.vector.Shape pathClose()
    {
        String extension = "pathClose().";
        try
        {
            java.lang.reflect.Constructor<jsx3.vector.Shape> ctor = jsx3.vector.Shape.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.vector.Shape.class.getName());
        }
    }

    /**
     * 
     * @param returnType The expected return type
     * @return this object.
     */
    @SuppressWarnings("unchecked")
    public <T> T pathClose(Class<T> returnType)
    {
        String extension = "pathClose().";
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
     * Sets the fill of this shape, other fills may be present as children of this instance.
     * @param fill the fill value.
     */
    public void setFill(jsx3.vector.Fill fill)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setFill", fill);
        getScriptProxy().addScript(script);
    }

    /**
     * Sets the stroke of this shape, other strokes may be present as children of this instance.
     * @param stroke the stroke value.
     */
    public void setStroke(jsx3.vector.Stroke stroke)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setStroke", stroke);
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the fill of this shape.
     */
    public void getFill()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getFill");
        getScriptProxy().addScript(script);
    }

    /**
     * Returns the stroke of this shape.
     */
    public void getStroke()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "getStroke");
        getScriptProxy().addScript(script);
    }

}

