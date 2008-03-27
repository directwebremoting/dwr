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
package org.directwebremoting.drapgen.generate.gi;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class GiProject
{
    protected final Map<String, GiType> sources = new HashMap<String, GiType>();

    /**
     * @param code
     */
    public void add(GiType code)
    {
        sources.put(code.getClassName(), code);
    }

    /**
     *
     */
    public Collection<GiType> getClasses()
    {
        return sources.values();
    }

    /**
     *
     */
    public GiType getClassByName(String name)
    {
        return sources.get(name);
    }

    /**
     * @param directory Where to write the XML
     * 
     */
    public void save(String directory) throws IOException
    {
        for (GiType code : sources.values())
        {
            code.writeDOM(directory);
        }
    }
}
