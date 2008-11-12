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
package org.directwebremoting.json.parse.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SimpleJsonDecoder extends StatefulJsonDecoder
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#createObject(Object, String)
     */
    @Override
    protected Map<String, Object> createObject(Object parent, String propertyName)
    {
        return new HashMap<String, Object>();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#createArray(Object, String)
     */
    @Override
    protected ArrayList<Object> createArray(Object parent, String propertyName)
    {
        return new ArrayList<Object>();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#addMember(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    protected void addMemberToObject(Object parent, String propertyName, Object member)
    {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) parent;
        map.put(propertyName, member);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#addMember(java.lang.Object)
     */
    @Override
    protected void addMemberToArray(Object parent, Object member)
    {
        @SuppressWarnings("unchecked")
        List<Object> array = (List<Object>) parent;
        array.add(member);
    }
}
