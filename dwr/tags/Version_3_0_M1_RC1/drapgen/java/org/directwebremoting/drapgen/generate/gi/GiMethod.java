package org.directwebremoting.drapgen.generate.gi;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 
 */
public class GiMethod
{
    /**
     * @param element
     */
    public GiMethod(Element element, String creatingClassName)
    {
        NamedNodeMap attributes = element.getAttributes();

        Node nameAttr = attributes.getNamedItem("name");
        if (nameAttr == null)
        {
            throw new NullPointerException("Missing name attribute from element: " + element);
        }
        name = nameAttr.getNodeValue();

        // We might be implemented elsewhere
        Node sourceAttr = attributes.getNamedItem("source");
        if (sourceAttr == null)
        {
            declarationClassName = creatingClassName;
        }
        else
        {
            declarationClassName = sourceAttr.getNodeValue();
        }

        this.element = element;
    }

    /**
     * 
     */
    public String getDeclarationClassName()
    {
        return declarationClassName;
    }

    /**
     * 
     */
    public String getName()
    {
        return name;
    }

    /**
     * 
     */
    protected Element getElement()
    {
        return element;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
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

        GiMethod that = (GiMethod) obj;

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
        return declarationClassName + "#" + name + "(...)";
    }

    private Element element;

    private String name;

    private String declarationClassName;
}
