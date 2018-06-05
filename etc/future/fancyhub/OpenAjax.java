package org.directwebremoting.proxy.openajax;

import java.util.Collection;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.PublishListener;
import org.directwebremoting.ui.ScriptProxy;

/**
 * Util is a server-side proxy that allows Java programmers to call client
 * side Javascript from Java.
 * <p>
 * Each Util object is associated with a list of ScriptSessions and the
 * proxy code is creates will be dynamically forwarded to all those browsers.
 * <p>
 * Currently this class contains only the write-only DOM manipulation functions
 * from Util. It is possible that we could add the read methods, however
 * the complexity in the callback and the fact that you are probably not going
 * to need it means that we'll leave it for another day. Specifically,
 * <code>getValue</code>, <code>getValues</code> and <code>getText</code> have
 * been left out as being read functions and <code>useLoadingMessage</code> etc
 * have been left out as not being DOM related.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class OpenAjax
{
    /**
     * Publishes (broadcasts) an event based on a library-specific prefix and
     * event name.
     * @param prefix The prefix that corresponds to this event. This must be a
     * prefix that has been registered via OpenAjax.registerLibrary().
     * @param name The name of the event to listen for. Names can be any string
     */
    public void publish(String prefix, String name)
    {
        addFunctionCall("OpenAjax.publish", prefix, name);
    }

    /**
     * Publishes (broadcasts) an event based on a library-specific prefix and
     * event name.
     * @param prefix The prefix that corresponds to this event. This must be a
     * prefix that has been registered via OpenAjax.registerLibrary().
     * @param name The name of the event to listen for. Names can be any string
     * @param publisherData An arbitrary Object holding extra information that
     * will be passed as an argument to the handler function. Can be null.
     */
    public void publish(String prefix, String name, Object publisherData)
    {
        addFunctionCall("OpenAjax.publish", prefix, name, publisherData);
    }

    /**
     * Allows registration of interest in named events based on library-specific
     * prefix and event name. Global event matching is provided by passing "*"
     * in the prefix and/or name arguments. Optional arguments may be specified
     * for executing the specified handler function in a provided scope and for
     * further filtering events prior to application.
     * <p>
     * The callback function will receive the following parameters
     * (see OpenAjax.publish() for description of publisherData):
     * <pre>
     * function(prefix, name, subscriberData, publisherData){ ... }
     * </pre>
     * @param prefix The prefix that corresponds to this library. This is the
     * same value that was previously passed to registerLibrary(). Can be "*" to
     * match the provided event name across all libraries.
     * @param name The name of the event to listen for. Names can be any string.
     * Can be "*" to match all events in the specified toolkit (see prefix). If
     * both name and prefix specify "*", all events in the system will be routed
     * to the registered handler (modulo any filtering provided by filter).
     * @param listener The object to deliver messages to
     */
    public void subscribe(String prefix, String name, PublishListener listener)
    {
    }

    /**
     * Allows registration of interest in named events based on library-specific
     * prefix and event name. Global event matching is provided by passing "*"
     * in the prefix and/or name arguments. Optional arguments may be specified
     * for executing the specified handler function in a provided scope and for
     * further filtering events prior to application.
     * <p>
     * The callback function will receive the following parameters
     * (see OpenAjax.publish() for description of publisherData):
     * <pre>
     * function(prefix, name, subscriberData, publisherData){ ... }
     * </pre>
     * @param prefix The prefix that corresponds to this library. This is the
     * same value that was previously passed to registerLibrary(). Can be "*" to
     * match the provided event name across all libraries.
     * @param name The name of the event to listen for. Names can be any string.
     * Can be "*" to match all events in the specified toolkit (see prefix). If
     * both name and prefix specify "*", all events in the system will be routed
     * to the registered handler (modulo any filtering provided by filter).
     * @param listener The object to deliver messages to
     */
    public void subscribe(String prefix, String name, PublishListener listener, Object subscriberData)
    {
    }

    /**
     * Removes a subscription to an event. In order for a subscription to be
     * removed, the values of the parameters supplied to OpenAjax.unsubscribe()
     * must exactly match the values of the parameters supplied to a previous
     * call to OpenAjax.subscribe(). Note that it is possible that one
     * invocation of OpenAjax.unsubscribe() might result in removal of multiple
     * subscriptions.
     * @param prefix The prefix that corresponds to this library. This is the
     * same value that was previously passed to registerLibrary(). Can be "*" to
     * match the provided event name across all libraries.
     * @param name The name of the event to listen for. Names can be any string.
     * Can be "*" to match all events in the specified toolkit (see prefix). If
     * both name and prefix specify "*", all events in the system will be routed
     * to the registered handler (modulo any filtering provided by filter).
     * @param listener The object to deliver messages to
     */
    public void unsubscribe(String prefix, String name, PublishListener listener)
    {
    }
}
