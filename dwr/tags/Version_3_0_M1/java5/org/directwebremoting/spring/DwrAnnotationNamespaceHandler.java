/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Adds the missing elements to the DWR namespace handling. Namely <dwr:annotation-config />
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class DwrAnnotationNamespaceHandler extends DwrNamespaceHandler
{

    /** 
     * Add a new Parser to register <dwr:annotation-config /> 
     *
     * @see org.directwebremoting.spring.DwrNamespaceHandler#init()
     */
    @Override
    public void init()
    {
        super.init();
        registerBeanDefinitionParser("annotation-config", new AnnotationConfigBeanDefinitionParser());
    }

    /**
     * Register a new bean definition for each Spring bean that is annotated with 
     * {@link org.directwebremoting.annotations.RemoteProxy}.
     *
     * @author Jose Noheda [jose.noheda@gmail.com]
     */
    protected class AnnotationConfigBeanDefinitionParser implements BeanDefinitionParser
    {

        /**
         * Creates a post-processor that will scan the context for candidate beans.
         *
         * @param element <dwr:annotation-config />
         * @param parserContext a reference to the bean registry
         */
        public BeanDefinition parse(Element element, ParserContext parserContext) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DwrAnnotationPostProcessor.class);
            parserContext.getRegistry().registerBeanDefinition("dwrAnnotationPostProcessor", builder.getBeanDefinition());
            return parserContext.getRegistry().getBeanDefinition("dwrAnnotationPostProcessor");
        }

    }

}
