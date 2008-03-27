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

import java.util.EventListener;

/**
 * Implementations of this interface are notified of changes to the list of
 * active sessions in a web application.
 * @see ScriptSessionEvent
 * @see javax.servlet.http.HttpSessionListener
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ScriptSessionListener extends EventListener
{
    /**
     * Notification that a session was created.
     * @param ev the notification event
     */
    public void sessionCreated(ScriptSessionEvent ev);

    /**
     * Notification that a session is about to be invalidated.
     * @param ev the notification event
     */
    public void sessionDestroyed(ScriptSessionEvent ev);
}
