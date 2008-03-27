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
package uk.ltd.getahead.dwr;

/**
 * Call is a POJO to encapsulate the information required to make a single java
 * call, including the result of the call (either returned data or exception).
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Call
{
    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param scriptName The scriptName to set.
     */
    public void setScriptName(String scriptName)
    {
        this.scriptName = scriptName;
    }

    /**
     * @return Returns the scriptName.
     */
    public String getScriptName()
    {
        return scriptName;
    }

    /**
     * @param methodName The methodName to set.
     */
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    /**
     * @return Returns the methodName.
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * @param inctx The inctx to set.
     */
    public void setInboundContext(InboundContext inctx)
    {
        this.inctx = inctx;
    }

    /**
     * @return Returns the inctx.
     */
    public InboundContext getInboundContext()
    {
        return inctx;
    }

    /**
     * @param reply The reply to set.
     */
    public void setReply(OutboundVariable reply)
    {
        this.reply = reply;
    }

    /**
     * @return Returns the reply.
     */
    public OutboundVariable getReply()
    {
        return reply;
    }

    /**
     * @param th The th to set.
     */
    public void setThrowable(OutboundVariable th)
    {
        this.th = th;
    }

    /**
     * @return Returns the th.
     */
    public OutboundVariable getThrowable()
    {
        return th;
    }

    private String id = null;

    private String scriptName = null;

    private String methodName = null;

    private InboundContext inctx = new InboundContext();

    private OutboundVariable reply =  null;

    private OutboundVariable th = null;
}
