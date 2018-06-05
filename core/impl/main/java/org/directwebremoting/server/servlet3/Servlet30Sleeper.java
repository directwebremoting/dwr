package org.directwebremoting.server.servlet3;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.impl.BaseSleeper;

/**
 * A Sleeper that works with Servlet 3.0 async servlets.
 * @author Mike Wilson
 */
public class Servlet30Sleeper extends BaseSleeper
{
    /**
     * @throws IOException
     */
    public Servlet30Sleeper(HttpServletRequest request, HttpServletResponse response, RealScriptSession scriptSession, ScriptConduit conduit) throws IOException
    {
        super(response, scriptSession, conduit);
        this.request = request;
        this.response = response;

        workInProgress = true; // block doing work until we officially enter sleep
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#enterSleep()
     */
    @Override
    protected void enterSleep()
    {
        asyncCtx = request.startAsync(request, response);
        asyncCtx.setTimeout(-1);

        synchronized (workLock)
        {
            if (queuedWork) {
                asyncCtx.start(new Runnable() {
                    public void run() {
                        resumeWork();
                    }
                });
            } else {
                workInProgress = false; // open up for doing new work
            }
            queuedWork = false;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#wakeUp()
     */
    @Override
    protected void wakeUp()
    {
        synchronized (workLock)
        {
            if (workInProgress) {
                queuedWork = true;
            } else {
                asyncCtx.start(new Runnable() {
                    public void run() {
                        resumeWork();
                    }
                });
                workInProgress = true;
            }
        }
    }

    /**
     * The method that should be called by a background thread when we are being awakened
     */
    private void resumeWork()
    {
        while(true) {
            doWork();
            synchronized (workLock)
            {
                if (queuedWork) {
                    // New work has arrived so fall through to take another spin in the loop
                    queuedWork = false;
                } else {
                    // No work in the queue so exit loop and go to sleep again
                    workInProgress = false;
                    return;
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.BaseSleeper#close()
     */
    @Override
    protected void close()
    {
        if (asyncCtx != null) {
            asyncCtx.complete();
        }
    }

    // Set at construction
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    // Internal state
    private AsyncContext asyncCtx = null;
    private final Object workLock = new Object();
    private boolean workInProgress = false;
    private boolean queuedWork = false;
}
