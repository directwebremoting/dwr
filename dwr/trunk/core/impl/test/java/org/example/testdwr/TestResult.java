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
package org.example.testdwr;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestResult
{
    /**
     * @return the group
     */
    public String getGroup()
    {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group)
    {
        this.group = group;
    }

    /**
     * @see #getGroup()
     */
    private String group;

    /**
     * @return the testName
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name the testName to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @see #getName()
     */
    private String name;

    /**
     * @return the status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @see #getStatus()
     */
    private String status;

    /**
     * @return the messages
     */
    public List<String> getMessages()
    {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<String> messages)
    {
        this.messages = messages;
    }

    /**
     * @see #getMessages()
     */
    private List<String> messages = new ArrayList<String>();
}
