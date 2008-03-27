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

import java.util.List;

import nu.xom.Elements;

import static org.directwebremoting.drapgen.ast.SerializationStrings.*;

/**
 * An abstract parent for {@link Method}s and {@link Constructor}s
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class Subroutine extends Element
{
    /**
     * All {@link Subroutine}s need a parent {@link Type}
     * @param parent the type of which we are a part
     */
    protected Subroutine(Type parent)
    {
        if (parent == null)
        {
            throw new NullPointerException("parent");
        }

        this.parent = parent;
    }

    /**
     * @return the parameters
     */
    public List<Parameter> getParameters()
    {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(List<Parameter> parameters)
    {
        this.parameters = parameters;
    }

    /**
     * Create a XOM Element from this
     * @return a Element representing this Type
     */
    protected nu.xom.Element toXomElement(String name)
    {
        nu.xom.Element element = new nu.xom.Element(name);
        writeDocumentation(element);

        for (Parameter parameter : parameters)
        {
            element.appendChild(parameter.toXomElement(PARAM));
        }

        return element;
    }

    /**
     * Load this type with data from the given document
     * @param element The element to load from
     */
    protected void fromXomDocument(nu.xom.Element element)
    {
        readDocumentation(element);

        Elements childElements = element.getChildElements(PARAM);
        parameters.clear();
        for (int i = 0; i < childElements.size(); i++)
        {
            nu.xom.Element childElement = childElements.get(i);
            Parameter parameter = new Parameter(getParent().getProject());
            parameter.fromXomDocument(childElement);
            parameters.add(parameter);
        }
    }

    /**
     * @return the parent
     */
    public Type getParent()
    {
        return parent;
    }

    private final Type parent;

    private List<Parameter> parameters;
}
