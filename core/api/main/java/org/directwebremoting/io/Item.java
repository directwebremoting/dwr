package org.directwebremoting.io;

import java.util.Map;

import org.directwebremoting.datasync.ExposeToString;

/**
 * Analogous to a {@link java.util.Map.Entry} that we use to pass objects that
 * have been stored in a {@link org.directwebremoting.datasync.StoreProvider} to
 * the Internet.
 * TODO: Consider if we should add version field to this so we can add some sort
 * of pessimistic locking to updates.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Item
{
    /**
     * Create an Item with its ID and data.
     */
    public Item(String itemId, Object data)
    {
        this.itemId = itemId;
        this.data = data;
    }

    /**
     * Create an Item with its ID and data from a {@link java.util.Map.Entry}
     */
    public Item(Map.Entry<String, Object> entry)
    {
        this.itemId = entry.getKey();
        this.data = entry.getValue();
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
     * The object that is referred to by the itemId.
     * @return The real data
     */
    public Object getData()
    {
        return data;
    }

    /**
     * Items need labels to support dojo.data.api.Read.getLabel()
     * By default we just use the itemId, however if the data implements
     * {@link ExposeToString} then consider {@link Object#toString()}
     * to be safe for Internet use.
     * @return A label for this object
     */
    public String getLabel()
    {
        if (data.getClass().isAnnotationPresent(ExposeToString.class))
        {
            return data.toString();
        }
        else
        {
            return itemId;
        }
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

        Item that = (Item) obj;

        if (!this.itemId.equals(that.itemId))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Item[" + itemId + "]";
    }

    /**
     * @see #getItemId
     */
    private final String itemId;

    /**
     * @see #getData
     */
    private final Object data;
}
