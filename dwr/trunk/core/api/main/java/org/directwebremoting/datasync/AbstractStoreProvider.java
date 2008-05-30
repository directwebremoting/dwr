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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.io.Item;

/**
 * Some methods to help implementors create {@link StoreProvider}s. It is
 * strongly recommended that all implementors of {@link StoreProvider} inherit
 * from this class in case it can provide some form of backwards compatibility.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractStoreProvider implements StoreProvider
{
    /**
     * Return true iff the <code>value</code> passed in contains a property
     * by the name of each and every key in the <code>filter</code>, and where
     * the string value (using {@link #toString()}) of the property is equal to
     * the value from the <code>filter</code> map.
     * @param pojo The object to be tested to see if it matches
     * @param filter The set of property/matches to test the value against
     * @return True if the value contains properties that match the filter
     */
    protected boolean passesFilter(Object pojo, Map<String, String> filter)
    {
        if (filter == null || filter.size() == 0)
        {
            return true;
        }

        try
        {
            for (Map.Entry<String, String> entry : filter.entrySet())
            {
                String testProperty = entry.getKey();
                String testValue = entry.getValue();

                try
                {
                    String realValue = getProperty(pojo, testProperty).toString();
                    if (!testValue.equals(realValue.toString()))
                    {
                        return false;
                    }
                }
                catch (NoSuchMethodException ex)
                {
                    return false;
                }
            }
        }
        catch (Exception ex)
        {
            log.warn("Failed to introspect: " + pojo.getClass() + ". Filters will fail.");
            return false;
        }

        return true;
    }

    /**
     * Take a list of Items and apply a set of sort criteria
     * @param matches The list of items to sort
     * @param sort The sort criteria
     */
    protected void sort(List<Item> matches, final List<SortCriteria> sort)
    {
        Collections.sort(matches, new Comparator<Item>()
        {
            @SuppressWarnings("unchecked")
            public int compare(Item item1, Item item2)
            {
                Object object1 = item1.getData();
                Object object2 = item2.getData();

                if (object1.getClass() != object2.getClass())
                {
                    log.warn("Classes don't match. Results could be unpredictable: " + object1.getClass() + " / " + object2.getClass());
                }

                try
                {
                    for (SortCriteria criteria : sort)
                    {
                        Object value1 = getProperty(object1, criteria.getAttribute());
                        Object value2 = getProperty(object2, criteria.getAttribute());

                        if (value1 instanceof Comparable)
                        {
                            Comparable comp1 = (Comparable) value1;
                            Comparable comp2 = (Comparable) value2;

                            int comparison;
                            if (criteria.isDescending())
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

                return 0;
            }
        });
    }

    /**
     * Utility to find a getter and return it's value from an object.
     * If Java had the option to temporarily do dynamic typing there would be
     * no need for this.
     * @param pojo The POJO to extract some data from.
     * @param propertyName The name of the property form which we form a getter
     * name by upper-casing the first letter (in the EN locale) and prefixing
     * with 'get'
     * @return The value of property
     * @throws NoSuchMethodException If the getter was missing
     * @throws SecurityException If the getter was not accessible
     * @throws IllegalArgumentException If something else went wrong
     * @throws IllegalAccessException If something else went wrong
     * @throws InvocationTargetException If something else went wrong
     */
    public static Object getProperty(Object pojo, String propertyName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        Class<? extends Object> real = pojo.getClass();

        String getterName = "get" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);
        
        Method method = real.getMethod(getterName);
        return method.invoke(pojo);
    }

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(AbstractStoreProvider.class);
}
