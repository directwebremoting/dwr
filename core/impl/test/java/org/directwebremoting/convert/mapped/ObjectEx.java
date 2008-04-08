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
 * An example that is mapped to the object converter
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectEx
{
    public ObjectEx()
    {
    }

    public ObjectEx(String name)
    {
        this.name = name;
    }

    public String name;

    private String hidden;

    @Override
    public String toString()
    {
        return "ObjectEx[" + name + "]";
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

        ObjectEx that = (ObjectEx) obj;

        if (!CompareUtil.equals(this.name, that.name))
        {
            return false;
        }

        return true;
    }

    void shutupStupidCompiler() { System.getProperty(name, hidden); }
}
