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
 * We are assuming that there is no point to non-public fields for Drapgen
 * @see java.lang.reflect.Field
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Field extends Element
{
    /**
     * All {@link Field}s need a parent {@link Type}
     * @param parent the type of which we are a part
     */
    protected Field(Type parent)
    {
        this.parent = parent;
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
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Create a XOM Element from this
     * @return a Element representing this Type
     */
    protected nu.xom.Element toXomElement()
    {
        nu.xom.Element field = new nu.xom.Element(CONSTANT);
        writeDocumentation(field);
        field.addAttribute(new Attribute(NAME, name));
        field.addAttribute(new Attribute(TYPE, type.getFullName()));
        if (value == null)
        {
            field.addAttribute(new Attribute(VALUE, "null"));
        }
        else
        {
            field.addAttribute(new Attribute(VALUE, value));
        }

        return field;
    }

    /**
     * Load this type with data from the given document
     * @param element The element to load from
     */
    protected void fromXomDocument(nu.xom.Element element)
    {
        readDocumentation(element);
        name = element.getAttributeValue(NAME);
        type = parent.getProject().getType(element.getAttributeValue(TYPE));
        value = element.getAttributeValue(VALUE);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return parent.hashCode() + name.hashCode();
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

        Field that = (Field) obj;

        if (!this.parent.equals(that.parent))
        {
            return false;
        }

        if (!this.name.equals(that.name))
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
        return parent.toString() + "." + name;
    }

    private Type parent;

    private String name;

    private Type type;

    private String value;
}
