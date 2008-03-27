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

package org.directwebremoting.convert.test;

/**
 * @author Bram Smeets
 */
public class MyBeanImpl
{
    private String property;

    private String nonReadableProperty;

    /**
     * @return a string
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * @param property
     */
    public void setProperty(String property)
    {
        this.property = property;
    }

    /**
     * @param nonReadableProperty
     */
    public void setNonReadableProperty(String nonReadableProperty)
    {
        this.nonReadableProperty = nonReadableProperty;
    }

    /**
     * This just shuts lint up
     */
    protected void ignore()
    {
        String ignore = nonReadableProperty;
        nonReadableProperty = ignore;
    }
}
