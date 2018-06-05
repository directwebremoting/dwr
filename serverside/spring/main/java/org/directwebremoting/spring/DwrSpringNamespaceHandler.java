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
