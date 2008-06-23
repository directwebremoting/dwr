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
package org.directwebremoting.extend;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.io.JavascriptFunction;

/**
 * Represents a callback function, passed in from a client for later execution.
 * <p>A DefaultJavascriptFunction is tied to a specific function in a specific browser
 * page. In this way the eval of a DefaultJavascriptFunction is outside of the normal
 * execution scoping provided by {@link org.directwebremoting.Browser}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultJavascriptFunction implements JavascriptFunction
{
    public DefaultJavascriptFunction(ScriptSession session, String id)
    {
        this.session = session;
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.JavascriptFunction#execute(java.lang.Object[])
     */
    public void execute(Object... params)
    {
        ScriptBuffer script = EnginePrivate.getRemoteExecuteFunctionScript(id, params);
        session.addScript(script);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.JavascriptFunction#close()
     */
    public void close()
    {
        ScriptBuffer script = EnginePrivate.getRemoteCloseFunctionScript(id);
        session.addScript(script);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.JavascriptFunction#executeAndClose(java.lang.Object[])
     */
    public void executeAndClose(Object... params)
    {
        execute(params);
        close();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "browser[" + session.getId() + "].functions[" + id + "](...)";
    }

    /**
     * The browser window that owns this function
     */
    private ScriptSession session;

    /**
     * The id into the function cache
     */
    private String id;
}
