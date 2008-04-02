/**
 * 
 */
package org.getahead.dwrdemo.stress;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Stress implements Runnable
{
    public Stress()
    {
        session = WebContextFactory.get().getScriptSession();
    }

    /**
     * Call for use in client side stress testing
     */
    public void ping()
    {
    }

    /**
     * 
     * @param publishing
     */
    public void setPublishing(boolean publishing)
    {
        this.publishing = publishing;

        if (publishing)
        {
            Thread worker = new Thread(this);
            worker.setName("Stress on ScriptSession: " + session.getId().substring(10));
            worker.start();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            log.debug("Stress: Starting server-side thread");
            int pings = 0;

            while (publishing)
            {
                ScriptProxy pages = new ScriptProxy(session);
                pages.addFunctionCall("serverPing", new Integer(pings));

                pings++;
                Thread.sleep(delay);
            }

            log.debug("Stress: Stopping server-side thread");
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }        

    /**
     * Accessor for the delay between server publishes
     * @param delay The new delay between server publishes
     */
    public void setHitDelay(int delay)
    {
        this.delay = delay;
    }

    /**
     * Our key to get hold of ServerContexts
     */
    private ScriptSession session;

    /**
     * Are we in a server publish cycle
     */
    private boolean publishing = false;

    /**
     * How long between server publishes
     */
    private int delay = 1000;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Stress.class);
}
