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
