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
package org.directwebremoting.dwrp;

import java.util.Map;

import org.directwebremoting.extend.OutboundVariable;

/**
 * A helper class for people that want to implement {@link OutboundVariable}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface MapOutboundVariable extends OutboundVariable
{
    /**
     * Recursive OutboundVariable need to be given their children after
     * construction because we need to check for potentially recursive
     * conditions.
     * @param children The contained objects
     */
     void setChildren(Map<String, OutboundVariable> children);
}
