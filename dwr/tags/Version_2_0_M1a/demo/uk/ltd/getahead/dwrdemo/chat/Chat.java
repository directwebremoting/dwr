package uk.ltd.getahead.dwrdemo.chat;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwrutil.DwrUtil;
import org.directwebremoting.util.Logger;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Chat
{
    /**
     * @param text The new message text to add
     */
    public void addMessage(String text)
    {
        if (text != null && text.trim().length() > 0)
        {
            messages.addFirst(new Message(text));
            while (messages.size() > 10)
            {
                messages.removeLast();
            }
        }

        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        try
        {
            OutboundVariable ov = wctx.toJavascript(messages);
            String eval = ov.getInitCode() + "receiveMessages(" + ov.getAssignCode() + ");"; //$NON-NLS-1$ //$NON-NLS-2$

            // Loop over all the users on the current page
            Collection pages = wctx.getScriptSessionsByPage(currentPage);
            for (Iterator it = pages.iterator(); it.hasNext();)
            {
                ScriptSession otherSession = (ScriptSession) it.next();
                otherSession.addScript(eval);
            }
        }
        catch (MarshallException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * @param text The new message text to add
     */
    public void addMessage0(String text)
    {
        if (text != null && text.trim().length() > 0)
        {
            messages.addFirst(new Message(text));
            while (messages.size() > 10)
            {
                messages.removeLast();
            }
        }

        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        try
        {
            StringBuffer chatlog = new StringBuffer();
            for (Iterator it = messages.iterator(); it.hasNext();)
            {
                Message message = (Message) it.next();
                chatlog.insert(0, "<div>"); //$NON-NLS-1$
                chatlog.insert(0, message.getText());
                chatlog.insert(0, "</div>"); //$NON-NLS-1$
            }

            DwrUtil util = new DwrUtil(wctx.getScriptSessionsByPage(currentPage));
            util.setValue("chatlog", chatlog.toString()); //$NON-NLS-1$
        }
        catch (MarshallException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * The current set of messages
     */
    private LinkedList messages = new LinkedList();

    /**
     * 
     */
    public void pingMe()
    {
        WebContext wctx = WebContextFactory.get();
        final ScriptSession scriptSession = wctx.getScriptSession();
        Thread worker = new Thread(new Runnable()
        {
            public void run()
            {
                int count = 0;
                while (count < 100)
                {
                    count++;
                    try
                    {
                        Thread.sleep(1000);
                        log.debug("ping: " + count);
                        scriptSession.addScript("DWRUtil.setValue('ping', 'count=" + count + "');"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    catch (Exception ex)
                    {
                        log.warn("Waking:", ex); //$NON-NLS-1$
                    }
                }
            }
        });
        worker.start();
    }

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(Chat.class);
}
