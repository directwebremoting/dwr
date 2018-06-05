package org.directwebremoting;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * A filter is a way to insert processing tasks at various points during the
 * processing of an Ajax call.
 * <p>Example filters:</p>
 * <ul>
 * <li>Authentication</li>
 * <li>Latency simulators</li>
 * <li>Data cleansing - remove private data</li>
 * <li>Logging filters - when you need specific logging action</li>
 * </ul>
 * @since DWR 2.0
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface AjaxFilter
{
    /**
     * The <code>doFilter</code> method of the AjaxFilter is called by DWR each
     * time an Ajax request is made on a method that this filter is configured
     * against. The <code>AjaxFilterChain<code> passed in to this method allows
     * the filter to pass on method details to next entity in the chain.
     * <p>Typically the method would do the following:</p>
     * <ol>
     * <li>Examine the request</li>
     * <li>Optionally alter the method, object or parameters</li>
     * <li>Either invoke the next entity in the chain using the AjaxFilterChain
     * or decide to take some other action instead.</li>
     * <li>Optionally modify the value returned to the user</li>
     * <li>Take some other action (e.g. logging)</li>
     * </ol>
     * @param obj The object to execute the method on (i.e. 'this')
     * @param method The method to execute
     * @param params The parameters to the method call
     * @param chain Allow the request to be passed on
     * @return The results of the method execution
     * @throws IOException When some I/O error occurs
     * @throws Exception When some processing goes wrong
     */
    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception;
}
