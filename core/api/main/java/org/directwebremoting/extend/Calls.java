package org.directwebremoting.extend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The request made by the browser which consists of a number of function call
 * requests and some associated information like the request mode (XHR or
 * iframe).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Calls implements Iterable<Call>
{
    /**
     * How many calls are there is this request
     * @return The total number of calls
     */
    public int getCallCount()
    {
        return calls.size();
    }

    /**
     * @param index The index (starts at 0) of the call to fetch
     * @return Returns the calls.
     */
    public Call getCall(int index)
    {
        return calls.get(index);
    }

    /**
     * Add a call to the collection of calls we are about to make
     * @param call The call to add
     */
    public void addCall(Call call)
    {
        calls.add(call);
    }

    /**
     * @param batchId The batchId to set.
     */
    public void setBatchId(String batchId)
    {
        this.batchId = batchId;
    }

    /**
     * @return Returns the batchId.
     */
    public String getBatchId()
    {
        return batchId;
    }

    /**
     * @param instanceId The batchId to set.
     */
    public void setInstanceId(String instanceId)
    {
        this.instanceId = instanceId;
    }

    /**
     * @return Returns the instanceId.
     */
    public String getInstanceId()
    {
        return instanceId;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Call> iterator()
    {
        return calls.iterator();
    }

    private String batchId = null;

    private String instanceId = null;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Calls[id=" + batchId + ":" + calls + "]";
    }

    /**
     * The collection of Calls that we should execute
     */
    protected List<Call> calls = new ArrayList<Call>();
}
