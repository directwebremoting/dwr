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

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.w3c.dom.Element;

import static org.springframework.util.StringUtils.*;

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
        registerBeanDefinitionParser("annotation-scan", new AnnotationScannerDefinitionParser());
    }

    /**
     * Register a new creator proxy for each Spring bean that is annotated with
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

    /**
     * Register a new bean definition and a proxy for each class that is annotated with
     * {@link org.directwebremoting.annotations.RemoteProxy} and is detected scanning the
     * base-package directory.
     *
     * @author Jose Noheda [jose.noheda@gmail.com]
     */
    protected class AnnotationScannerDefinitionParser implements BeanDefinitionParser
    {

        private boolean scanProxies = true;
        private boolean scanConverters = true;

        public BeanDefinition parse(Element element, ParserContext parserContext)
        {
            ClassPathBeanDefinitionScanner scanner = new DwrClassPathBeanDefinitionScanner(parserContext.getRegistry());
            String basePackage = element.getAttribute("base-package");
            if (!hasText(basePackage))
            {
                if (log.isInfoEnabled())
                {
                    log.info("No base package defined for classpath scanning. Traversing the whole JVM classpath");
                }
            }
            String regex = element.getAttribute("regex");
            if (hasText(regex))
            {
                scanner.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(regex)));
            }
            String proxies = element.getAttribute("scanRemoteProxy");
            if (hasText(proxies) && ("TRUE".equals(proxies.toUpperCase()) || "FALSE".equals(proxies.toUpperCase())))
            {
                scanProxies = Boolean.parseBoolean(proxies);
            }
            if (scanProxies)
            {
                scanner.addIncludeFilter(new AnnotationTypeFilter(RemoteProxy.class));
            }
            String conv = element.getAttribute("scanDataTransferObject");
            if (hasText(conv) && ("TRUE".equals(conv.toUpperCase()) || "FALSE".equals(conv.toUpperCase())))
            {
                scanConverters = Boolean.parseBoolean(conv);
            }
            if (scanConverters)
            {
                scanner.addIncludeFilter(new AnnotationTypeFilter(DataTransferObject.class));
            }
            if (scanProxies | scanConverters)
            {
                scanner.scan(basePackage == null ? "" : basePackage);
            }
            else
            {
                log.warn("Scan is not required if both @RemoteProxy and @DataTransferObject are disabled. Skipping detection");
            }
            return null;
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrAnnotationNamespaceHandler.class);
}
