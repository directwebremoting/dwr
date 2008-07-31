/**
 * 
 */
package org.example.testdwr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ui.ScriptProxy;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Stress
{
    public Stress()
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
            Thread worker = new Thread(new Runnable()
            {
                /* (non-Javadoc)
                 * @see java.lang.Runnable#run()
                 */
                public void run()
                {
                    try
                    {
                        log.debug("Stress: Starting server-side thread");
                        int pings = 0;

                        while (Stress.this.publishing)
                        {
                            ScriptProxy.addFunctionCall("serverPing", pings);

                            pings++;
                            Thread.sleep(delay);
                        }
                    }
                    catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                    finally
                    {
                        log.debug("Stress: Stopping server-side thread");
                    }
                }        
            });

            worker.start();
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
     * Are we in a server publish cycle
     */
    protected boolean publishing = false;

    /**
     * How long between server publishes
     */
    private int delay = 1000;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Stress.class);
}
