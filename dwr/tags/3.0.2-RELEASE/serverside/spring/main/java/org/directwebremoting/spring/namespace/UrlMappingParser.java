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

import org.directwebremoting.spring.DwrHandlerMapping;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Converts <code>&lt;dwr:url-mapping /&gt;</code> tag in the adequate DwrHandlerMapping bean definition.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class UrlMappingParser extends AbstractSingleBeanDefinitionParser
{

    @Override
    protected Class<?> getBeanClass(Element element)
    {
        return DwrHandlerMapping.class;
    }

    /**
     * Offers a default value of ""
     */
    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException
    {
        String id = super.resolveId(element, definition, parserContext);
        return StringUtils.hasText(id) ? id : "dwrHandlerMapping";
    }

    /**
     * Includes the interceptors list.
     *
     * @param element the tag itself
     * @param urlMapping the bean definition already created
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder urlMapping)
    {
        String interceptors = element.getAttribute("interceptors");
        if (StringUtils.hasText(interceptors))
        {
            urlMapping.addPropertyReference("interceptors", interceptors);
        }
    }

}
