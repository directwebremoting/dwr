package uk.ltd.getahead.dwr.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.util.Log;

/**
 * A class to manage the types of creators and the instansiated creators.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultCreatorManager implements CreatorManager
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#setDebug(boolean)
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#isDebug()
     */
    public boolean isDebug()
    {
        return debug;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#addCreatorType(java.lang.String, java.lang.Class)
     */
    public void addCreatorType(String typename, Class clazz)
    {
        if (!Creator.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.CreatorNotAssignabe", clazz.getName(), Creator.class.getName())); //$NON-NLS-1$
        }

        creatorTypes.put(typename, clazz);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#addCreator(java.lang.String, java.lang.String, org.w3c.dom.Element)
     */
    public void addCreator(String type, String javascript, Element allower) throws InstantiationException, IllegalAccessException, IllegalArgumentException
    {
        Class clazz = (Class) creatorTypes.get(type);

        Creator creator = (Creator) clazz.newInstance();

        Map params = new HashMap();

        // Go through the attributes in the allower element, adding to the param map
        NamedNodeMap attrs = allower.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++)
        {
            Node node = attrs.item(i);
            String name = node.getNodeName();
            String value = node.getNodeValue();
            params.put(name, value);
        }

        // Go through the param elements in the allower element, adding to the param map
        NodeList locNodes = allower.getElementsByTagName("param"); //$NON-NLS-1$
        for (int i = 0; i < locNodes.getLength(); i++)
        {
            // Since this comes from getElementsByTagName we can assume that
            // all the nodes are elements.
            Element element = (Element) locNodes.item(i);

            String name = element.getAttribute("name"); //$NON-NLS-1$
            String value = element.getAttribute("value"); //$NON-NLS-1$
            if (name != null && value != null)
            {
                params.put(name, value);
            }
        }

        // Initialize the creator with the parameters that we know of.
        creator.init(params);

        // Check that we don't have this one already
        Creator other = (Creator) creators.get(javascript);
        if (other != null)
        {
            throw new IllegalArgumentException(Messages.getString("DefaultCreatorManager.DuplicateName", javascript, other.getType().getName(), type)); //$NON-NLS-1$
        }

        creators.put(javascript, creator);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#getCreatorNames()
     */
    public Collection getCreatorNames() throws SecurityException
    {
        if (!debug)
        {
            throw new SecurityException();
        }

        return Collections.unmodifiableSet(creators.keySet());
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.CreatorManager#getCreator(java.lang.String)
     */
    public Creator getCreator(String name)
    {
        Creator creator = (Creator) creators.get(name);
        if (creator == null)
        {
            StringBuffer buffer = new StringBuffer("Names of known classes are: "); //$NON-NLS-1$
            for (Iterator it = creators.keySet().iterator(); it.hasNext();)
            {
                String key = (String) it.next();
                buffer.append(key);
                buffer.append(' ');
            }

            Log.warn(buffer.toString());
            throw new SecurityException(Messages.getString("DefaultCreatorManager.MissingName", name)); //$NON-NLS-1$
        }

        return creator;
    }

    /**
     * The list of the available creators
     */
    private Map creatorTypes = new HashMap();

    /**
     * The list of the configured creators
     */
    private Map creators = new HashMap();

    /**
     * Are we in debug mode?
     */
    private boolean debug;
}
