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
package jsx3.lang;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.Context;

/**
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class Object extends Context
{
    /**
     * All reverse ajax proxies need context to work from
     * @param parent The parent context
     * @param extension The script to take us from the parent to this object
     */
    public Object(Context parent, String extension)
    {
        super(parent, extension);
    }

    /**
     * If we need to execute a (drapgen generated) function that looks like a
     * setter, but that returns some data we have a problem because DWR assumes
     * that it is a getter and doesn't add the final ScriptBuffer, so this
     * temporary function will do just that.
     * This is a bit of a nasty hack that I hope we can get rid of
     */
    public void ignoreReturn()
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(getContextPath().replaceFirst(".$", ";"));
        ScriptSessions.addScript(script);
    }
}
