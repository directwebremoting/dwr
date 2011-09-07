/*
 * Copyright 2006 Maik Schreiber <blizzy AT blizzy DOT de>
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
package org.directwebremoting.annotations;

import org.directwebremoting.extend.Creator;

/**
 * A scripting scope.
 * See {@link org.directwebremoting.extend.Creator} for details.
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public enum ScriptScope
{
    /**
     * @see Creator#APPLICATION
     */
    APPLICATION(Creator.APPLICATION),

    /**
     * @see Creator#SESSION
     */
    SESSION(Creator.SESSION),

    /**
     * @see Creator#SCRIPT
     */
    SCRIPT(Creator.SCRIPT),

    /**
     * @see Creator#REQUEST
     */
    REQUEST(Creator.REQUEST),

    /**
     * @see Creator#PAGE
     */
    PAGE(Creator.PAGE);

    /**
     * Link a ScriptScope to constant from Creator
     * @param value The scope string constant 
     */
    private ScriptScope(String value)
    {
        this.value = value;
    }

    /**
     * Accessor for the scope string constant from Creator
     * @return One of "application", "session", etc.
     */
    String getValue()
    {
        return value;
    }

    /**
     * The scope constant value
     */
    private String value;
}
