/*
 * Copyright 2007 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.guice;

import java.lang.annotation.Annotation;


class FilteringImpl implements Filtering
{
    public FilteringImpl()
    {
        this.value = "";
    }

    public FilteringImpl(String value)
    {
        if (value == null)
        {
            throw new NullPointerException("@Filtering");
        }
        this.value = value;
    }

    public String value()
    {
        return this.value;
    }

    public Class<? extends Annotation> annotationType()
    {
        return Filtering.class;
    }

    @Override
    public boolean equals(Object t)
    {
        if (!(t instanceof Filtering))
        {
            return false;
        }

        Filtering that = (Filtering) t;
        return this.value.equals(that.value());
    }

    @Override
    public int hashCode()
    {
        // Annotation spec sez:
        return 127 * "value".hashCode() ^ value.hashCode();
    }

    @Override
    public String toString()
    {
        return "@" + Filtering.class.getName() + "(value=" + value + ")";
    }

    private final String value;
}
