/*
 * Copyright 2005-2006 Joe Walker
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
 * The configuration for a Converter.
 *
 * It allows the specification of the following optional configuration parameters:
 * <ul>
 *  <li>includes - the list of method names to include</li>
 *  <li>excludes - the list of method names to exclude</li>
 *  <li>force - if <code>true</code> instructs DWR to use reflection modifiers to access private
 *      members of objects</li>
 * </ul>
 *
 * @author Brendan Grainger
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConverterConfig extends AbstractConfig
{
    /**
     * Gets the converter type.
     * @return Returns the converter type
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the converter type.
     * @param converterType Sets the converter type
     */
    public void setType(String converterType)
    {
        this.type = converterType;
    }

    /**
     * @return the javascriptClassName
     */
    public String getJavascriptClassName()
    {
        return javascriptClassName;
    }

    /**
     * @param javascriptClassName the javascriptClassName to set
     */
    public void setJavascriptClassName(String javascriptClassName)
    {
        this.javascriptClassName = javascriptClassName;
    }

    /**
     * If true DWR will use reflection modifiers to access private members of objects
     * @return - Returns whether to use reflection for accessing private members
     */
    public boolean isForce()
    {
        return force;
    }

    /**
     * Instruct DWR to use reflection modifiers to access private members of objects
     *
     * @param force - if true DWR will use reflection to access private members of objects
     */
    public void setForce(boolean force)
    {
        this.force = force;
    }

    /** The converter type. */
    private String type;

    /**
     * If <code>true</code> instructs DWR to use reflection modifiers to access private members of objects
     */
    private boolean force = false;
    
    /**
     * The javascriptClassName to set
     */
    private String javascriptClassName; 

}