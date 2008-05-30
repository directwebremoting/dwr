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
