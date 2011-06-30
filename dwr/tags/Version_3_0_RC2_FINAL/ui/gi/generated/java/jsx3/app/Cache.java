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
 * Provides cached access to XML and XSL data.
Events
Cache instances publish two types of events for every operation that modifies the contents of the cache. The
schemas of the two event types are


        subject - Cache.CHANGE


        id or ids - the ID or array of IDs of the modified documents

        action - Cache.ADD, Cache.CHANGE or Cache.REMOVE


and


        subject - the cache ID of the modified document

        action - Cache.ADD, Cache.CHANGE or Cache.REMOVE


Asynchronous Loading
Cache documents can be loaded asychronously with the getOrOpenAsync() method. This method returns
the corresponding document synchronously if it already exists in the cache. If the document does not exist in the
cache, then it is loaded asynchronously and the method returns a placeholder document. The namespace URI of this
placeholder document is Cache.XSDNS and its root node name is "loading".

Since the cache stores this placeholder document until the document finishes loading, subsequent calls to
synchronous APIs (getDocument(), getOrOpenDocument(), etc) may also return the
placeholder document. It is therefore important to check the namespace of the returned document when any code
uses the asynchronous APIs.

Once a document finishes loading asynchronously the placeholder document is replaced with the loaded document.
This change in value causes the cache to publish a pair of events of action Cache.CHANGE. If
loading the document fails or times out the placeholder document is instead replaced with another placeholder
document. This document also has a URI namespace of Cache.XSDNS. Its root node name may be either
"error" or "timeout". If the root node name is "error" then the root node
has an attribute, also named "error", which contains the XML error message.
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Cache extends jsx3.lang.Object
{
    /**
     * All reverse ajax proxies need context to work from
     * @param context The script that got us to where we are now
     */
    public Cache(Context context, String extension)
    {
        super(context, extension);
    }

    /**
     * Creates a new instance of this class.
     */
    public Cache()
    {
        super((Context) null, (String) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new Cache");
        setInitScript(script);
    }


    /**
     * Event action.
     */
    public static final String REMOVE = "remove";

    /**
     * Event action.
     */
    public static final String ADD = "add";

    /**
     * Event subject and action.
     */
    public static final String CHANGE = "change";

    /**
     * The number of milliseconds before asynchronous document loads time out.
     */
    public static final int ASYNC_TIMEOUT = 60000;

    /**
     * 
     */
    public static final String XSDNS = "http://xsd.tns.tibco.com/gi/cache";


    /**
     * Removes the document stored in this cache under id strId.
     * @param strId
     * @return the remove document, if any.
     */

    public jsx3.xml.CdfDocument clearById(String strId)
    {
        String extension = "clearById(\"" + strId + "\").";
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
     * Removes the document stored in this cache under id strId.
     * @param strId
     * @param returnType The expected return type
     * @return the remove document, if any.
     */

    public <T> T clearById(String strId, Class<T> returnType)
    {
        String extension = "clearById(\"" + strId + "\").";
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
     * Removes all documents placed in this cache before intTimestamp.
     * @param intTimestamp epoch seconds or a date object.
     * @param callback the ids of the removed documents.
     */

    public void clearByTimestamp(java.util.Date intTimestamp, org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "clearByTimestamp", intTimestamp);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Removes all documents placed in this cache before intTimestamp.
     * @param intTimestamp epoch seconds or a date object.
     * @param callback the ids of the removed documents.
     */

    public void clearByTimestamp(int intTimestamp, org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "clearByTimestamp", intTimestamp);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns the document stored in this cache under id strId.
     * @param strId
     * @return the stored document or <code>null</code> if none exists.
     */

    public jsx3.xml.CdfDocument getDocument(String strId)
    {
        String extension = "getDocument(\"" + strId + "\").";
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
     * Returns the document stored in this cache under id strId.
     * @param strId
     * @param returnType The expected return type
     * @return the stored document or <code>null</code> if none exists.
     */

    public <T> T getDocument(String strId, Class<T> returnType)
    {
        String extension = "getDocument(\"" + strId + "\").";
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
     * Retrieves a document from this cache or, if this cache contains no such document, loads the document
synchronously and returns it.
     * @param strURL the URI of the document.
     * @param strId the id under which the document is/will be stored. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance if a new document is opened.
     * @return the document retrieved from the cache or loaded.
     */

    public jsx3.xml.CdfDocument getOrOpenDocument(String strURL, String strId, Class<?> objClass)
    {
        String extension = "getOrOpenDocument(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Retrieves a document from this cache or, if this cache contains no such document, loads the document
synchronously and returns it.
     * @param strURL the URI of the document.
     * @param strId the id under which the document is/will be stored. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance if a new document is opened.
     * @param returnType The expected return type
     * @return the document retrieved from the cache or loaded.
     */

    public <T> T getOrOpenDocument(String strURL, String strId, Class<?> objClass, Class<T> returnType)
    {
        String extension = "getOrOpenDocument(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Retrieves a document from this cache or, if this cache contains no such document, loads the document
synchronously and returns it.
     * @param strURL the URI of the document.
     * @param strId the id under which the document is/will be stored. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance if a new document is opened.
     * @return the document retrieved from the cache or loaded.
     */

    public jsx3.xml.CdfDocument getOrOpenDocument(java.net.URI strURL, String strId, Class<?> objClass)
    {
        String extension = "getOrOpenDocument(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Retrieves a document from this cache or, if this cache contains no such document, loads the document
synchronously and returns it.
     * @param strURL the URI of the document.
     * @param strId the id under which the document is/will be stored. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance if a new document is opened.
     * @param returnType The expected return type
     * @return the document retrieved from the cache or loaded.
     */

    public <T> T getOrOpenDocument(java.net.URI strURL, String strId, Class<?> objClass, Class<T> returnType)
    {
        String extension = "getOrOpenDocument(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Synchronously loads an xml document, stores it in this cache, and returns the loaded document.
     * @param strURL url (relative or absolute) the URI of the document to open.
     * @param strId the id under which to store the document. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance.
     * @return the loaded document object.
     */

    public jsx3.xml.CdfDocument openDocument(String strURL, String strId, Class<?> objClass)
    {
        String extension = "openDocument(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Synchronously loads an xml document, stores it in this cache, and returns the loaded document.
     * @param strURL url (relative or absolute) the URI of the document to open.
     * @param strId the id under which to store the document. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance.
     * @param returnType The expected return type
     * @return the loaded document object.
     */

    public <T> T openDocument(String strURL, String strId, Class<?> objClass, Class<T> returnType)
    {
        String extension = "openDocument(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Synchronously loads an xml document, stores it in this cache, and returns the loaded document.
     * @param strURL url (relative or absolute) the URI of the document to open.
     * @param strId the id under which to store the document. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance.
     * @return the loaded document object.
     */

    public jsx3.xml.CdfDocument openDocument(java.net.URI strURL, String strId, Class<?> objClass)
    {
        String extension = "openDocument(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Synchronously loads an xml document, stores it in this cache, and returns the loaded document.
     * @param strURL url (relative or absolute) the URI of the document to open.
     * @param strId the id under which to store the document. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance.
     * @param returnType The expected return type
     * @return the loaded document object.
     */

    public <T> T openDocument(java.net.URI strURL, String strId, Class<?> objClass, Class<T> returnType)
    {
        String extension = "openDocument(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Asynchronously loads an xml document and stores it in this cache.
     * @param strURL url (relative or absolute) the URI of the document to open.
     * @param strId the id under which to store the document. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance.
     * @return the document retrieved from the cache or a placeholder document if the document
   is in the process of loading asynchronously.
     */

    public jsx3.xml.CdfDocument getOrOpenAsync(String strURL, String strId, Class<?> objClass)
    {
        String extension = "getOrOpenAsync(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Asynchronously loads an xml document and stores it in this cache.
     * @param strURL url (relative or absolute) the URI of the document to open.
     * @param strId the id under which to store the document. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance.
     * @param returnType The expected return type
     * @return the document retrieved from the cache or a placeholder document if the document
   is in the process of loading asynchronously.
     */

    public <T> T getOrOpenAsync(String strURL, String strId, Class<?> objClass, Class<T> returnType)
    {
        String extension = "getOrOpenAsync(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Asynchronously loads an xml document and stores it in this cache.
     * @param strURL url (relative or absolute) the URI of the document to open.
     * @param strId the id under which to store the document. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance.
     * @return the document retrieved from the cache or a placeholder document if the document
   is in the process of loading asynchronously.
     */

    public jsx3.xml.CdfDocument getOrOpenAsync(java.net.URI strURL, String strId, Class<?> objClass)
    {
        String extension = "getOrOpenAsync(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Asynchronously loads an xml document and stores it in this cache.
     * @param strURL url (relative or absolute) the URI of the document to open.
     * @param strId the id under which to store the document. If this parameter is not provided, the
   <code>strURL</code> parameter is used as the id.
     * @param objClass <code>jsx3.xml.Document</code> (default value) or one of its subclasses. The
   class with which to instantiate the new document instance.
     * @param returnType The expected return type
     * @return the document retrieved from the cache or a placeholder document if the document
   is in the process of loading asynchronously.
     */

    public <T> T getOrOpenAsync(java.net.URI strURL, String strId, Class<?> objClass, Class<T> returnType)
    {
        String extension = "getOrOpenAsync(\"" + strURL + "\", \"" + strId + "\", \"" + objClass + "\").";
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
     * Stores the document objDocument in this cache under id strId. If a document already
exists in this cache under strId then that document is removed from the cache.
     * @param strId the id under which to store <code>objDocument</code>.
     * @param objDocument
     */
    public void setDocument(String strId, jsx3.xml.CdfDocument objDocument)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "setDocument", strId, objDocument);
        ScriptSessions.addScript(script);
    }

    /**
     * Returns the timestamp from when the document stored under id strId was stored in this cache.
     * @param strId the id under which the document is stored.
     * @param callback the timestamp as an integer (epoch seconds) or <code>null</code> if no such document exists
   in this cache.
     */

    public void getTimestamp(String strId, org.directwebremoting.ui.Callback<Integer> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "getTimestamp", strId);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Integer.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Returns a list of all the keys in this cache instance.
     */

    public void keys(org.directwebremoting.ui.Callback<Object[]> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "keys");

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelperFactory.get().saveCallback(callback, Object[].class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Removes all references to documents contained in this cache. This cache is no longer usable after calling this
method.
     */
    public void destroy()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "destroy");
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
    public void subscribe(String strEventId, org.directwebremoting.ui.CodeBlock objHandler, String objFunction)
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
    public void subscribe(String strEventId, String objHandler, String objFunction)
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
    }

}

