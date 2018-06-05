package com.example.dwr.reverseajax;

import java.util.LinkedList;

import org.directwebremoting.Browser;
import org.directwebremoting.ui.dwr.Util;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JavaChat
{
    /**
     * @param text The new message text to add
     */
    public void addMessage(String text)
    {
        // Make sure we have a list of the list 10 messages
        if (text != null && text.trim().length() > 0)
        {
            messages.addFirst(new Message(text));
            while (messages.size() > 10)
            {
                messages.removeLast();
            }
        }

        // Clear the input box in the browser that kicked off this page only
        Util.setValue("text", "");

        // For all the browsers on the current page:
        Browser.withCurrentPage(new Runnable()
        {
            public void run()
            {
                // Clear the list and add in the new set of messages
                Util.removeAllOptions("chatlog");
                Util.addOptions("chatlog", messages, "text");
            }
        });
    }

    /**
     * The current set of messages
     */
    private final LinkedList<Message> messages = new LinkedList<Message>();
}
