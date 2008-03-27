package org.directwebremoting.proxy.openajax;

import java.util.List;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.PublishEvent;
import org.directwebremoting.event.PublishListener;

/**
 * OpenAjaxSynchronizingPublishListener is responsible for taking events
 * from a server-side {@link PubSubHub} and passing them on to a client side
 * OpenAjax hub.
 * <p>TODO: Currently we are passing every message that the server hub knows of
 * to all the relevant clients, but we should perhaps maintain a list of 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class OpenAjaxSynchronizingPublishListener implements PublishListener
{
    /**
     * {@link OpenAjaxSynchronizingPublishListener}s need to reference an
     * {@link OpenAjaxSynchronizer} to know who to publish to
     * @param openAjaxSynchronizer the source of enrolled {@link ScriptSession}s
     */
    public OpenAjaxSynchronizingPublishListener(OpenAjaxSynchronizer openAjaxSynchronizer)
    {
        this.openAjaxSynchronizer = openAjaxSynchronizer;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.proxy.openajax.PublishListener#eventHappened(org.directwebremoting.proxy.openajax.PublishEvent)
     */
    public void publishHappened(PublishEvent ev)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("OpenAjax.publish(")
              .appendData(ev.getPrefix())
              .appendScript(",")
              .appendData(ev.getName())
              .appendScript(");");

        List<ScriptSession> scriptSessions = openAjaxSynchronizer.getEnrolledScriptSessions();

        if (ev.getScriptSessionId().equals(PubSubHub.ANY_SCRIPT_SESSION))
        {
            for (ScriptSession scriptSession : scriptSessions)
            {
                scriptSession.addScript(script);
            }
        }
        else
        {
            for (ScriptSession scriptSession : scriptSessions)
            {
                if (scriptSession.getId().equals(ev.getScriptSessionId()))
                {
                    scriptSession.addScript(script);
                }
            }
        }
    }

    /**
     * the source of enrolled {@link ScriptSession}s
     */
    private OpenAjaxSynchronizer openAjaxSynchronizer;
}
