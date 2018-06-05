package org.directwebremoting.util;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Various utilities to make up for the fact that DOM isn't as useful as it
 * could be.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DomUtil
{
    /**
     * Extract the textual content from a Node.
     * This is rather like the XPath value of a Node.
     * @param node The node to extract the text from
     * @return The textual value of the node
     */
    public static String getText(Node node)
    {
        StringBuffer reply = new StringBuffer();

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);

            if ((child instanceof CharacterData && !(child instanceof Comment)) || child instanceof EntityReference)
            {
                reply.append(child.getNodeValue());
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                reply.append(getText(child));
            }
        }

        return reply.toString();
    }
}
