package org.directwebremoting.ui.browser;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.extend.CallbackHelperFactory;
import org.directwebremoting.ui.Callback;

/**
 * A copy of some of the functions from the Window DOM object on the server
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Window
{
    /**
     * Show in an 'alert' dialog
     * @param message The text to go into the alert box
     */
    public static void alert(String message)
    {
        ScriptSessions.addFunctionCall("alert", message);
    }

    /**
     * Show a 'confirm' dialog
     * @param message The text to go into the alert box
     * @param callback The function to be called when a browser replies
     */
    public static void confirm(String message, Callback<Boolean> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + "confirm", message);

        if (callback != null)
        {
            String key = CallbackHelperFactory.get().saveCallback(callback, Boolean.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

   /**
     * Show a 'prompt' dialog
     * @param message The text to go into the alert box
     * @param callback The function to be called when a browser replies
     */
    public static void prompt(String message, Callback<String> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + "prompt", message);

        if (callback != null)
        {
            String key = CallbackHelperFactory.get().saveCallback(callback, String.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        ScriptSessions.addScript(script);
    }

    /**
     * Attempt to close this window
     */
    public static void close()
    {
        ScriptSessions.addFunctionCall("close");
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public static void setLocation(URI newPage)
    {
        setLocation(newPage.toASCIIString());
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public static void setLocation(URL newPage)
    {
        setLocation(newPage.toExternalForm());
    }

    /**
     * Navigate to a new page
     * @param newPage The page to navigate to
     */
    public static void setLocation(String newPage)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendData("window.location = '" + newPage + "';");

        ScriptSessions.addScript(script);
    }

    /**
     * Open a new browser child window, pointing at given <code>url</code> and
     * identified with a given <code>windowName</code>.
     * @see #open(String, String, OptionBuilder) for more details
     */
    public static void open(String url, String windowName)
    {
        ScriptSessions.addFunctionCall("open", url, windowName);
    }

    /**
     * Open a new browser child window, pointing at given <code>url</code> and
     * identified with a given <code>windowName</code>.
     * @see #open(String, String, OptionBuilder) for more details
     */
    public static void open(URL url, String windowName)
    {
        open(url.toExternalForm(), windowName);
    }

    /**
     * Open a new browser child window, pointing at given <code>uri</code> and
     * identified with a given <code>windowName</code>.
     * @see #open(String, String, OptionBuilder) for more details
     */
    public static void open(URI uri, String windowName)
    {
        open(uri.toASCIIString(), windowName);
    }

    /**
     * Open a new browser child window, pointing at given <code>url</code> and
     * identified with a given <code>windowName</code>.
     * @param windowName The window name can be one of the special values:
     * _blank, _parent, _self, _top.
     */
    public static void open(String url, String windowName, OptionBuilder builder)
    {
        ScriptSessions.addFunctionCall("open", url, windowName, builder.createWindowFeatures());
    }

    /**
     * Open a new browser child window, pointing at given <code>url</code> and
     * identified with a given <code>windowName</code>.
     * @see #open(String, String, OptionBuilder) for more details
     */
    public static void open(URL url, String windowName, OptionBuilder builder)
    {
        open(url.toExternalForm(), windowName, builder);
    }

    /**
     * Open a new browser child window, pointing at given <code>uri</code> and
     * identified with a given <code>windowName</code>.
     * @see #open(String, String, OptionBuilder) for more details
     */
    public static void open(URI uri, String windowName, OptionBuilder builder)
    {
        open(uri.toASCIIString(), windowName, builder);
    }

    /**
     * Aide to building windowFeatures strings.
     * The list of window options was taken from
     * <a href="http://developer.mozilla.org/En/DOM:window.open">the DMoz
     * reference to window.open</a>
     */
    public static class OptionBuilder
    {
        public OptionBuilder()
        {
            addOption("menubar", true);
            addOption("toolbar", true);
            addOption("location", true);
            addOption("status", true);
        }

        /**
         * Specifies the distance the new window is placed from the left side of
         * the work area for applications of the user's operating system to the
         * leftmost border (resizing handle) of the browser window.
         * The new window can not be initially positioned off-screen.
         */
        public OptionBuilder left(int value)
        {
            return addOption("left", value);
        }

        /**
         * Specifies the distance the new window is placed from the top side of
         * the work area for applications of the user's operating system to the
         * topmost border (resizing handle) of the browser window.
         * The new window can not be initially positioned off-screen.
         */
        public OptionBuilder top(int value)
        {
            return addOption("top", value);
        }

        /**
         * Specifies the height of the content area, viewing area of the new
         * secondary window in pixels. The height value includes the height of
         * the horizontal scrollbar if present.
         * The minimum required value is 100.
         */
        public OptionBuilder height(int value)
        {
            if (value < 100)
            {
                log.warn("Setting height < 100 will fail in some browsers");
            }

            return addOption("height", value);
        }

        /**
         * Specifies the width of the content area, viewing area of the new
         * secondary window in pixels. The width value includes the width of the
         * vertical scrollbar if present.
         * The width value does not include the sidebar if it is expanded.
         * The minimum required value is 100.
         */
        public OptionBuilder width(int value)
        {
            if (value < 100)
            {
                log.warn("Setting width < 100 will fail in some browsers");
            }

            return addOption("width", value);
        }

        /**
         * If used, then the new secondary window is requested to not render the
         * menu-bar.
         * <p>
         * Certain browsers may ignore this hint in some situations.
         */
        public OptionBuilder hideMenubar()
        {
            return addOption("menubar", false);
        }

        /**
         * If used, then the new secondary window is requested to not render the
         * navigation toolbar. Some browsers may extend this to not render any
         * toolbars.
         * <p>
         * Certain browsers may ignore this hint in some situations.
         */
        public OptionBuilder hideToolbar()
        {
            return addOption("toolbar", false);
        }

        /**
         * If used, then the new secondary window is requested not to render the
         * Location/Address bar
         * <p>
         * Certain browsers may ignore this hint in some situations.
         */
        public OptionBuilder hideLocation()
        {
            return addOption("location", false);
        }

        /**
         * If used, then the new secondary window is requested to not display a
         * status bar.
         * <p>
         * Many browsers ignore this hint.
         */
        public OptionBuilder hideStatus()
        {
            return addOption("status", false);
        }

        /**
         * If used, the new secondary window is requested to not be resizable.
         * <p>
         * Many browsers ignore this hint, and its use use is <strong>strongly</strong>
         * discouraged for accessibility reasons
         */
        public OptionBuilder notResizable()
        {
            return addOption("resizable", false);
        }

        /**
         * If used, the new secondary window is requested to not show horizontal
         * and/or vertical scrollbar(s) if the document doesn't fit into the
         * window's viewport.
         * Certain browsers ignore this hint, and its use use is
         * <strong>strongly</strong> discouraged for accessibility reasons
         */
        public OptionBuilder hideScrollbars()
        {
            return addOption("scrollbars", false);
        }

        /**
         * Helper to prevent lots of cut and paste in adding new options.
         */
        private OptionBuilder addOption(String name, Object value)
        {
            options.put(name, value);
            return this;
        }

        /**
         * Create a string fit for the <code>windowFeatures</code> parameter
         * to window.open(url, name windowFeatures);
         */
        protected String createWindowFeatures()
        {
            StringBuilder reply = new StringBuilder();

            for (Map.Entry<String, Object> entry : options.entrySet())
            {
                if (reply.length() > 0)
                {
                    reply.append(',');
                }

                reply.append(entry.getKey());
                reply.append("=");
                reply.append(entry.getValue());
            }
            return reply.toString();
        }

        /**
         * The options that we are using.
         */
        private final Map<String, Object> options = new HashMap<String, Object>();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Window.class);
}
