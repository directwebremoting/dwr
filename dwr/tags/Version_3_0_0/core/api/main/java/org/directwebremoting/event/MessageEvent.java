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

import org.directwebremoting.Hub;

/**
 * An MessageEvent is fired to a set of {@link MessageListener}s by the DWR
 * {@link Hub}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface MessageEvent
{
    /**
     * @see EventObject#getSource()
     * @return the hub that processed this message
     */
    public Hub getHub();

    /**
     * We convert the data (if the message is from the client) as late as
     * possible so the message recipient can choose what type it should be.
     * @param <T> The type that we are converting to
     * @param asType The type that we are converting to
     * @return The data coerced into the required type
     */
    public <T> T getData(Class<T> asType);

    /**
     * WARNING: This method is for internal use only. It may well disappear at
     * some stage in the future
     * Sometimes we just want to get at whatever the data was originally without
     * any conversion.
     * @return The original data probably of type RawData
     */
    public Object getRawData();
}
