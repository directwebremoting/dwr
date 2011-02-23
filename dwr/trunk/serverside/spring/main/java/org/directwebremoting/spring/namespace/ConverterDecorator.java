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

import org.directwebremoting.spring.ConverterConfig;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Handles <code>&lt;convert&gt;</code> tag.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class ConverterDecorator extends ConverterParserHelper implements BeanDefinitionDecorator
{

    public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder parent, ParserContext parserContext)
    {
        Element element = (Element) node;
        BeanDefinitionRegistry registry = parserContext.getRegistry();

        String type = element.getAttribute("type");
        if ("preconfigured".equals(type))
        {
            type += ":" + element.getAttribute("ref");
        }

        ConverterConfig converterConfig = new ConverterConfig();
        converterConfig.setType(type);
        converterConfig.setJavascriptClassName(element.getAttribute("javascript"));

        String forceAsString = element.getAttribute("force");
        if (forceAsString != null)
        {
            boolean force = Boolean.parseBoolean(forceAsString);
            converterConfig.setForce(force);
        }

        parseConverterSettings(converterConfig, element);
        lookupConverters(registry).put(element.getAttribute("class"), converterConfig);

        return parent;
    }

}
