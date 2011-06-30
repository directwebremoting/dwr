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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class SignatureDecorator implements BeanDefinitionDecorator
{

    private static final Log log = LogFactory.getLog(SignatureDecorator.class);

    public BeanDefinitionHolder decorate(Node signatureElement, BeanDefinitionHolder bean, ParserContext parserContext)
    {
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        BeanDefinition config = ConfigurationParser.registerConfigurationIfNecessary(registry);

        StringBuffer sigtext = new StringBuffer();
        NodeList children = signatureElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if ((child.getNodeType() != Node.TEXT_NODE) && (child.getNodeType() != Node.CDATA_SECTION_NODE))
            {
                log.warn("Ignoring illegal node type: " + child.getNodeType());
                continue;
            }
            sigtext.append(child.getNodeValue());
        }

        config.getPropertyValues().addPropertyValue("signatures", sigtext.toString());

        return bean;
    }

}
