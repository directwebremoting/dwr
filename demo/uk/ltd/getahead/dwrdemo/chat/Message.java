package uk.ltd.getahead.dwrdemo.chat;

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
