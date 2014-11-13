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
package org.directwebremoting.event;

import java.util.EventObject;

import org.directwebremoting.ScriptSession;

/**
 * This is the class representing event notifications for changes to script
 * sessions within a web application.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ScriptSessionEvent extends EventObject
{
    /**
     * Construct a session event from the given source.
     * @param source The ScriptSession that is being created/destroyed
     */
    public ScriptSessionEvent(ScriptSession source)
    {
        super(source);
    }

    /**
     * Return the session that changed.
     * @return The newly created/destroyed ScriptSession
     */
    public ScriptSession getSession()
    {
        return (ScriptSession) super.getSource();
    }
}
