package org.directwebremoting.datasync;

import java.util.Comparator;

/**
 * A ComparatorFactory that assumes that
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultComparatorFactory<T> implements ComparatorFactory<T>
{
    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.ComparatorFactory#getComparator(java.lang.String, boolean)
     */
    public Comparator<? super T> getComparator(final String property, final boolean ascending)
    {
        final AttributeValueExtractor extractor = new PojoAttributeValueExtractor();

        return new Comparator<T>()
        {
            /* (non-Javadoc)
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            @SuppressWarnings("unchecked")
            public int compare(T o1, T o2)
            {
                Object value1 = extractor.getValue(o1, property);
                Object value2 = extractor.getValue(o2, property);

                if (value1 == null)
                {
                    if (value2 == null)
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
                    if (value2 == null)
                    {
                        return 1;
                    }
                }

                Comparable comp1 = (Comparable) value1;
                Comparable comp2 = (Comparable) value2;

                if (!ascending)
                {
                    return comp2.compareTo(comp1);
                }
                else
                {
                    return comp1.compareTo(comp2);
                }
            }
        };
    }
}
