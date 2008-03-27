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
package org.directwebremoting.extend;

import java.util.ArrayList;
import java.util.List;

/**
 * The request made by the browser which consists of a number of function call
 * requests and some associated information like the request mode (XHR or
 * iframe).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Calls
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
        return (Call) calls.get(index);
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

    private String batchId = null;

    /**
     * The collection of Calls that we should execute
     */
    protected List calls = new ArrayList();
}
