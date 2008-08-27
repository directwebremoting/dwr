/*
 * Copyright 2008 Tim Peierls
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
package org.directwebremoting.guice.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

class ExceptionLoggingCloseableHandler extends AbstractContextCloseHandler<Closeable>
{
    ExceptionLoggingCloseableHandler(List<Exception> exceptions)
    {
        super(Closeable.class);
        this.exceptions = exceptions;
    }

    @Override
    public void close(Closeable closeable) throws IOException
    {
        try
        {
            closeable.close();
        }
        catch (IOException e)
        {
            exceptions.add(e);
            throw e;
        }
    }

    private final List<Exception> exceptions;
}
