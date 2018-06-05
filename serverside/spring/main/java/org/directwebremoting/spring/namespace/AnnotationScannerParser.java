package org.directwebremoting.spring.namespace;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.GlobalFilter;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.DwrClassPathBeanDefinitionScanner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.w3c.dom.Element;

import static org.springframework.util.StringUtils.*;

/**
 * Register a new bean definition and a proxy for each class that is annotated with
 * {@link org.directwebremoting.annotations.RemoteProxy} and is detected scanning the
 * base-package directory. It will also scan for DTOs and global filters in the same way.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class AnnotationScannerParser implements BeanDefinitionParser
{

    private static final Log log = LogFactory.getLog(AnnotationScannerParser.class);

    private boolean scanFilters = true;
    private boolean scanProxies = true;
    private boolean scanConverters = true;

    public BeanDefinition parse(Element element, ParserContext parserContext)
    {
        ClassPathBeanDefinitionScanner scanner = new DwrClassPathBeanDefinitionScanner(parserContext.getRegistry());
        String basePackage = element.getAttribute("base-package");
        // Override - By default Spring uses a name generator that uses AnnotationBeanNameGenerator which uses the name specified on the Component
        // annotation and if not present uses the simple name.  Since our annotation-scanner doesn't scan @Component if two classes with the
        // same simple name exist in different packages there will be issues.  See https://directwebremoting.atlassian.net/browse/DWR-651.
        scanner.setBeanNameGenerator(new DefaultBeanNameGenerator());
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
        String filters = element.getAttribute("scanGlobalFilter");
        if (hasText(filters) && ("TRUE".equals(filters.toUpperCase()) || "FALSE".equals(filters.toUpperCase())))
        {
            scanFilters = Boolean.parseBoolean(filters);
        }
        if (scanFilters)
        {
            scanner.addIncludeFilter(new AnnotationTypeFilter(GlobalFilter.class));
        }
        if (scanProxies | scanConverters | scanFilters)
        {
            scanner.scan(basePackage == null ? "" : basePackage);
        }
        else
        {
            log.warn("Scan is not required if all @RemoteProxy, @DataTransferObject and @GlobalFilter are disabled. Skipping detection");
        }
        return null;
    }

}
