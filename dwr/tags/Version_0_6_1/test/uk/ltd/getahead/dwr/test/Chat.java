package uk.ltd.getahead.dwr.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Chat
{
    /**
     * @param message A new message to add to the list
     * @return The last 10 messages
     */
    public Map[] addMessage(String message)
    {
        if (message != null && message.trim().length() > 0)
        {
            if (message.length() > 256) message = message.substring(0, 256);

            Map data = new HashMap();
            data.put("message", message.replace('<', '[')); //$NON-NLS-1$
            data.put("id", new Long(System.currentTimeMillis())); //$NON-NLS-1$
            messages.add(data);

            while (messages.size() > 10)
            {
                messages.remove(0);
            }
            cache = (Map[]) messages.toArray(new Map[messages.size()]);
        }

        return cache;
    }

    /**
     * @return The last 10 messages
     */
    public Map[] getMessages()
    {
        return cache;
    }

    private static List messages = new ArrayList();
    private static Map[] cache = new Map[0];
}
