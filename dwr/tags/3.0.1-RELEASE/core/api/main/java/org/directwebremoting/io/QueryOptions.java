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
package org.directwebremoting.io;

/**
 * Contains valid dojo data Read API values for the queryOptions
 * object (see http://www.dojotoolkit.org/book/dojo-book-0-9/part-3-programmatic-dijit-and-dojo/what-dojo-data/dojo-data-design/read-api)
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public final class QueryOptions
{

    public QueryOptions()
    {
        this.deep = false;
        this.ignoreCase = false;
    }

    public QueryOptions(boolean deep, boolean ignoreCase)
    {
        this.deep = deep;
        this.ignoreCase = ignoreCase;
    }

    /**
     * Not currently supported
     *
     * @return false
     */
    @Deprecated
    public boolean isDeep()
    {
        return deep;
    }

    /**
     * An {@link org.directwebremoting.datasync.Index} will include
     * or exclude items detecting capital letters.
     *
     * @return false unless the client request determines otherwise
     */
    public boolean isIgnoreCase()
    {
        return ignoreCase;
    }

    @Override
    public String toString()
    {
        return "QueryOptions[deep=" + deep + ", ignoreCase=" + ignoreCase + "]";
    }

    @Override
    public int hashCode()
    {
        int hash = 1789;
        if (deep)
        {
            hash += 1234;
        }
        if (ignoreCase)
        {
            hash += 5644;
        }
        return hash;
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

        QueryOptions that = (QueryOptions) obj;
        return deep == that.deep && ignoreCase == that.ignoreCase;
    }

    private final boolean deep;
    private final boolean ignoreCase;

}
