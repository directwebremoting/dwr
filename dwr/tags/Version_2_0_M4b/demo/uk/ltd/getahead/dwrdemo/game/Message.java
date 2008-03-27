package uk.ltd.getahead.dwrdemo.game;

/**
 * Something someone typed
 */
public class Message
{
    /**
     * @param newtext
     * @param author Who wrote this?
     * @param trusted Is the string trusted
     */
    public Message(String newtext, Person author, boolean trusted)
    {
        text = newtext;

        if (!trusted)
        {
            text = BattleshipUtil.makeSafe(text, 256);
        }

        text = BattleshipUtil.autolink(text);

        this.setAuthor(author);
    }

    /**
     * @param id the id to set
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * @return the id
     */
    public long getId()
    {
        return id;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text)
    {
        this.text = text;
    }

    /**
     * @return the text
     */
    public String getText()
    {
        return text;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(Person author)
    {
        this.author = author;
    }

    /**
     * @return the author
     */
    public Person getAuthor()
    {
        return author;
    }

    private long id = System.currentTimeMillis();
    private String text;
    private Person author;
}