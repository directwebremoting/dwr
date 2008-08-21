/*
 * Copyright 2008 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.guice.util;

/**
 * A partially implemented handler for objects contained in contexts that
 * are closing. Concrete extensions of this class only have to define
 * {@code close} and have a constructor that calls {@code super(T.class)}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractContextCloseHandler<T> implements ContextCloseHandler<T>
{
    protected AbstractContextCloseHandler(Class<T> type)
    {
        this.type = type;
    }

    public abstract void close(T object) throws Exception;

    public Class<T> type()
    {
        return type;
    }

    private final Class<T> type;
}
