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
 * We are assuming that there is no point to non-public methods for Drapgen
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Method extends Subroutine
{
    /**
     * All {@link Method}s need a parent {@link Type}
     * @param parent the type of which we are a part
     */
    protected Method(Type parent)
    {
        super(parent);
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
     * @return the returnType
     */
    public Parameter getReturnType()
    {
        return returnType;
    }

    /**
     * @param returnType the return Type to set
     */
    public void setReturnType(Parameter returnType)
    {
        this.returnType = returnType;
    }

    /**
     * Create a XOM Element from this
     * @return a Element representing this Type
     */
    protected nu.xom.Element toXomElement()
    {
        nu.xom.Element element = super.toXomElement(METHOD);
        element.addAttribute(new Attribute(NAME, name));
        element.appendChild(returnType.toXomElement(RETURN_TYPE));

        return element;
    }

    /**
     * Load this type with data from the given document
     * @param element The element to load from
     */
    @Override
    protected void fromXomDocument(nu.xom.Element element)
    {
        super.fromXomDocument(element);
        name = element.getAttributeValue(NAME);
        returnType.fromXomDocument(element.getFirstChildElement(RETURN_TYPE));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return getParent().hashCode() + name.hashCode() + getParameters().hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        Method that = (Method) obj;

        if (!this.getParent().equals(that.getParent()))
        {
            return false;
        }

        if (!this.name.equals(that.name))
        {
            return false;
        }

        if (!this.getParameters().equals(that.getParameters()))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getParent() + "." + name + "(" + getParameters() + ")";
    }

    private String name;

    private Parameter returnType;
}
