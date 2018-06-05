package org.directwebremoting.io;

/**
 * Analogous to a {@link java.util.Map.Entry} that we use to pass objects that
 * have been stored in a {@link org.directwebremoting.datasync.StoreProvider} to
 * the Internet.
 * TODO: Consider if we should add version field to this so we can add some sort
 * of pessimistic locking to updates.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ItemUpdate
{
    /**
     * Create an Item with its ID and data.
     */
    public ItemUpdate(String itemId, String attribute, RawData newValue)
    {
        this.itemId = itemId;
        this.attribute = attribute;
        this.newValue = newValue;
    }

    /**
     * Accessor for the primary key for this Object.
     * Clearly not all database primary keys are Strings, my current feeling is
     * that the majority of database primary keys can either be simply mapped to
     * strings, or are a bad idea anyway.
     * <p>Warning: It would be a bad idea to expose database primary keys
     * directly to the Internet anyway, so some form of mapping is required.
     * @return The primary key for this Object
     */
    public String getItemId()
    {
        return itemId;
    }

    /**
     * Accessor for the property that we want to change on the Object with the
     * ID given in <code>itemId</code>.
     */
    public String getAttribute()
    {
        return attribute;
    }

    /**
     * Accessor for the value for the <code>attribute</code> that we want to
     * change on the Object with the ID given in <code>itemId</code>.
     */
    public RawData getNewValue()
    {
        return newValue;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return 4783 + itemId.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        ItemUpdate that = (ItemUpdate) obj;

        if (!this.itemId.equals(that.itemId))
        {
            return false;
        }

        return true;
    }

    /**
     * @see #getItemId
     */
    private final String itemId;

    /**
     * @see #getAttribute
     */
    private final String attribute;

    /**
     * @see #getNewValue
     */
    private final RawData newValue;
}
