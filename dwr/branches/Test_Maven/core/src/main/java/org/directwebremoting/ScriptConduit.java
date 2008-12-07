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
package org.directwebremoting;

import java.io.IOException;

/**
 * While a Marshaller is processing a request it can register a ScriptConduit
 * with the ScriptSession to say - pass scripts straight to me and bypass the
 * temporary storage area.
 * This interface allows this to happen.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ScriptConduit
{
    /**
     * Add a script to the list waiting for remote execution.
     * The version automatically wraps the string in a ClientScript object.
     * @param script The script to execute
     * @return true if this ScriptConduit handled the script.
     * @throws IOException If the script can not go out via this conduit.
     */
    public boolean addScript(String script) throws IOException;

    /**
     * Called to flush any scripts written to the conduit
     * @throws IOException
     */
    public void flush() throws IOException;
}