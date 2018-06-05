package org.directwebremoting.impl;

import java.lang.reflect.Method;

import org.directwebremoting.extend.Compressor;
import org.directwebremoting.util.Loggers;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;

/**
 * JavaScript Compression Implementation using Dojo ShrinkSafe.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Randy Jones (Updater)
 */
public class ShrinkSafeCompressor implements Compressor
{
    /**
     * @throws Exception Likely if YUI is present, and not ShrinkSafe
     */
    public ShrinkSafeCompressor() throws Exception
    {
        // This should fail if ShrinkSafe is not in classpath
        try
        {
            global = Main.getGlobal();
            ToolErrorReporter errorReporter = new ToolErrorReporter(false, global.getErr());
            Main.shellContextFactory.setErrorReporter(errorReporter);

            // Do a trial compression to check
            compressJavaScript("");
        }
        catch (NoClassDefFoundError ex)
        {
            throw new InstantiationException("Could not setup ShrinkSafeCompressor because a class is missing, assuming shrinksafe.jar and js.jar are not in the classpath.");
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.error("ShrinkSafeCompressor startup", ex);
            throw new InstantiationException("Could not setup ShrinkSafeCompressor, assuming shrinksafe.jar and js.jar are not in the classpath. Exception caught was " + ex);
        }

    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Compressor#compressJavaScript(java.lang.String)
     */
    public String compressJavaScript(final String source) throws Exception
    {
        final Exception[] thrown = new Exception[1];
        String reply = (String) Main.shellContextFactory.call(new ContextAction()
        {
            public Object run(Context cx)
            {
                try
                {
                    // The shrinksafe Compressor only obfuscates the javascript. Line breaks are removed
                    // by the dojo build. Adding the removal of line breaks to make the optimization that
                    // would come from using the dojo build. See
                    // http://svn.dojotoolkit.org/src/tags/release-1.5.0/util/buildscripts/jslib/buildUtil.js
                    // in the buildUtil.optimizeJs function which has the following:
                    // if(optimizeType.indexOf("shrinksafe") == 0 || optimizeType == "packer"){
                    //     //Apply compression using custom compression call in Dojo-modified rhino.
                    //     fileContents = new String(Packages.org.dojotoolkit.shrinksafe.Compressor.compressScript(fileContents, 0, 1, stripConsole));
                    //     if(optimizeType.indexOf(".keepLines") == -1){
                    //         fileContents = fileContents.replace(/[\r\n]/g, "");
                    //     }
                    // }
                    String obfuscated = org.dojotoolkit.shrinksafe.Compressor.compressScript(source, 0, 1, false, null);
                    return obfuscated.replaceAll("[\\r\\n]", "");
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    thrown[0] = ex;
                }
                return null;
            }
        });

        if (thrown[0] != null)
        {
            throw thrown[0];
        }
        else
        {
            return reply;
        }
    }

    protected Global global;
    protected Method compressReaderMethod;
}
