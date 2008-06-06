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


class ConvertingImpl implements Converting
{
    public ConvertingImpl(String match)
    {
        if (match == null)
        {
            throw new NullPointerException("@Converting(match==null)");
        }
        this.match = match;
        this.type = Void.class;
        this.impl = Void.class;
    }

    public ConvertingImpl(Class<?> type)
    {
        if (type == null)
        {
            throw new NullPointerException("@Converting(type==null)");
        }
        this.match = "";
        this.type = type;
        this.impl = Void.class;
    }

    public ConvertingImpl(Class<?> type, Class<?> impl)
    {
        if (type == null)
        {
            throw new NullPointerException("@Converting(type==null)");
        }
        if (impl == null)
        {
            throw new NullPointerException("@Converting(impl==null)");
        }
        this.match = "";
        this.type = type;
        this.impl = impl;
    }

    public String match()
    {
        return this.match;
    }

    public Class<?> type()
    {
        return this.type;
    }

    public Class<?> impl()
    {
        return this.impl;
    }

    public Class<? extends Annotation> annotationType()
    {
        return Converting.class;
    }

    @Override
    public boolean equals(Object t)
    {
        if (!(t instanceof Converting))
        {
            return false;
        }

        Converting that = (Converting) t;
        return this.match.equals(that.match())
            && this.type.equals(that.type())
            && this.impl.equals(that.impl());
    }

    @Override
    public int hashCode()
    {
        // Annotation spec sez:
        return (127 * "match".hashCode() ^ match.hashCode())
             + (127 * "type".hashCode() ^ type.hashCode())
             + (127 * "impl".hashCode() ^ impl.hashCode());
    }

    @Override
    public String toString()
    {
        return "@" + Converting.class.getName() +
               "(match=" + match +
               ",type=" + type.getName() +
               ",impl=" + impl.getName() +
               ")";
    }

    private final String match;
    private final Class<?> type;
    private final Class<?> impl;
}
