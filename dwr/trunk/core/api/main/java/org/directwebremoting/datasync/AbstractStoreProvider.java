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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.RealRawData;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.io.RawData;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Pair;

/**
 * Some methods to help implementors create {@link StoreProvider}s. It is
 * strongly recommended that all implementors of {@link StoreProvider} inherit
 * from this class in case it can provide some form of backwards compatibility.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractStoreProvider<T> implements StoreProvider<T>
{
    public AbstractStoreProvider(Class<T> type)
    {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.StoreProvider#put(java.lang.String, org.directwebremoting.io.RawData)
     */
    public void put(String itemId, RawData rawData)
    {
        T value = convert(rawData);
        put(itemId, value);
    }

    /**
     * Return true iff the <code>value</code> passed in contains a property
     * by the name of each and every key in the <code>query</code>, and where
     * the string value (using {@link #toString()}) of the property is equal to
     * the value from the <code>filter</code> map.
     * @param pojo The object to be tested to see if it matches
     * @param query The set of property/matches to test the value against
     * @return True if the value contains properties that match the filter
     */
    protected static boolean passesFilter(Object pojo, Map<String, String> query)
    {
        if (query == null || query.isEmpty())
        {
            return true;
        }

        try
        {
            for (Map.Entry<String, String> entry : query.entrySet())
            {
                String testProperty = entry.getKey();
                String testValue = entry.getValue();

                try
                {
                    String realValue = LocalUtil.getProperty(pojo, testProperty).toString();
                    if (!testValue.equals(realValue))
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
     * Convert from {@link RawData} to the type that this {@link StoreProvider}
     * supports.
     * @param rawData The data from the Internet
     * @return An object of the type supported by this store
     */
    protected T convert(RawData rawData)
    {
        if (converterManager == null)
        {
            converterManager = ServerContextFactory.get().getContainer().getBean(ConverterManager.class);
        }

        RealRawData realRawData = (RealRawData) rawData;
        InboundVariable inboundVariable = realRawData.getInboundVariable();
        TypeHintContext typeHintContext = new TypeHintContext(converterManager, null, 0);

        T value = converterManager.convertInbound(type, inboundVariable, typeHintContext);
        return value;
    }

    /**
     * A PairComparator is a way to proxy comparisons to the 'value' of a
     * String, Object paring.
     * @author Joe Walker [joe at getahead dot ltd dot uk]
     */
    protected static class PairComparator<T> implements Comparator<Pair<String, T>>
    {
        /**
         * @param proxy The value object comparator
         */
        protected PairComparator(Comparator<T> proxy)
        {
            this.proxy = proxy;
        }

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Pair<String, T> p1, Pair<String, T> p2)
        {
            return proxy.compare(p1.right, p2.right);
        }

        /**
         * The comparator that we proxy to
         */
        private final Comparator<T> proxy;
    }

    /**
     * The type that this StoreProvider uses
     */
    protected final Class<T> type;

    /**
     * Cached converterManager so we don't look it up every time
     */
    protected ConverterManager converterManager;

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(AbstractStoreProvider.class);
}
