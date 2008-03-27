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
 * A simple data container for 2 strings that comprise information about
 * how a Java object has been converted into Javascript
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class OutboundVariable
{
    /**
     * Default ctor that leaves blank (not null) members
     */
    public OutboundVariable()
    {
    }

    /**
     * Default ctor that leaves blank (not null) members
     * @param initCode the init script
     * @param assignCode the access for the inited code
     */
    public OutboundVariable(String initCode, String assignCode)
    {
        this.initCode = initCode;
        this.assignCode = assignCode;
    }

    /**
     * @param initCode The initCode to set.
     */
    public void setInitCode(String initCode)
    {
        this.initCode = initCode;
    }

    /**
     * @return Returns the initCode.
     */
    public String getInitCode()
    {
        return initCode;
    }

    /**
     * @param assignCode The assignCode to set.
     */
    public void setAssignCode(String assignCode)
    {
        this.assignCode = assignCode;
    }

    /**
     * @return Returns the assignCode.
     */
    public String getAssignCode()
    {
        return assignCode;
    }

    /**
     * The code to be executed to initialize any variables
     */
    private String initCode = ""; //$NON-NLS-1$

    /**
     * The code to be executed to get the value of the initialized data
     */
    private String assignCode = ""; //$NON-NLS-1$
}
