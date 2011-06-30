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

/**
 * Most listeners have a parameter that inherits from {@link EventObject}, but
 * not JMS which just uses a 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface MessageListener
{
    /**
     * Passes a message to the listener.
     * @param message the message passed to the listener
     */
    void onMessage(MessageEvent message);
}
