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
package org.directwebremoting.spring;

/**
 * @author Brendan Grainger
 */
public class TestExcludesBean
{
    private String excludedProperty;

    private String notExcludedProperty;

    /**
     * @return the excludedProperty
     */
    public String getExcludedProperty()
    {
        return excludedProperty;
    }

    /**
     * @param excludedProperty the excludedProperty to set
     */
    public void setExcludedProperty(String excludedProperty)
    {
        this.excludedProperty = excludedProperty;
    }

    /**
     * @return the notExcludedProperty
     */
    public String getNotExcludedProperty()
    {
        return notExcludedProperty;
    }

    /**
     * @param notExcludedProperty the notExcludedProperty to set
     */
    public void setNotExcludedProperty(String notExcludedProperty)
    {
        this.notExcludedProperty = notExcludedProperty;
    }
  
}

