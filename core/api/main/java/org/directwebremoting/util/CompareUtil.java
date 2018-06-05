package org.directwebremoting.util;

/**
 * Some utilities for comparing objects that could be null
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CompareUtil
{
    /**
     * Compare 2 objects taking account of the fact that either could be null
     * @param <T> The type that we are comparing
     * @param o1 The first object to compare
     * @param o2 The second object to compare
     * @return -1, 0, or 1
     */
    public static <T> int compare(Comparable<T> o1, T o2)
    {
        if (o1 == null && o2 == null)
        {
            return 0;
        }

        if (o1 == null)
        {
            return -1;
        }

        if (o2 == null)
        {
            return 1;
        }

        return o1.compareTo(o2);
    }

    /**
     * Compare 2 objects taking account of the fact that either could be null
     * @param o1 The first object to compare
     * @param o2 The second object to compare
     * @return true iff they are both null or if o1.equals(o2) 
     */
    public static boolean equals(Object o1, Object o2)
    {
        if (o1 == null && o2 == null)
        {
            return true;
        }

        if (o1 == null || o2 == null)
        {
            return false;
        }

        return o1.equals(o2);
    }
}
