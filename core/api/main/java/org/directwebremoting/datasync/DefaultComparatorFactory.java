/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
