package uk.ltd.getahead.dwrdemo.chat;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Message
{
    /**
     * @param newtext the new message text
     */
    public Message(String newtext)
    {
        text = newtext;

        if (text.length() > 256)
        {
            text = text.substring(0, 256);
        }
        text = text.replace('<', '[');
        text = text.replace('&', '_');

        try
        {
            if (text.startsWith("http://")) //$NON-NLS-1$
            {
                URL url = new URL(text);
                text = "<a href='#' onclick='window.open(\"" + url.toExternalForm() + "\", \"\", \"resizable=yes,scrollbars=yes,status=yes\");'>" + url.toExternalForm() + "</a>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }
        catch (MalformedURLException ex)
        {
            // Ignore - it's not a URL
        }
    }

    /**
     * @return the message id
     */
    public long getId()
    {
        return id;
    }

    /**
     * @return the message itself
     */
    public String getText()
    {
        return text;
    }

    private long id = System.currentTimeMillis();
    private String text;
}
