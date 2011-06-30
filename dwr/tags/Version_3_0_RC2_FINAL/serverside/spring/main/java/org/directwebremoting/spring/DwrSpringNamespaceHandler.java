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
package org.directwebremoting.spring;

import org.directwebremoting.spring.namespace.AnnotationConfigParser;
import org.directwebremoting.spring.namespace.AnnotationScannerParser;
import org.directwebremoting.spring.namespace.ConfigurationParser;
import org.directwebremoting.spring.namespace.ControllerParser;
import org.directwebremoting.spring.namespace.ConverterDecorator;
import org.directwebremoting.spring.namespace.FilterDecorator;
import org.directwebremoting.spring.namespace.InitDecorator;
import org.directwebremoting.spring.namespace.ProxyParser;
import org.directwebremoting.spring.namespace.RemoteDecorator;
import org.directwebremoting.spring.namespace.SignatureDecorator;
import org.directwebremoting.spring.namespace.UrlMappingParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Handles all dwr namespace in XML files.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class DwrSpringNamespaceHandler extends NamespaceHandlerSupport
{

    /**
     * Registers all the available tag parsers and decorators.
     */
    public void init()
    {
        registerBeanDefinitionDecorator("init", new InitDecorator());
        registerBeanDefinitionDecorator("filter", new FilterDecorator());
        registerBeanDefinitionDecorator("remote", new RemoteDecorator());
        registerBeanDefinitionDecorator("convert", new ConverterDecorator());
        registerBeanDefinitionDecorator("signatures", new SignatureDecorator());

        registerBeanDefinitionParser("proxy-ref", new ProxyParser());
        registerBeanDefinitionParser("controller", new ControllerParser());
        registerBeanDefinitionParser("url-mapping", new UrlMappingParser());
        registerBeanDefinitionParser("configuration", new ConfigurationParser(this));
        registerBeanDefinitionParser("annotation-scan", new AnnotationScannerParser());
        registerBeanDefinitionParser("annotation-config", new AnnotationConfigParser());

    }

}
