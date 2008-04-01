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
package jsx3;

import java.util.Collection;

import jsx3.app.Server;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * A Factory class to allow access to GI components in the same way that the
 * global Javascript variable(s) do on the client.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class GI
{
    /**
     * Get access to a Server instance with the given name
     * @param scriptSessions A collection of ScriptSessions that we should act on.
     * @param name The name to get access to
     * @return A new Server proxy instance
     */
    public static Server getServer(Collection<ScriptSession> scriptSessions, String name)
    {
        ScriptProxy proxy = new ScriptProxy(scriptSessions);
        return new Server(null, name + ".", proxy);
    }

    /**
     * Get access to a Server instance with the given name
     * @param scriptSession The ScriptSessions that we should act on.
     * @param name The name to get access to
     * @return A new Server proxy instance
     */
    public static Server getServer(ScriptSession scriptSession, String name)
    {
        ScriptProxy proxy = new ScriptProxy(scriptSession);
        return new Server(null, name + ".", proxy);
    }
}
