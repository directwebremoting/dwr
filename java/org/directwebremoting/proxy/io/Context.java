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
package org.directwebremoting.proxy.io;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * Context is a way for a {@link ScriptProxy} to return a value that can only be
 * realized on the client.
 * A {@link Context} allows a {@link ScriptProxy} to return something, and have
 * server side code keep track of the things it is acting on.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Context
{
    /**
     * Create a new Context
     * @param parent Our parent context, or null if we start from scratch
     * @param extension A string to append to the parent context
     */
    public Context(Context parent, String extension, ScriptProxy scriptProxy)
    {
        this.parent = parent;
        this.extension = extension;
        this.scriptProxy = scriptProxy;
    }

    /**
     * Accessor for the string to append to the parent context to define us
     * @return the extension
     */
    protected String getContextPath()
    {
        String prefix = (parent == null) ? "" : parent.getContextPath();
        return prefix + extension;
    }

    /**
     * Accessor for the current script proxy
     * @return The current script proxy
     */
    protected ScriptProxy getScriptProxy()
    {
        return scriptProxy;
    }

    /**
     * Accessor for the current InitScript
     * @param initScript The new InitScript
     */
    protected void setInitScript(ScriptBuffer initScript)
    {
        this.initScript = initScript;
    }

    /**
     * Accessor for the current InitScript
     * @return The current InitScript
     */
    protected ScriptBuffer getInitScript()
    {
        return initScript;
    }

    /**
     * A destination for the generated scripts
     */
    private ScriptProxy scriptProxy;

    /**
     * When we're stand-alone, created from 'new' someone else want the start point
     */
    private ScriptBuffer initScript;

    /**
     * Our parent context, or null if we start from scratch
     */
    private Context parent;

    /**
     * A string to append to the parent context to get to where we are
     */
    private String extension;
}
