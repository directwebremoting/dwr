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


class InitParamImpl implements InitParam
{
    public InitParamImpl(ParamName value)
    {
        assert value != null;
        this.value = value;
        this.id = 0L;
    }

    public InitParamImpl(ParamName value, long id)
    {
        assert value != null;
        this.value = value;
        this.id = id;
    }

    public ParamName value()
    {
        return this.value;
    }

    public long id()
    {
        return this.id;
    }

    public Class<? extends Annotation> annotationType()
    {
        return InitParam.class;
    }

    @Override
    public boolean equals(Object t)
    {
        if (!(t instanceof InitParam))
        {
            return false;
        }

        InitParam that = (InitParam) t;
        return this.value.equals(that.value()) && this.id() == that.id();
    }

    @Override
    public int hashCode()
    {
        // Annotation spec sez:
        return (127 * "value".hashCode() ^ value.hashCode())
             + (127 * "id".hashCode() ^ (int)(id ^ (id >>> 32)));
    }

    @Override
    public String toString()
    {
        return String.format("@%s(value=%s, id=%d)", InitParam.class.getName(), value, id);
    }

    private final ParamName value;
    private final long id;
}
