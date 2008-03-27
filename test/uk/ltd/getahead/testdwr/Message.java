package uk.ltd.getahead.testdwr;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Message
{
    /**
     * @param newtext
     */
    public Message(String newtext)
    {
        text = newtext;
        if (text.length() > 256)
        {
            text = text.substring(0, 256);
        }
        text = text.replace('<', '[').replace('&', '_');
    }

    /**
     * @return The ID
     */
    public long getId()
    {
        return id;
    }

    /**
     * @return The text of this message
     */
    public String getText()
    {
        return text;
    }

    long id = System.currentTimeMillis();
    String text;
}
