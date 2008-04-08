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
package org.directwebremoting.convert.mapped;

import org.directwebremoting.util.CompareUtil;

/**
 * An example that is mapped to the object converter, which requires use of the
 * force parameter to read the name member
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectForceEx
{
    public ObjectForceEx()
    {
    }

    public ObjectForceEx(String name)
    {
        this.name = name;
    }

    private String name;

    @Override
    public String toString()
    {
        return "ObjectForceEx[" + name + "]";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        ObjectForceEx that = (ObjectForceEx) obj;

        if (!CompareUtil.equals(this.name, that.name))
        {
            return false;
        }

        return true;
    }

    void shutupStupidCompiler() { System.getProperty(name, name); }
}
