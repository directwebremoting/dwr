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

import com.google.inject.Provider;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * A provider implementation that uses the FutureTask machinery.
 * @author Tim Peierls [tim at peierls dot net]
 */
class FutureTaskProvider<T> extends FutureTask<T> implements InstanceProvider<T>
{
    FutureTaskProvider(final Provider<T> creator)
    {
        super(new Callable<T>()
        {
            public T call()
            {
                return creator.get();
            }
        });
    }

    @Override
    public T get()
    {
        try
        {
            return super.get();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            return null;
        }
        catch (ExecutionException e)
        {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException)
            {
                throw (RuntimeException) t;
            }
            else if (t instanceof Error)
            {
                throw (Error) t;
            }
            else
            {
                throw new IllegalStateException("unexpected Exception: " + t, t);
            }
        }
    }
}
