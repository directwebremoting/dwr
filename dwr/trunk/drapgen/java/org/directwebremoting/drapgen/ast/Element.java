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
package org.directwebremoting.drapgen.ast;

import static org.directwebremoting.drapgen.ast.SerializationStrings.*;

/**
 * Element it the parent type for anything in Drapgen that can be documented.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Element
{
    /**
     * @return the documentation
     */
    public String getDocumentation()
    {
        return documentation;
    }

    /**
     * @param documentation the documentation to set
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /**
     * Add a documentation element to the given element
     * @param element The element to which to add a documentation element
     */
    protected void writeDocumentation(nu.xom.Element element)
    {
        if (documentation != null && documentation.length() != 0)
        {
            nu.xom.Element docElement = new nu.xom.Element(DOCUMENTATION);
            docElement.appendChild(documentation);
            element.appendChild(docElement);
        }
    }

    /**
     * Read a documentation element child of the given element
     * @param element The element from which to read a documentation element
     */
    protected void readDocumentation(nu.xom.Element element)
    {
        nu.xom.Element docElement = element.getFirstChildElement(DOCUMENTATION);
        if (docElement != null)
        {
            documentation = docElement.getValue();
        }
    }

    private String documentation;
}
