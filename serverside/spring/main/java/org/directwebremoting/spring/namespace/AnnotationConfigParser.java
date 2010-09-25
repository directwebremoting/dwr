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

import org.directwebremoting.spring.DwrAnnotationPostProcessor;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * Creates a post-processor that will scan the context for candidate beans
 * (<code>&lt;dwr:annotation-config /&gt;</code>). It will register a new
 * creator proxy for each Spring bean that is annotated with
 * {@link org.directwebremoting.annotations.RemoteProxy}.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class AnnotationConfigParser extends AbstractSimpleBeanDefinitionParser
{

    @Override
    protected Class<?> getBeanClass(Element element)
    {
        return DwrAnnotationPostProcessor.class;
    }

}
