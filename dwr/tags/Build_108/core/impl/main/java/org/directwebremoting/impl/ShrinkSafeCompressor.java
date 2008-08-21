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
package org.directwebremoting.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.directwebremoting.extend.Compressor;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;

/**
 * JavaScript Compression Implementation using Dojo ShrinkSafe.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ShrinkSafeCompressor implements Compressor
{
    /**
     * @throws Exception Likely if YUI is present, and not ShrinkSafe
     */
    public ShrinkSafeCompressor() throws Exception
    {
        global = Main.getGlobal();
        ToolErrorReporter errorReporter = new ToolErrorReporter(false, global.getErr());
        Main.shellContextFactory.setErrorReporter(errorReporter);
        global.init(Main.shellContextFactory);

        // This should fail if ShrinkSafe is not in classpath
        try
        {
            compressReaderMethod = Context.class.getMethod("compressReader", Context.class, Scriptable.class, Script .class, String.class, String.class, Integer.TYPE, Object.class);
        }
        catch (NoSuchMethodException ex)
        {
            throw new InstantiationException("Context.compressReader() is not available, assuming Rhino is not here from custom_rhino.jar, aka ShrinkSafe");
        }

        // Do a trial compression to check
        compressJavaScript("");
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
                Script script = Main.loadScriptFromSource(cx, source, "compress", 1, null);

                try
                {
                    //return cx.compressReader(global, script, source, "compress", 1, null);
                    return compressReaderMethod.invoke(cx, global, script, source, "compress", 1, null);
                }
                catch (InvocationTargetException ex)
                {
                    final Throwable target = ex.getTargetException();
                    if (target instanceof Exception)
                    {
                        thrown[0] = (Exception) target;
                    }
                    else
                    {
                        thrown[0] = ex;
                    }
                }
                catch (Exception ex)
                {
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
