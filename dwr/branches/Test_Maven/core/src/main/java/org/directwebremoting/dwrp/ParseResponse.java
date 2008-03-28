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
package org.directwebremoting.dwrp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.Calls;

/**
 * A container for all the by-products of an HttpRequest parse
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
class ParseResponse
{
    /**
     * @return the allParameters
     */
    public Map getAllParameters()
    {
        return new HashMap(allParameters);
    }

    /**
     * @param allParameters the allParameters to set
     */
    public void setAllParameters(Map allParameters)
    {
        this.allParameters = allParameters;
    }

    /**
     * @return the inboundContexts
     */
    public List getInboundContexts()
    {
        return inboundContexts;
    }

    /**
     * @param inboundContexts the inboundContexts to set
     */
    public void setInboundContexts(List inboundContexts)
    {
        this.inboundContexts = inboundContexts;
    }

    /**
     * @return the spareParameters
     */
    public Map getSpareParameters()
    {
        return spareParameters;
    }

    /**
     * @param spareParameters the spareParameters to set
     */
    public void setSpareParameters(Map spareParameters)
    {
        this.spareParameters = spareParameters;
    }

    /**
     * @return the page
     */
    public String getPage()
    {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(String page)
    {
        this.page = page;
    }

    /**
     * @return the scriptSessionId
     */
    public String getScriptSessionId()
    {
        return scriptSessionId;
    }

    /**
     * @param scriptSessionId the scriptSessionId to set
     */
    public void setScriptSessionId(String scriptSessionId)
    {
        this.scriptSessionId = scriptSessionId;
    }

    /**
     * @return the calls
     */
    public Calls getCalls()
    {
        return calls;
    }

    /**
     * @param calls the calls to set
     */
    public void setCalls(Calls calls)
    {
        this.calls = calls;
    }

    private List inboundContexts = new ArrayList();

    private String scriptSessionId;

    private String page;

    private Calls calls;

    private Map allParameters = new HashMap();

    private Map spareParameters = new HashMap();
}
