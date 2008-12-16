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
package jsx3.app;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * The abstract base class that defines the JSX DOM. Instances of this class exist as nodes in a tree, each with
a single parent and multiple children. This class includes all the methods for querying and manipulating the DOM's
tree structure, such as getChild(), adoptChild(), getParent(), etc.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Model extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Model(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * The instance initializer.
     * @param strName a unique name distinguishing this object from all other JSX GUI objects in the JSX application
     * @param strInstanceOf
     */
    public Model(String strName, String strInstanceOf)
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Model", strName, strInstanceOf);
        setInitScript(script);
    }


    /**
     * Persistance value fora child that is temporarily part of the DOM tree and will not be persisted.
     */
    public static final int PERSISTNONE = 0;

    /**
     * Normal persistance value for a child that will be persisted.
     */
    public static final int PERSISTEMBED = 1;

    /**
     * Persistance value for a child that exists in an external serialization file and is referenced by URI.
     */
    public static final int PERSISTREF = 2;

    /**
     * Persistance value for a child that exists in an external serialization file and is referenced by URI. The
  loading of a child with this persistence value will happen asynchronously after the file that references it is
  loaded.
     */
    public static final int PERSISTREFASYNC = 3;

    /**
     * The normal load type for a DOM branch. The DOM branch deserializes and paints with its parent.
     */
    public static final int LT_NORMAL = 0;

    /**
     * Load type indicating that the DOM branch will paint after its parent paints and the call stack resets.
     */
    public static final int LT_SLEEP_PAINT = 1;

    /**
     * Load type indicating that the DOM branch will deserialize and paint after its parent deserializes and the
   call stack resets.
     */
    public static final int LT_SLEEP_DESER = 2;

    /**
     * Load type indicating that the DOM branch will deserialize after its parent deserializes and the call stack
   resets and will paint after its parent paints and the call stack resets.
     */
    public static final int LT_SLEEP_PD = 3;

    /**
     * Load type indicating that the DOM branch will paint as needed when it becomes visible. It is left to
   subclasses of Model to implement this functionality.
     */
    public static final int LT_SHOW_PAINT = 4;

    /**
     * Load type indicating that the DOM branch will deserialize and paint as needed when it becomes visible.
   It is left to subclasses of Model to implement this functionality.
     */
    public static final int LT_SHOW_DESER = 5;

    /**
     * Minimum supported version for serialization files
     */
    public static final String CURRENT_VERSION = "urn:tibco.com/v3.0";

    /**
     * Minimum supported version CIF formatted serialization files
     */
    public static final String CIF_VERSION = "http://xsd.tns.tibco.com/gi/cif/2006";

    /**
     * The number of milliseconds before asynchronous component loads time out.
     */
    public static final int ASYNC_LOAD_TIMEOUT = 60000;


    /**
     * Returns the child DOM node of this node at the given index or with the given name. If a name is supplied, the
children are searched in order and the first matching child is returned.
     * @param vntIndexOrName either the integer index or the string name of the child.
     * @return the child at the given index or with the given name, or <code>null</code> if no such
    child exists.
     */

    public jsx3.app.Model getChild(int vntIndexOrName)
    {
        String extension = "getChild(\"" + vntIndexOrName + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the child DOM node of this node at the given index or with the given name. If a name is supplied, the
children are searched in order and the first matching child is returned.
     * @param vntIndexOrName either the integer index or the string name of the child.
     * @param returnType The expected return type
     * @return the child at the given index or with the given name, or <code>null</code> if no such
    child exists.
     */

    public <T> T getChild(int vntIndexOrName, Class<T> returnType)
    {
        String extension = "getChild(\"" + vntIndexOrName + "\").";
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
     * Returns the child DOM node of this node at the given index or with the given name. If a name is supplied, the
children are searched in order and the first matching child is returned.
     * @param vntIndexOrName either the integer index or the string name of the child.
     * @return the child at the given index or with the given name, or <code>null</code> if no such
    child exists.
     */

    public jsx3.app.Model getChild(String vntIndexOrName)
    {
        String extension = "getChild(\"" + vntIndexOrName + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the child DOM node of this node at the given index or with the given name. If a name is supplied, the
children are searched in order and the first matching child is returned.
     * @param vntIndexOrName either the integer index or the string name of the child.
     * @param returnType The expected return type
     * @return the child at the given index or with the given name, or <code>null</code> if no such
    child exists.
     */

    public <T> T getChild(String vntIndexOrName, Class<T> returnType)
    {
        String extension = "getChild(\"" + vntIndexOrName + "\").";
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
     * Returns the first child.
     */

    public jsx3.app.Model getFirstChild()
    {
        String extension = "getFirstChild().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the first child.
     * @param returnType The expected return type
     */

    public <T> T getFirstChild(Class<T> returnType)
    {
        String extension = "getFirstChild().";
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
     * Returns the last child.
     */

    public jsx3.app.Model getLastChild()
    {
        String extension = "getLastChild().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the last child.
     * @param returnType The expected return type
     */

    public <T> T getLastChild(Class<T> returnType)
    {
        String extension = "getLastChild().";
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
     * Returns the next sibling.
     */

    public jsx3.app.Model getNextSibling()
    {
        String extension = "getNextSibling().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the next sibling.
     * @param returnType The expected return type
     */

    public <T> T getNextSibling(Class<T> returnType)
    {
        String extension = "getNextSibling().";
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
     * Returns the previous sibling.
     */

    public jsx3.app.Model getPreviousSibling()
    {
        String extension = "getPreviousSibling().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the previous sibling.
     * @param returnType The expected return type
     */

    public <T> T getPreviousSibling(Class<T> returnType)
    {
        String extension = "getPreviousSibling().";
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
     * Returns an array containing all the child DOM nodes of this object. The return value is the original array rather
than a copy and should not be modified.
     */

    public void getChildren(org.directwebremoting.ui.Callback<Object[]> callback)
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
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the persistence bit for this model object.
     * @param callback one of <code>PERSISTNONE</code>, <code>PERSISTEMBED</code>, <code>PERSISTREF</code>,
   <code>PERSISTREFASYNC</code>.
     */

    public void getPersistence(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getPersistence");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the persistence bit for this model object.
     * @param intPersist one of <code>PERSISTNONE</code>, <code>PERSISTEMBED</code>, <code>PERSISTREF</code>,
   <code>PERSISTREFASYNC</code>.
     * @return this object
     */
    public jsx3.app.Model setPersistence(int intPersist)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setPersistence", intPersist);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Appends a child DOM node to this parent DOM node. If the child already has a parent, adoptChild()
should be used instead to ensure that the child is removed from its current parent.
     * @param objChild the root node of a DOM fragment.
     * @param intPersist defines how the child will be persisted/serialized. The valid values are the four
   persistence values defined as static fields in this class.
     * @param strSourceURL the path to the serialization file where the child exists. This parameter is only
   relevant if the given <code>intPersist</code> is <code>PERSISTREF</code> or <code>PERSISTREFASYNC</code>.
     * @param strNS the namespace of the child to append. This parameter is normally not required but is useful
   when sharing DOM nodes between servers with different namespaces.
     * @return this object or <code>false</code> if the set was vetoed
     */
    public jsx3.app.Model setChild(jsx3.app.Model objChild, int intPersist, java.net.URI strSourceURL, String strNS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setChild", objChild, intPersist, strSourceURL, strNS);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Appends a child DOM node to this parent DOM node. If the child already has a parent, adoptChild()
should be used instead to ensure that the child is removed from its current parent.
     * @param objChild the root node of a DOM fragment.
     * @param intPersist defines how the child will be persisted/serialized. The valid values are the four
   persistence values defined as static fields in this class.
     * @param strSourceURL the path to the serialization file where the child exists. This parameter is only
   relevant if the given <code>intPersist</code> is <code>PERSISTREF</code> or <code>PERSISTREFASYNC</code>.
     * @param strNS the namespace of the child to append. This parameter is normally not required but is useful
   when sharing DOM nodes between servers with different namespaces.
     * @return this object or <code>false</code> if the set was vetoed
     */
    public jsx3.app.Model setChild(jsx3.app.Model objChild, int intPersist, String strSourceURL, String strNS)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setChild", objChild, intPersist, strSourceURL, strNS);
        ScriptSessions.addScript(script);
        return this;
    }

    /**
     * Hook that allows for a prospective parent DOM node to veto the adoption of a child.
     * @param objChild
     * @param callback true to allow the set, false to veto
     */

    public void onSetChild(java.lang.Object objChild, org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "onSetChild", objChild);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Hook that allows for a prospective child DOM node to veto its adoption by a parent. This method is only called if
the prospective parent has not already vetoed the adoption in the onSetChild() method.
     * @param objParent
     * @param callback true to allow the set, false to veto
     */

    public void onSetParent(java.lang.Object objParent, org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "onSetParent", objParent);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Hook that notifies this model object that one of its children has been removed. This hook exists simply to allow
this object to perform cleanup/re-render, and does not provide a veto mechanism. This method is called after
the child has been removed from the model (this.getChildren() does not contain objChild)
and after the child has been removed from the view (objChild.getRendered() is also null).

This method is only called if the child is being removed from the DOM but this object (the parent) is not
being removed. Therefore, this hook cannot be relied upon for garbage collection.

If removeChildren() is called on this object, this hook is called exactly once after all children
have been removed. In that case, the first parameter to this method will be the array of children and the
second parameter will be null.

In general a method overriding this method should begin by calling jsxsuper.
     * @param objChild the child that was removed
     * @param intIndex the index of the removed child
     */
    public void onRemoveChild(Object[] objChild, int intIndex)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onRemoveChild", objChild, intIndex);
        ScriptSessions.addScript(script);
    }

    /**
     * Hook that notifies this model object that one of its children has been removed. This hook exists simply to allow
this object to perform cleanup/re-render, and does not provide a veto mechanism. This method is called after
the child has been removed from the model (this.getChildren() does not contain objChild)
and after the child has been removed from the view (objChild.getRendered() is also null).

This method is only called if the child is being removed from the DOM but this object (the parent) is not
being removed. Therefore, this hook cannot be relied upon for garbage collection.

If removeChildren() is called on this object, this hook is called exactly once after all children
have been removed. In that case, the first parameter to this method will be the array of children and the
second parameter will be null.

In general a method overriding this method should begin by calling jsxsuper.
     * @param objChild the child that was removed
     * @param intIndex the index of the removed child
     */
    public void onRemoveChild(jsx3.app.Model objChild, int intIndex)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onRemoveChild", objChild, intIndex);
        ScriptSessions.addScript(script);
    }

    /**
     * Removes a DOM child from this object. This method removes the on-screen DHTML of the removed object. The removed
child will be completely derefenced from the DOM and will be prepped for garbage collection. If a DOM child must
continue to exist after removing it from this parent, adoptChild() should be used instead of this
method.
     * @param vntItem either the index of the child to remove or the child itself.
     * @return this object
     */

    public jsx3.app.Model removeChild(jsx3.app.Model vntItem)
    {
        String extension = "removeChild(\"" + vntItem + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Removes a DOM child from this object. This method removes the on-screen DHTML of the removed object. The removed
child will be completely derefenced from the DOM and will be prepped for garbage collection. If a DOM child must
continue to exist after removing it from this parent, adoptChild() should be used instead of this
method.
     * @param vntItem either the index of the child to remove or the child itself.
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T removeChild(jsx3.app.Model vntItem, Class<T> returnType)
    {
        String extension = "removeChild(\"" + vntItem + "\").";
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
     * Removes a DOM child from this object. This method removes the on-screen DHTML of the removed object. The removed
child will be completely derefenced from the DOM and will be prepped for garbage collection. If a DOM child must
continue to exist after removing it from this parent, adoptChild() should be used instead of this
method.
     * @param vntItem either the index of the child to remove or the child itself.
     * @return this object
     */

    public jsx3.app.Model removeChild(int vntItem)
    {
        String extension = "removeChild(\"" + vntItem + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Removes a DOM child from this object. This method removes the on-screen DHTML of the removed object. The removed
child will be completely derefenced from the DOM and will be prepped for garbage collection. If a DOM child must
continue to exist after removing it from this parent, adoptChild() should be used instead of this
method.
     * @param vntItem either the index of the child to remove or the child itself.
     * @param returnType The expected return type
     * @return this object
     */

    public <T> T removeChild(int vntItem, Class<T> returnType)
    {
        String extension = "removeChild(\"" + vntItem + "\").";
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
     * Removes some or all children of this object.
     * @param arrChildren the children to remove. If this parameter is not provided then all
  children are removed.
     * @return this object.
     */

    public jsx3.app.Model removeChildren(Object[] arrChildren)
    {
        String extension = "removeChildren(\"" + arrChildren + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Removes some or all children of this object.
     * @param arrChildren the children to remove. If this parameter is not provided then all
  children are removed.
     * @param returnType The expected return type
     * @return this object.
     */

    public <T> T removeChildren(Object[] arrChildren, Class<T> returnType)
    {
        String extension = "removeChildren(\"" + arrChildren + "\").";
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
     * Returns an object reference to the server that owns this object. This method returns null if this
object is part of a DOM fragment. Until an object is added to a DOM tree by passing it as the parameter to
setChild(), the object will be a DOM fragment.
     */

    public jsx3.app.Server getServer()
    {
        String extension = "getServer().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Server> ctor = jsx3.app.Server.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Server.class.getName());
        }
    }


    /**
     * Appends a DOM node to this object after removing the node from its former parent reference. If the node to append
does not already have a DOM parent, setChild() should be used instead of this method.
     * @param objChild the child to adopt
     * @param bRepaint if <code>true</code> or <code>null</code>, the object being adopted will be added to
   the parent's view via the parent's <code>paintChild()</code> method.
   This parameter is made available for those situations where a loop is executing and multiple
   objects are being adopted.  As view operations are the most CPU intensive, passing <code>false</code>
   while looping through a collection of child objects to adopt will improve performance. After the
   last child is adopted, simply call <code>repaint()</code> on the parent to immediately synchronize the view.
     * @param bForce if true, the adoption is forced, even if the parent/child don't accept such adoptions (<code>onSetChild()</code> and <code>onSetParent()</code> will still be called)
     */
    public void adoptChild(jsx3.app.Model objChild, boolean bRepaint, boolean bForce)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "adoptChild", objChild, bRepaint, bForce);
        ScriptSessions.addScript(script);
    }

    /**
     * Assigns objMoveChild as the previousSibling of objPrecedeChild
     * @param objMoveChild the one being moved. Can belong to this object, another object, or can be a GUI fragment
     * @param objPrecedeChild the one to insert before
     * @param bRepaint if <code>false</code> the repaint will be suppressed (useful for multiple obejct updates
   that would lead to unnecessary updates to the VIEW)
     * @param callback true if successful
     */

    public void insertBefore(jsx3.app.Model objMoveChild, jsx3.app.Model objPrecedeChild, boolean bRepaint, org.directwebremoting.ui.Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "insertBefore", objMoveChild, objPrecedeChild, bRepaint);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Called when the server owning this DOM node changes.
     * @param objNewServer
     * @param objOldServer
     */
    public void onChangeServer(jsx3.app.Server objNewServer, jsx3.app.Server objOldServer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onChangeServer", objNewServer, objOldServer);
        ScriptSessions.addScript(script);
    }

    /**
     * Creates and returns an exact replica of the object. The clone will be appended as a new child node of this
object's parent node.
     * @param intPersist the persistance value of the clone.
     * @param intMode <code>0</code> for insert as the last child of the parent of this object and paint,
    <code>1</code> for insert as the last child of this parent of this object and do not paint, or <code>2</code>
    for load as a fragment.
     * @return the clone.
     */

    public jsx3.app.Model doClone(int intPersist, int intMode)
    {
        String extension = "doClone(\"" + intPersist + "\", \"" + intMode + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Creates and returns an exact replica of the object. The clone will be appended as a new child node of this
object's parent node.
     * @param intPersist the persistance value of the clone.
     * @param intMode <code>0</code> for insert as the last child of the parent of this object and paint,
    <code>1</code> for insert as the last child of this parent of this object and do not paint, or <code>2</code>
    for load as a fragment.
     * @param returnType The expected return type
     * @return the clone.
     */

    public <T> T doClone(int intPersist, int intMode, Class<T> returnType)
    {
        String extension = "doClone(\"" + intPersist + "\", \"" + intMode + "\").";
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
     * Finds the first descendant of this DOM node with a the given name.
     * @param strName the name to query on.
     * @param bDepthFirst specifies whether to do a depth first or breadth first search.
     * @param bChildOnly if <code>true</code>, only search the children of this DOM node.
     * @return the descendant with the given name or <code>null</code> if none found.
     */

    public jsx3.app.Model getDescendantOfName(String strName, boolean bDepthFirst, boolean bChildOnly)
    {
        String extension = "getDescendantOfName(\"" + strName + "\", \"" + bDepthFirst + "\", \"" + bChildOnly + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Finds the first descendant of this DOM node with a the given name.
     * @param strName the name to query on.
     * @param bDepthFirst specifies whether to do a depth first or breadth first search.
     * @param bChildOnly if <code>true</code>, only search the children of this DOM node.
     * @param returnType The expected return type
     * @return the descendant with the given name or <code>null</code> if none found.
     */

    public <T> T getDescendantOfName(String strName, boolean bDepthFirst, boolean bChildOnly, Class<T> returnType)
    {
        String extension = "getDescendantOfName(\"" + strName + "\", \"" + bDepthFirst + "\", \"" + bChildOnly + "\").";
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
     * Finds the first child of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bExact if <code>true</code> then only return objects whose class is exactly <code>strType</code>
   (rather than returning subclasses too).
     * @return the child of the given type or <code>null</code> if none found.
     */

    public jsx3.app.Model getFirstChildOfType(org.directwebremoting.ui.CodeBlock strType, boolean bExact)
    {
        String extension = "getFirstChildOfType(\"" + strType + "\", \"" + bExact + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Finds the first child of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bExact if <code>true</code> then only return objects whose class is exactly <code>strType</code>
   (rather than returning subclasses too).
     * @param returnType The expected return type
     * @return the child of the given type or <code>null</code> if none found.
     */

    public <T> T getFirstChildOfType(org.directwebremoting.ui.CodeBlock strType, boolean bExact, Class<T> returnType)
    {
        String extension = "getFirstChildOfType(\"" + strType + "\", \"" + bExact + "\").";
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
     * Finds the first child of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bExact if <code>true</code> then only return objects whose class is exactly <code>strType</code>
   (rather than returning subclasses too).
     * @return the child of the given type or <code>null</code> if none found.
     */

    public jsx3.app.Model getFirstChildOfType(Class<?> strType, boolean bExact)
    {
        String extension = "getFirstChildOfType(\"" + strType + "\", \"" + bExact + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Finds the first child of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bExact if <code>true</code> then only return objects whose class is exactly <code>strType</code>
   (rather than returning subclasses too).
     * @param returnType The expected return type
     * @return the child of the given type or <code>null</code> if none found.
     */

    public <T> T getFirstChildOfType(Class<?> strType, boolean bExact, Class<T> returnType)
    {
        String extension = "getFirstChildOfType(\"" + strType + "\", \"" + bExact + "\").";
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
     * Finds the first child of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bExact if <code>true</code> then only return objects whose class is exactly <code>strType</code>
   (rather than returning subclasses too).
     * @return the child of the given type or <code>null</code> if none found.
     */

    public jsx3.app.Model getFirstChildOfType(String strType, boolean bExact)
    {
        String extension = "getFirstChildOfType(\"" + strType + "\", \"" + bExact + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Finds the first child of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bExact if <code>true</code> then only return objects whose class is exactly <code>strType</code>
   (rather than returning subclasses too).
     * @param returnType The expected return type
     * @return the child of the given type or <code>null</code> if none found.
     */

    public <T> T getFirstChildOfType(String strType, boolean bExact, Class<T> returnType)
    {
        String extension = "getFirstChildOfType(\"" + strType + "\", \"" + bExact + "\").";
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
     * Finds all descendants of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bShallow if <code>true</code>, only search direct children, not all descendants.
     * @param callback an array of matching descendants
     */

    public void getDescendantsOfType(org.directwebremoting.ui.CodeBlock strType, boolean bShallow, org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDescendantsOfType", strType, bShallow);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Finds all descendants of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bShallow if <code>true</code>, only search direct children, not all descendants.
     * @param callback an array of matching descendants
     */

    public void getDescendantsOfType(String strType, boolean bShallow, org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDescendantsOfType", strType, bShallow);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Finds all descendants of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param bShallow if <code>true</code>, only search direct children, not all descendants.
     * @param callback an array of matching descendants
     */

    public void getDescendantsOfType(Class<?> strType, boolean bShallow, org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getDescendantsOfType", strType, bShallow);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Finds all DOM nodes descending from this DOM node that pass the given test function. Results are guaranteed to be
returned in order according to the search order used.
     * @param fctTest test function, takes a single <code>jsx3.app.Model</code> parameter and returns
   <code>true</code> if the node matches.
     * @param bDepthFirst specifies whether to do a depth first or breadth first search.
     * @param bMultiple if <code>true</code>, return an array of matches, otherwise just the first match.
     * @param bShallow if <code>true</code>, only search direct children.
     * @param bIncludeSelf if <code>true</code>, include this node in the search.
     * @return the match (bMultiple = false) or matches (bMultiple = true).
     */

    public jsx3.app.Model findDescendants(org.directwebremoting.ui.CodeBlock fctTest, boolean bDepthFirst, boolean bMultiple, boolean bShallow, boolean bIncludeSelf)
    {
        String extension = "findDescendants(\"" + fctTest + "\", \"" + bDepthFirst + "\", \"" + bMultiple + "\", \"" + bShallow + "\", \"" + bIncludeSelf + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Finds all DOM nodes descending from this DOM node that pass the given test function. Results are guaranteed to be
returned in order according to the search order used.
     * @param fctTest test function, takes a single <code>jsx3.app.Model</code> parameter and returns
   <code>true</code> if the node matches.
     * @param bDepthFirst specifies whether to do a depth first or breadth first search.
     * @param bMultiple if <code>true</code>, return an array of matches, otherwise just the first match.
     * @param bShallow if <code>true</code>, only search direct children.
     * @param bIncludeSelf if <code>true</code>, include this node in the search.
     * @param returnType The expected return type
     * @return the match (bMultiple = false) or matches (bMultiple = true).
     */

    public <T> T findDescendants(org.directwebremoting.ui.CodeBlock fctTest, boolean bDepthFirst, boolean bMultiple, boolean bShallow, boolean bIncludeSelf, Class<T> returnType)
    {
        String extension = "findDescendants(\"" + fctTest + "\", \"" + bDepthFirst + "\", \"" + bMultiple + "\", \"" + bShallow + "\", \"" + bIncludeSelf + "\").";
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
     * The finalizer method. This method provides a hook for subclasses of this class to perform custom logic
when an instance of this class is removed from the DOM. Methods that override this method should begin with
a call to jsxsuper().

Note that this method is called after this object has been removed from the DOM tree. Therefore
this.getParent() and this.getServer() will return null. Use the
objParent parameter for access to the DOM tree.
     * @param objParent reference to the former parent
     */
    public void onDestroy(jsx3.app.Model objParent)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onDestroy", objParent);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the custom JSX-generated id for the object (i.e., _jsx2384098324509823049).
     * @param callback JSX id
     */

    public void getId(org.directwebremoting.ui.Callback<String> callback)
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
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the zero-based index for this DOM node in relation to its siblings.
     * @param callback the index or <code>-1</code> if this object does not have a parent.
     */

    public void getChildIndex(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getChildIndex");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the custom developer-defined name of this object.
     */

    public void getName(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getName");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the custom developer-defined name of this object.
     * @param strName a name unique among all DOM nodes currently loaded in the application.
     */
    public void setName(String strName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setName", strName);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the help ID of this object.
     */

    public void getHelpId(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getHelpId");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the help ID of this object.
     * @param strId
     */
    public void setHelpId(String strId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setHelpId", strId);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the load type of this DOM node and the descending branch. The load type determines how this DOM branch
deserializes and paints in relation to its parent DOM node.
     * @param callback <code>LT_NORMAL</code>, <code>LT_SLEEP_PAINT</code>, <code>LT_SLEEP_DESER</code>,
   <code>LT_SLEEP_PD</code>, <code>LT_SHOW_PAINT</code>, or <code>LT_SHOW_DESER</code>.
     */

    public void getLoadType(org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getLoadType");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Sets the load type of this DOM node and the descending branch.
     * @param intLoadType <code>LT_NORMAL</code>, <code>LT_SLEEP_PAINT</code>, <code>LT_SLEEP_DESER</code>,
   <code>LT_SLEEP_PD</code>, <code>LT_SHOW_PAINT</code>, or <code>LT_SHOW_DESER</code>.
     */
    public void setLoadType(int intLoadType)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setLoadType", intLoadType);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the parent DOM node of this object.
     */

    public jsx3.app.Model getParent()
    {
        String extension = "getParent().";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the parent DOM node of this object.
     * @param returnType The expected return type
     */

    public <T> T getParent(Class<T> returnType)
    {
        String extension = "getParent().";
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
     * Returns the first ancestor of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @return the first ancestor of the given type or <code>null</code> if none found.
     */

    public jsx3.app.Model getAncestorOfType(String strType)
    {
        String extension = "getAncestorOfType(\"" + strType + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the first ancestor of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param returnType The expected return type
     * @return the first ancestor of the given type or <code>null</code> if none found.
     */

    public <T> T getAncestorOfType(String strType, Class<T> returnType)
    {
        String extension = "getAncestorOfType(\"" + strType + "\").";
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
     * Returns the first ancestor of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @return the first ancestor of the given type or <code>null</code> if none found.
     */

    public jsx3.app.Model getAncestorOfType(Class<?> strType)
    {
        String extension = "getAncestorOfType(\"" + strType + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the first ancestor of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param returnType The expected return type
     * @return the first ancestor of the given type or <code>null</code> if none found.
     */

    public <T> T getAncestorOfType(Class<?> strType, Class<T> returnType)
    {
        String extension = "getAncestorOfType(\"" + strType + "\").";
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
     * Returns the first ancestor of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @return the first ancestor of the given type or <code>null</code> if none found.
     */

    public jsx3.app.Model getAncestorOfType(org.directwebremoting.ui.CodeBlock strType)
    {
        String extension = "getAncestorOfType(\"" + strType + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the first ancestor of the given type.
     * @param strType the fully-qualified class name, class constructor function,
   or <code>jsx3.Class</code> instance.
     * @param returnType The expected return type
     * @return the first ancestor of the given type or <code>null</code> if none found.
     */

    public <T> T getAncestorOfType(org.directwebremoting.ui.CodeBlock strType, Class<T> returnType)
    {
        String extension = "getAncestorOfType(\"" + strType + "\").";
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
     * Returns the first ancestor with the given name.
     * @param strName the name to query on.
     * @return the first ancestor with the given name or <code>null</code> if none found.
     */

    public jsx3.app.Model getAncestorOfName(String strName)
    {
        String extension = "getAncestorOfName(\"" + strName + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the first ancestor with the given name.
     * @param strName the name to query on.
     * @param returnType The expected return type
     * @return the first ancestor with the given name or <code>null</code> if none found.
     */

    public <T> T getAncestorOfName(String strName, Class<T> returnType)
    {
        String extension = "getAncestorOfName(\"" + strName + "\").";
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
     * Returns the first ancestor passing the given test function.
     * @param fctTest test function, takes a single <code>jsx3.app.Model</code> parameter and returns
   <code>true</code> if the node matches.
     * @param bIncludeSelf if <code>true</code>, include this object in the search
     */

    public jsx3.app.Model findAncestor(org.directwebremoting.ui.CodeBlock fctTest, boolean bIncludeSelf)
    {
        String extension = "findAncestor(\"" + fctTest + "\", \"" + bIncludeSelf + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Returns the first ancestor passing the given test function.
     * @param fctTest test function, takes a single <code>jsx3.app.Model</code> parameter and returns
   <code>true</code> if the node matches.
     * @param bIncludeSelf if <code>true</code>, include this object in the search
     * @param returnType The expected return type
     */

    public <T> T findAncestor(org.directwebremoting.ui.CodeBlock fctTest, boolean bIncludeSelf, Class<T> returnType)
    {
        String extension = "findAncestor(\"" + fctTest + "\", \"" + bIncludeSelf + "\").";
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
     * Returns this object serialized as XML by calling toString() on the result of toXMLDoc()
called on this object.
     * @param objProperties name-value pairs that affect the serialization. See
  <code>toXMLDoc()</code> for a description.
     * @param callback this object serialized as an XML string.
     */

    public void toXML(jsx3.lang.Object objProperties, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "toXML", objProperties);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Serializes this object as an XML document.

The objProperties parameter may include the following keys:

  onafter {String} - the value of the onAfterDeserialize element
  onbefore {String} - the value of the onBeforeDeserialize element
  name {String} - the value of the name element
  icon {String} - the value of the icon element
  description {String} - the value of the description element
  children {boolean} - if true the children of this object, rather than this object, are
         serialized
  persistall {boolean} - if true all descendants with persistence PERSISTNONE are included in the
         serialization
     * @param objProperties name-value pairs that affect the serialization. See above for
  valid names and how they affect serialization.
     * @return this object serialized as an XML document.
     */

    public jsx3.xml.CdfDocument toXMLDoc(jsx3.lang.Object objProperties)
    {
        String extension = "toXMLDoc(\"" + objProperties + "\").";
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
     * Serializes this object as an XML document.

The objProperties parameter may include the following keys:

  onafter {String} - the value of the onAfterDeserialize element
  onbefore {String} - the value of the onBeforeDeserialize element
  name {String} - the value of the name element
  icon {String} - the value of the icon element
  description {String} - the value of the description element
  children {boolean} - if true the children of this object, rather than this object, are
         serialized
  persistall {boolean} - if true all descendants with persistence PERSISTNONE are included in the
         serialization
     * @param objProperties name-value pairs that affect the serialization. See above for
  valid names and how they affect serialization.
     * @param returnType The expected return type
     * @return this object serialized as an XML document.
     */

    public <T> T toXMLDoc(jsx3.lang.Object objProperties, Class<T> returnType)
    {
        String extension = "toXMLDoc(\"" + objProperties + "\").";
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
     * Returns the namespace that distinguishes this object's server (owner) from other server instances. The namespace
is set when this object is bound to a DOM tree.
     * @param callback the namespace of the server that owns this object instance.
     */

    public void getNS(org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getNS");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the URI resolver for this DOM node. This method returns the server of this object unless this node
or its ancestor was loaded into the DOM with an explicit URI resolver.
     */

    public jsx3.net.URIResolver getUriResolver()
    {
        String extension = "getUriResolver().";
        try
        {
            java.lang.reflect.Constructor<jsx3.net.URIResolver> ctor = jsx3.net.URIResolver.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.net.URIResolver.class.getName());
        }
    }

    /**
     * Returns the URI resolver for this DOM node. This method returns the server of this object unless this node
or its ancestor was loaded into the DOM with an explicit URI resolver.
     * @param returnType The expected return type
     */

    public <T> T getUriResolver(Class<T> returnType)
    {
        String extension = "getUriResolver().";
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
     * Deserializes a JSX serialization file and appends the deserialized objects as children of this DOM node.
     * @param strURL URL (either relative or absolute) of the serialization file to deserialize.
   This URL is resolved relative to <code>objResolver</code>, if provided, or the URI resolver of this DOM node.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objResolver If this parameter is provided, <code>strURL</code> is resolved
   relative to it. Additionally, this resolver is stored as the URI resolver for this DOM node and its descendants.
     * @return the deserialized object. A serialization file may specify more than one root
   object, in which case this method returns the first deserialized object.
     */

    public jsx3.app.Model load(java.net.URI strURL, boolean bRepaint, jsx3.net.URIResolver objResolver)
    {
        String extension = "load(\"" + strURL + "\", \"" + bRepaint + "\", \"" + objResolver + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Deserializes a JSX serialization file and appends the deserialized objects as children of this DOM node.
     * @param strURL URL (either relative or absolute) of the serialization file to deserialize.
   This URL is resolved relative to <code>objResolver</code>, if provided, or the URI resolver of this DOM node.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objResolver If this parameter is provided, <code>strURL</code> is resolved
   relative to it. Additionally, this resolver is stored as the URI resolver for this DOM node and its descendants.
     * @param returnType The expected return type
     * @return the deserialized object. A serialization file may specify more than one root
   object, in which case this method returns the first deserialized object.
     */

    public <T> T load(java.net.URI strURL, boolean bRepaint, jsx3.net.URIResolver objResolver, Class<T> returnType)
    {
        String extension = "load(\"" + strURL + "\", \"" + bRepaint + "\", \"" + objResolver + "\").";
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
     * Deserializes a JSX serialization file and appends the deserialized objects as children of this DOM node.
     * @param strURL URL (either relative or absolute) of the serialization file to deserialize.
   This URL is resolved relative to <code>objResolver</code>, if provided, or the URI resolver of this DOM node.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objResolver If this parameter is provided, <code>strURL</code> is resolved
   relative to it. Additionally, this resolver is stored as the URI resolver for this DOM node and its descendants.
     * @return the deserialized object. A serialization file may specify more than one root
   object, in which case this method returns the first deserialized object.
     */

    public jsx3.app.Model load(String strURL, boolean bRepaint, jsx3.net.URIResolver objResolver)
    {
        String extension = "load(\"" + strURL + "\", \"" + bRepaint + "\", \"" + objResolver + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Deserializes a JSX serialization file and appends the deserialized objects as children of this DOM node.
     * @param strURL URL (either relative or absolute) of the serialization file to deserialize.
   This URL is resolved relative to <code>objResolver</code>, if provided, or the URI resolver of this DOM node.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objResolver If this parameter is provided, <code>strURL</code> is resolved
   relative to it. Additionally, this resolver is stored as the URI resolver for this DOM node and its descendants.
     * @param returnType The expected return type
     * @return the deserialized object. A serialization file may specify more than one root
   object, in which case this method returns the first deserialized object.
     */

    public <T> T load(String strURL, boolean bRepaint, jsx3.net.URIResolver objResolver, Class<T> returnType)
    {
        String extension = "load(\"" + strURL + "\", \"" + bRepaint + "\", \"" + objResolver + "\").";
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
     * Deserializes a JSX serialization file and appends the deserialized objects as children of this DOM node.
     * @param strXML the XML content of a JSX serialization file.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objResolver
     * @return the deserialized object. A serialization file may specify more than one root
   object, in which case this method returns the first deserialized object.
     */

    public jsx3.app.Model loadXML(jsx3.xml.CdfDocument strXML, boolean bRepaint, jsx3.net.URIResolver objResolver)
    {
        String extension = "loadXML(\"" + strXML + "\", \"" + bRepaint + "\", \"" + objResolver + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Deserializes a JSX serialization file and appends the deserialized objects as children of this DOM node.
     * @param strXML the XML content of a JSX serialization file.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objResolver
     * @param returnType The expected return type
     * @return the deserialized object. A serialization file may specify more than one root
   object, in which case this method returns the first deserialized object.
     */

    public <T> T loadXML(jsx3.xml.CdfDocument strXML, boolean bRepaint, jsx3.net.URIResolver objResolver, Class<T> returnType)
    {
        String extension = "loadXML(\"" + strXML + "\", \"" + bRepaint + "\", \"" + objResolver + "\").";
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
     * Deserializes a JSX serialization file and appends the deserialized objects as children of this DOM node.
     * @param strXML the XML content of a JSX serialization file.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objResolver
     * @return the deserialized object. A serialization file may specify more than one root
   object, in which case this method returns the first deserialized object.
     */

    public jsx3.app.Model loadXML(String strXML, boolean bRepaint, jsx3.net.URIResolver objResolver)
    {
        String extension = "loadXML(\"" + strXML + "\", \"" + bRepaint + "\", \"" + objResolver + "\").";
        try
        {
            java.lang.reflect.Constructor<jsx3.app.Model> ctor = jsx3.app.Model.class.getConstructor(Context.class, String.class);
            return ctor.newInstance(this, extension);
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + jsx3.app.Model.class.getName());
        }
    }

    /**
     * Deserializes a JSX serialization file and appends the deserialized objects as children of this DOM node.
     * @param strXML the XML content of a JSX serialization file.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objResolver
     * @param returnType The expected return type
     * @return the deserialized object. A serialization file may specify more than one root
   object, in which case this method returns the first deserialized object.
     */

    public <T> T loadXML(String strXML, boolean bRepaint, jsx3.net.URIResolver objResolver, Class<T> returnType)
    {
        String extension = "loadXML(\"" + strXML + "\", \"" + bRepaint + "\", \"" + objResolver + "\").";
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
     * Loads a component file and caches the document in an XML cache. If the component file already exists in the cache
then it is loaded from the cache. All component files loaded as a result of this call (referenced files) are also
cached. This method is a useful replacement for load() when the same URL will be loaded multiple
times in one application.
     * @param strURL URL (either relative or absolute) of the serialization file to deserialize.
   This URL is resolved relative to <code>objResolver</code>, if provided, or the URI resolver of this DOM node.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objCache the cache to store the component XML documents in. If not provided, the cache
   of the server of this model object is used.
     * @param objResolver If this parameter is provided, <code>strURL</code> is resolved
   relative to it. Additionally, this resolver is stored as the URI resolver for this DOM node and its descendants.
     */
    public void loadAndCache(java.net.URI strURL, boolean bRepaint, jsx3.app.Cache objCache, jsx3.net.URIResolver objResolver)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "loadAndCache", strURL, bRepaint, objCache, objResolver);
        ScriptSessions.addScript(script);
    }

    /**
     * Loads a component file and caches the document in an XML cache. If the component file already exists in the cache
then it is loaded from the cache. All component files loaded as a result of this call (referenced files) are also
cached. This method is a useful replacement for load() when the same URL will be loaded multiple
times in one application.
     * @param strURL URL (either relative or absolute) of the serialization file to deserialize.
   This URL is resolved relative to <code>objResolver</code>, if provided, or the URI resolver of this DOM node.
     * @param bRepaint if <code>true</code> or <code>null</code> the deserialized objects will be
   added to the parent's view via the parent object's <code>paintChild()</code> method.
     * @param objCache the cache to store the component XML documents in. If not provided, the cache
   of the server of this model object is used.
     * @param objResolver If this parameter is provided, <code>strURL</code> is resolved
   relative to it. Additionally, this resolver is stored as the URI resolver for this DOM node and its descendants.
     */
    public void loadAndCache(String strURL, boolean bRepaint, jsx3.app.Cache objCache, jsx3.net.URIResolver objResolver)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "loadAndCache", strURL, bRepaint, objCache, objResolver);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns one of the meta data values stored at the top of the serialization file that this object was loaded from.
     * @param strKey the name of the meta data field, one of the keys in <code>META_FIELDS</code>.
     * @param callback the meta data value or empty string.
     */

    public void getMetaValue(String strKey, org.directwebremoting.ui.Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getMetaValue", strKey);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * setS one of the meta data values stored at the top of a component's serialization file.
     * @param strKey the name of the meta data field, one of the keys in <code>META_FIELDS</code>
     * @param strValue the new value of the meta data field.
     */
    public void setMetaValue(String strKey, String strValue)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setMetaValue", strKey, strValue);
        ScriptSessions.addScript(script);
    }

    /**
     * Called during deserialization of this object. This method provides a hook for initializing
an object during deserialization since init() is not called. Called after this object has been instantiated but
before its fields and children have been assembled. This method is called before this object is attached to the
DOM, therefore getParent(), getServer(), getXML(), etc. return null.
     * @param objParent the parent of this object once it is attached to the DOM.
     * @param objServer the server that this DOM object will attach to.
     */
    public void onBeforeAssemble(jsx3.app.Model objParent, jsx3.app.Server objServer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onBeforeAssemble", objParent, objServer);
        ScriptSessions.addScript(script);
    }

    /**
     * Called during deserialization of this object. This method provides a hook for initializing
an object during deserialization since init() is not called. Called after this object has been instantiated and
after its fields and children have been assembled.This method is called before this object is attached to the
DOM, therefore getParent(), getServer(), getXML(), etc. return null.
     * @param objParent the parent of this object once it is attached to the DOM.
     * @param objServer the server that this DOM object will attach to.
     */
    public void onAfterAssemble(jsx3.app.Model objParent, jsx3.app.Server objServer)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onAfterAssemble", objParent, objServer);
        ScriptSessions.addScript(script);
    }

    /**
     * Called during deserialization of this object. This method provides a hook for initializing
an object during deserialization since init() is not called. Called after this object has been
instantiated and after it has been attached to the DOM. Methods overriding this method should usually finish
with a call to jsxsuper().

When a new branch is attached to the DOM, this method is executed on each node in the branch. The order is
reverse-breadth-first meaning that child nodes are notified from oldest to youngest and before the parent node.

This implementation of this method executes the on-after-deserialize script of this object.
     */
    public void onAfterAttach()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "onAfterAttach");
        ScriptSessions.addScript(script);
    }

    /**
     * Publishes an event to all subscribed objects.
     * @param objEvent the event, should have at least a field 'subject' that is the event id, another common field is 'target' (target will default to this instance)
     * @param callback the number of listeners to which the event was broadcast
     */

    public void publish(jsx3.lang.Object objEvent, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "publish", objEvent);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, jsx3.lang.Object objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, org.directwebremoting.ui.CodeBlock objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, org.directwebremoting.ui.CodeBlock objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, String objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, jsx3.lang.Object objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, org.directwebremoting.ui.CodeBlock objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, String objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, jsx3.lang.Object objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, jsx3.lang.Object objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, String objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(Object[] strEventId, String objHandler, org.directwebremoting.ui.CodeBlock objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Subscribes an object or function to a type of event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler if an object, the instance to notify of events (objFunction is required); if a string, the JSX id of the instance to notify of events (objFunction is required), must exist in the same Server; if a function, the function to call to notify of events (objFunction ignored)
     * @param objFunction if objHandler is a string or object then the function to call on that instance. either a function or a string that is the name of a method of the instance
     */
    public void subscribe(String strEventId, org.directwebremoting.ui.CodeBlock objHandler, String objFunction)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "subscribe", strEventId, objHandler, objFunction);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(Object[] strEventId, org.directwebremoting.ui.CodeBlock objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(Object[] strEventId, String objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(String strEventId, String objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(String strEventId, jsx3.lang.Object objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(Object[] strEventId, jsx3.lang.Object objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribe an object or function from an event published by this object.

As of version 3.4 a string value for objHandler is deprecated.
     * @param strEventId the event type(s).
     * @param objHandler the value of objHandler passed to subscribe
     */
    public void unsubscribe(String strEventId, org.directwebremoting.ui.CodeBlock objHandler)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribe", strEventId, objHandler);
        ScriptSessions.addScript(script);
    }

    /**
     * Unsubscribes all subscribed objects to a type of event published by this object.
     * @param strEventId the event type
     */
    public void unsubscribeAll(String strEventId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "unsubscribeAll", strEventId);
        ScriptSessions.addScript(script);
    }

}

