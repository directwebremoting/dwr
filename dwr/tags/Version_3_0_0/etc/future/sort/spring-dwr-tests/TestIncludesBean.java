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
public class TestIncludesBean
{
    private String includedProperty;
    private String notIncludedProperty;
    
    /**
     * Method to be included in testing
     * @return value of included property
     */
    public String getIncludedProperty()
    {
        return includedProperty;
    }

    /**
     * Method to be included in testing
     * @param stringProperty of included property
     */
    public void setIncludedProperty(String stringProperty)
    {
        this.includedProperty = stringProperty;
    }

    /**
     * @return the notIncludedProperty
     */
    public String getNotIncludedProperty()
    {
        return notIncludedProperty;
    }

    /**
     * @param notIncludedProperty the notIncludedProperty to set
     */
    public void setNotIncludedProperty(String notIncludedProperty)
    {
        this.notIncludedProperty = notIncludedProperty;
    }
  
}

