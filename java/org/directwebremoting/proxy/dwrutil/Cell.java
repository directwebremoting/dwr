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
package org.directwebremoting.proxy.dwrutil;

/**
 * A simple wrapper for a table cell element
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Cell extends HtmlElement
{
    /**
     * Ensure there is a default constructor
     */
    public Cell()
    {
        innerHtml = ""; //$NON-NLS-1$
    }

    /**
     * @param cellString The cell contents
     */
    public Cell(String cellString)
    {
        innerHtml = cellString;
    }

    /**
     * @return the innerHtml
     */
    public String getInnerHtml()
    {
        return innerHtml;
    }

    /**
     * @param innerHtml the innerHtml to set
     */
    public void setInnerHtml(String innerHtml)
    {
        this.innerHtml = innerHtml;
    }

    private String innerHtml;
}
