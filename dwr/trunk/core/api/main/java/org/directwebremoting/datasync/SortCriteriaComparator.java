package org.directwebremoting.datasync;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    public SortCriteriaComparator(List<SortCriterion> sort, AttributeValueExtractor extractor)
    {
        this.sort = sort;
        this.extractor = extractor;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public int compare(T object1, T object2)
    {
        if (object1.getClass() != object2.getClass())
        {
            log.warn("Classes don't match. Results could be unpredictable: " + object1.getClass() + " / " + object2.getClass());
        }

        try
        {
            for (SortCriterion criterion : sort)
            {
                Object value1 = extractor.getValue(object1, criterion.getAttribute());
                Object value2 = extractor.getValue(object2, criterion.getAttribute());

                if (value1 instanceof Comparable)
                {
                    Comparable comp1 = (Comparable) value1;
                    Comparable comp2 = (Comparable) value2;

                    int comparison;
                    if (criterion.isDescending())
                    {
                        comparison = comp2.compareTo(comp1);
                    }
                    else
                    {
                        comparison = comp1.compareTo(comp2);
                    }

                    if (comparison != 0)
                    {
                        return comparison;
                    }
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
    private AttributeValueExtractor extractor;

    /**
     * The sort criteria
     */
    private final List<SortCriterion> sort;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(SortCriteriaComparator.class);
}
