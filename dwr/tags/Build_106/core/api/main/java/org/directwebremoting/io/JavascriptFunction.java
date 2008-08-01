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
package org.directwebremoting.io;

/**
 * Represents a callback function, passed in from a client for later execution.
 * <p>A JavascriptFunction is tied to a specific function in a specific browser
 * page. In this way the eval of a JavascriptFunction is outside of the normal
 * execution scoping provided by {@link org.directwebremoting.Browser}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface JavascriptFunction
{
    /**
     * Execute the function.
     * TODO: At some stage it would be good to allow the final parameter to be
     * a Callback....
     * @param params The data to pass to the server
     */
    public void execute(Object... params);

    /**
     * A small amount of data is stored on the client to track the remotely.
     * accessible objects. To clear this data, the function needs to be cleared
     * on the server.
     */
    public void close();

    /**
     * Execute the function, and clear it's data from the client.
     * @param params The data to pass to the server
     * @see #execute
     * @see #close
     */
    public void executeAndClose(Object... params);
}
