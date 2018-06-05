package org.directwebremoting.datasync;

import java.util.Comparator;

/**
 * We need a way to get a comparator for a given property of a given type.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ComparatorFactory<T>
{
    /**
     * Return a Comparator which will sort 2 objects of type T based on the
     * values held in the given property
     */
    Comparator<? super T> getComparator(String property, boolean ascending);
}
