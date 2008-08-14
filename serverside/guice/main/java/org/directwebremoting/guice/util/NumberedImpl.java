/*
 * Copyright 2008 Tim Peierls
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
package org.directwebremoting.guice.util;

import java.lang.annotation.Annotation;


class NumberedImpl implements Numbered
{
    public NumberedImpl(long value)
    {
        this.value = value;
    }

    public long value()
    {
        return this.value;
    }

    public Class<? extends Annotation> annotationType()
    {
        return Numbered.class;
    }

    @Override
    public boolean equals(Object t)
    {
        if (!(t instanceof Numbered))
        {
            return false;
        }

        Numbered that = (Numbered) t;
        return this.value == that.value();
    }

    @Override
    public int hashCode()
    {
        // Annotation spec sez:
        return 127 * "value".hashCode() ^ (int)(value ^ (value >>> 32));
    }

    @Override
    public String toString()
    {
        return String.format("@%s(value=%d)", Numbered.class.getName(), value);
    }

    private final long value;
}
