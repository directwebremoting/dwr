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

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Handles <code>&lt;dwr:filter/&gt;</code> nested tags.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class FilterDecorator extends FilterParserHelper implements BeanDefinitionDecorator
{

    @Override
    public BeanDefinitionHolder decorate(Node filterElement, BeanDefinitionHolder parent, ParserContext parserContext)
    {
        String name = parent.getBeanName();
        Element element = (Element) filterElement;
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        processFilter(registry, element, name);
        addGlobalFilter(registry, element, name);
        return parent;
    }

}
