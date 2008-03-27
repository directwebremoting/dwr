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

import nu.xom.Attribute;

import static org.directwebremoting.drapgen.ast.SerializationStrings.*;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Parameter extends Element
{
    /**
     * All {@link Parameter}s need a parent {@link Type}
     * @param project the Project of which we are a part
     */
    public Parameter(Project project)
    {
        this.project = project;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the type
     */
    public Type getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Type type)
    {
        this.type = type;
    }

    /**
     * Create a XOM Element from this
     * @param elementName The name so we can use return type parameters
     * @return a Element representing this Type
     */
    protected nu.xom.Element toXomElement(String elementName)
    {
        nu.xom.Element element = new nu.xom.Element(elementName);
        writeDocumentation(element);

        if (name != null)
        {
            element.addAttribute(new Attribute(NAME, name));
        }

        element.addAttribute(new Attribute(TYPE, type.getFullName()));

        return element;
    }

    /**
     * Load this type with data from the given document
     * @param element The element to load from
     */
    protected void fromXomDocument(nu.xom.Element element)
    {
        readDocumentation(element);

        name = element.getAttributeValue(NAME);
        type = project.getType(element.getAttributeValue(TYPE));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "(" + type.toString() + " " + name + ")";
    }

    private Project project;

    private String name;

    private Type type;
}
