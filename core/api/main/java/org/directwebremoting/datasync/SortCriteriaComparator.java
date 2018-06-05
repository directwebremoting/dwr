package org.directwebremoting.datasync;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.io.SortCriterion;
import org.directwebremoting.util.LocalUtil;

/**
 * A {@link Comparator} that uses a list of {@link SortCriterion} to decide
 * how to sort the beans. Values to sort by are extracted using an implementation
 * of {@link AttributeValueExtractor},.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SortCriteriaComparator<T> implements Comparator<T>
{
    /**
     * All SortCriteriaComparators need a set of things to sort on and a way to
     * get the values to sort
     */
    public SortCriteriaComparator(List<SortCriterion> sort, ComparatorFactory<T> comparatorFactory)
    {
        this.sort = sort;
        this.comparatorFactory = comparatorFactory;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(T object1, T object2)
    {
        if (object1 == null)
        {
            if (object2 == null)
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            if (object2 == null)
            {
                return 1;
            }
        }

        if (object1.getClass() != object2.getClass())
        {
            log.warn("Classes don't match. Results could be unpredictable: " + object1.getClass() + " / " + object2.getClass());
        }

        try
        {
            for (SortCriterion criterion : sort)
            {
                Comparator<? super T> comparator = comparatorFactory.getComparator(criterion.getAttribute(), criterion.isAscending());
                int comparison = comparator.compare(object1, object2);
                if (comparison != 0)
                {
                    return comparison;
                }
            }
        }
        catch (Exception ex)
        {
            log.warn("Failure while sorting objects", ex);
        }

        // So we can't tell them apart by comparing them - we can't return 0
        // unless they are equal, so we need to do something determinate
        if (object1.equals(object2))
        {
            return 0;
        }

        int hash1 = object1.hashCode();
        int hash2 = object2.hashCode();
        return LocalUtil.shrink(hash1 - hash2);
    }

    /**
     * The way to extract values to sort on
     */
    private final ComparatorFactory<T> comparatorFactory;

    /**
     * The sort criteria
     */
    private final List<SortCriterion> sort;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(SortCriteriaComparator.class);
}
