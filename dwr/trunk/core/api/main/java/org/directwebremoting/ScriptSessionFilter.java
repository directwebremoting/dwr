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

/**
 * A way to loop over a set {@link ScriptSession}s looking for instances that
 * match some pattern.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ScriptSessionFilter
{
    /**
     * Does this {@link ScriptSession} match according to the matching pattern
     * declared by this filter.
     * @param session The ScriptSession to check for a match
     * @return true if the session matches, false otherwise
     */
    public boolean match(ScriptSession session);
}
