package org.directwebremoting.guice.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.google.inject.Provider;

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
