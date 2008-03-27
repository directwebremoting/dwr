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
package org.directwebremoting.drapgen.util;

import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.XPathException;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class XomHelper
{
    public interface ElementBlock
    {
        public void use(Element element);
    }

    public static String queryValue(Node doc, String xpath)
    {
        Nodes nodes = doc.query(xpath);

        if (nodes.size() == 0)
        {
            return null;
        }

        if (nodes.size() == 1)
        {
            return nodes.get(0).getValue();
        }

        throw new XPathException("XPath expression returned more than 1 node");
    }

    public static int query(Node doc, String xpath, ElementBlock functor)
    {
        Nodes nodes = doc.query(xpath);
        for (int i = 0; i < nodes.size(); i++)
        {
            Element element = (Element) nodes.get(i);
            functor.use(element);
        }
        return nodes.size();
    }

    public static int getChildElements(Element element, String name, ElementBlock functor)
    {
        Elements children = element.getChildElements(name);
        for (int i = 0; i < children.size(); i++)
        {
            Element child = children.get(i);
            functor.use(child);
        }
        return children.size();
    }
}
