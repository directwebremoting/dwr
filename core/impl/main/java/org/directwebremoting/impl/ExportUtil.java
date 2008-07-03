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
package org.directwebremoting.impl;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExportUtil
{
    /**
     * Is this class the System export as from the system dwr.xml?
     * @param scriptName The script name of the exported object
     * @return True if the scriptName represents the System export
     * @see org.directwebremoting.export.System
     */
    public static boolean isSystemClass(String scriptName)
    {
        return "__System".equals(scriptName);
    }
}
