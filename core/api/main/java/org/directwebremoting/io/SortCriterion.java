package org.directwebremoting.io;

import org.directwebremoting.util.LocalUtil;

/**
 * A SortCriterion describes one facet of a possible way of sorting some data.
 * It will generally be used as a List&lt;SortCriterion&gt;. So the data is
 * sorted by the fist SortCriterion in the list first, turning to the second
 * only if there is an equals scenario between 2 bits of data.
 * It is assumed that values for each attribute can be extracted from the data.
 * This is generally fairly simple for Maps and Pojos.
 * SortCriterion does not specify a {@link java.util.Comparator}. It is assumed
 * that the values stored by the data against each attribute are either
 * {@link Comparable} or that the system can find a {@link java.util.Comparator}
 * <p>Pomposity alert:
 * I wouldn't normally be pedantic about Criteria/Criterion, but it's important
 * to note that this is just one facet of an instruction to a sorting algorithm,
 * and not the entire instruction.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SortCriterion
{
    /**
     * All SortCriterion need an attribute and sort order.
     */
    public SortCriterion(String attribute, boolean descending)
    {
        this.attribute = attribute;
        this.descending = descending;
    }

    /**
     * The attribute points to a data member within the data to be sorted.
     * If the data is a {@link java.util.Map} this will be a key. If the data
     * is a Pojo, it will be a property.
     * @return The name of the data member to sort
     */
    public String getAttribute()
    {
        return attribute;
    }

    /**
     * Are we sorting in descending order?
     */
    public boolean isDescending()
    {
        return descending;
    }

    /**
     * Are we sorting in ascending order?
     * This is the logical negative of {@link #isDescending()}
     */
    public boolean isAscending()
    {
        return !descending;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return attribute.hashCode() + (descending ? 7 : 11);
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

        SortCriterion that = (SortCriterion) obj;

        if (this.descending != that.descending)
        {
            return false;
        }

        if (!LocalUtil.equals(this.attribute, that.attribute))
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
        return "Sorting[" + attribute + (descending ? "|desc" : "|asc") + "]";
    }

    /**
     * @see #getAttribute
     */
    private final String attribute;

    /**
     * @see #isDescending
     */
    private final boolean descending;
}
