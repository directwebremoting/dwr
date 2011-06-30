/*
 * Copyright 2010 original author or authors
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
package org.directwebremoting.spring.namespace;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Handler for <code>&lt;dwr:init&gt;</code> tags inside <code>&lt;dwr:configuration&gt;</code> tags.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class InitDecorator implements BeanDefinitionDecorator
{

    /**
     * Parses the <code>&lt;dwr:init&gt;</code> tag checking for nested creator and converter definitions.
     */
    public BeanDefinitionHolder decorate(Node initElement, BeanDefinitionHolder configuration, ParserContext parserContext)
    {
        NodeList inits = initElement.getChildNodes();
        Map<String, String> converters = new HashMap<String, String>();
        for (int index = 0; index < inits.getLength(); index++)
        {
            Node node = inits.item(index);
            if (node instanceof Element)
            {
                Element child = (Element) node;
                if (ELEMENT_CONVERTER.equals(node.getNodeName()))
                {
                    converters.put(child.getAttribute(ATTRIBUTE_ID), child.getAttribute(ATTRIBUTE_CLASS));
                }
            }
        }

        configuration.getBeanDefinition().getPropertyValues().addPropertyValue("converterTypes", converters);

        return configuration;
    }

    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String ELEMENT_CONVERTER = "dwr:converter";

}
