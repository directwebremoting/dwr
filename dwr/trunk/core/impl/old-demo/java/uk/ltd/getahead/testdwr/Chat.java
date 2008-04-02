package uk.ltd.getahead.testdwr;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Chat
{
    /**
     * @param text
     * @return A list of the currently viewed messages
     */
    public List<Message> addMessage(String text)
    {
        if (text != null && text.trim().length() > 0)
        {
            messages.addFirst(new Message(text));
            while (messages.size() > 10)
            {
                messages.removeLast();
            }
        }

        return messages;
    }

    /**
     * @return A list of the currently viewed messages
     */
    public List<Message> getMessages()
    {
        return messages;
    }

    private static LinkedList<Message> messages = new LinkedList<Message>();
}
