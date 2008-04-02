package uk.ltd.getahead.dwrdoc;

/**
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class WebDocument
{
    /**
     * 
     */
    public WebDocument()
    {
    }

    /**
     * @param id
     * @param title
     * @param contents
     */
    public WebDocument(String id, String title, String contents)
    {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    /**
     * @return Returns the contents.
     */
    public String getContents()
    {
        return contents;
    }

    /**
     * @param contents The contents to set.
     */
    public void setContents(String contents)
    {
        this.contents = contents;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title The title to set.
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    private String title;
    private String id;
    private String contents;
}

