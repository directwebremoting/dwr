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

import java.util.Map;

import org.directwebremoting.spring.ConverterConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public abstract class ConverterParserHelper extends FilterParserHelper
{

    /**
     * Get a list of the defined Converters.
     *
     * @param registry a non null instance
     * @return any
     */
    @SuppressWarnings("unchecked")
    public static Map<String, ConverterConfig> lookupConverters(BeanDefinitionRegistry registry)
    {
        BeanDefinition config = ConfigurationParser.registerConfigurationIfNecessary(registry);
        return (Map<String, ConverterConfig>) config.getPropertyValues().getPropertyValue("converters").getValue();
    }

    protected void parseConverterSettings(ConverterConfig converterConfig, Element parent)
    {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);
            if (node instanceof Element)
            {
                Element child = (Element) node;
                if (INCLUDE.equals(child.getNodeName()))
                {
                    converterConfig.addInclude(child.getAttribute("method"));
                }
                else if (EXCLUDE.equals(child.getNodeName()))
                {
                    converterConfig.addExclude(child.getAttribute("method"));
                }
                else if (PARAMETER.equals(node.getNodeName()))
                {
                    converterConfig.getParams().put(child.getAttribute("name"), child.getAttribute("value"));
                }
            }
        }
    }

    protected static final String INCLUDE = "dwr:include";
    protected static final String EXCLUDE = "dwr:exclude";
    protected static final String CONVERT = "dwr:convert";
    protected static final String PARAMETER = "dwr:param";

}
