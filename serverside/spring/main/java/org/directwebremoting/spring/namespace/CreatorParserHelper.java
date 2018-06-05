package org.directwebremoting.spring.namespace;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.Filter;
import org.directwebremoting.annotations.Filters;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.spring.BeanCreator;
import org.directwebremoting.spring.ConverterConfig;
import org.directwebremoting.spring.CreatorConfig;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public abstract class CreatorParserHelper extends ConverterParserHelper
{

    private static final Log log = LogFactory.getLog(CreatorParserHelper.class);

    /**
     * Get a list of the defined Creators.
     *
     * @param registry a non null instance
     * @return any
     */
    @SuppressWarnings("unchecked")
    protected static Map<String, RuntimeBeanReference> lookupCreators(BeanDefinitionRegistry registry)
    {
        BeanDefinition config = ConfigurationParser.registerConfigurationIfNecessary(registry);
        return (Map<String, RuntimeBeanReference>) config.getPropertyValues().getPropertyValue("creators").getValue();
    }

    /**
     * Registers a new {@link org.directwebremoting.extend.Creator} in the registry using name <code>javascript</code>.
     * @param registry The definition of all the Beans
     * @param javascript The name of the bean in the registry.
     * @param creatorConfig
     * @param children The node list to check for nested elements
     */
    protected void configureCreator(BeanDefinitionRegistry registry, String javascript, BeanDefinitionBuilder creatorConfig, NodeList children)
    {
        List<String> includes = new ArrayList<String>();
        List<String> excludes = new ArrayList<String>();
        Map<String, String> params = new HashMap<String, String>();
        Map<String, List<String>> auth = new HashMap<String, List<String>>();

        for (int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);
            if (node instanceof Element)
            {
                Element child = (Element) node;

                if (LATENCY_FILTER.equals(node.getNodeName()))
                {
                    processLatencyFilter(registry, creatorConfig, child, javascript);
                }
                else if (INCLUDE.equals(node.getNodeName()))
                {
                    includes.add(child.getAttribute("method"));
                }
                else if (EXCLUDE.equals(node.getNodeName()))
                {
                    excludes.add(child.getAttribute("method"));
                }
                else if (AUTH.equals(node.getNodeName()))
                {
                    processAuth(child, auth);
                }
                else if (CONVERT.equals(node.getNodeName()))
                {
                    processConvert(registry, child);
                }
                else if (FILTER.equals(node.getNodeName()))
                {
                    processFilter(registry, child, javascript);
                    creatorConfig.addPropertyValue("filters", createManagedFilterList(child, javascript));
                }
                else if (PARAMETER.equals(node.getNodeName()))
                {
                    params.put(child.getAttribute("name"), child.getAttribute("value"));
                }
            }
        }
        creatorConfig.addPropertyValue("auth", auth);
        creatorConfig.addPropertyValue("params", params);
        creatorConfig.addPropertyValue("includes", includes);
        creatorConfig.addPropertyValue("excludes", excludes);
    }

    public static void registerCreator(BeanDefinitionHolder beanDefinitionHolder, BeanDefinitionRegistry beanDefinitionRegistry, Class<?> beanDefinitionClass, String javascript)
    {
        String creatorConfigName = "__" + javascript;
        if (beanDefinitionRegistry.containsBeanDefinition(creatorConfigName))
        {
            log.info("[" + javascript + "] remote bean definition already detected. Invalid mixed use of <dwr:annotation-config /> and <dwr:annotation-scan />? Re-scanned package?");
        }
        else
        {
            BeanDefinitionBuilder beanCreator = BeanDefinitionBuilder.rootBeanDefinition(BeanCreator.class);
            try {
                beanCreator.addPropertyValue("beanClass", beanDefinitionClass);
                String name = beanDefinitionHolder.getBeanName();
                if (name.startsWith("scopedTarget."))
                {
                    name = name.substring(name.indexOf(".") + 1);
                }
                beanCreator.addPropertyValue("beanId", name);
                beanCreator.addPropertyValue("javascript", javascript);
                BeanDefinitionBuilder creatorConfig = BeanDefinitionBuilder.rootBeanDefinition(CreatorConfig.class);
                creatorConfig.addPropertyValue("creator", beanCreator.getBeanDefinition());
                List<String> includes = new ArrayList<String>();
                for (Method method : beanDefinitionClass.getMethods()) {
                    if (method.getAnnotation(RemoteMethod.class) != null)
                    {
                        includes.add(method.getName());
                    }
                }
                // Handle the Filter/Filters annotations.
                ManagedList filters = new ManagedList();
                Filter filter = beanDefinitionClass.getAnnotation(Filter.class);
                if (null != filter) {
                    processFilter(beanDefinitionRegistry, filter, javascript, filters);
                }
                Filters filtersAnn = beanDefinitionClass.getAnnotation(Filters.class);
                if (filtersAnn != null)
                {
                    Filter[] fs = filtersAnn.value();
                    for (Filter filterFromFilters : fs)
                    {
                        processFilter(beanDefinitionRegistry, filterFromFilters, javascript, filters);
                    }
                }
                if (filters.size() > 0) {
                    creatorConfig.addPropertyValue("filters", filters);
                }
                // Processing of Filter/Filters complete, continue processing.
                creatorConfig.addPropertyValue("includes", includes);
                BeanDefinitionHolder aux = new BeanDefinitionHolder(creatorConfig.getBeanDefinition(), creatorConfigName);
                BeanDefinitionReaderUtils.registerBeanDefinition(aux, beanDefinitionRegistry);
                lookupCreators(beanDefinitionRegistry).put(javascript, new RuntimeBeanReference(creatorConfigName));
            } catch (Exception ex) {
                throw new FatalBeanException("Unable to create DWR bean creator for '" + beanDefinitionHolder.getBeanName() + "'. ", ex);
            }
        }
    }

    protected void registerCreator(BeanDefinitionRegistry registry, BeanDefinitionBuilder creatorConfig, String javascript)
    {
        String creatorConfigName = "__" + javascript;
        BeanDefinitionHolder holder3 = new BeanDefinitionHolder(creatorConfig.getBeanDefinition(), creatorConfigName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder3, registry);
        lookupCreators(registry).put(javascript, new RuntimeBeanReference(creatorConfigName));
    }

    private void processAuth(Element child, Map<String, List<String>> auth)
    {
        String method = child.getAttribute("method");
        if (auth.get(method) == null)
        {
            auth.put(method, new ArrayList<String>());
        }
        auth.get(method).add(child.getAttribute("role"));
    }

    private void processConvert(BeanDefinitionRegistry registry, Element element)
    {
        ConverterConfig converterConfig = new ConverterConfig();
        converterConfig.setType(element.getAttribute("type"));
        parseConverterSettings(converterConfig, element);
        lookupConverters(registry).put(element.getAttribute("class"), converterConfig);
    }

    private static final String AUTH = "dwr:auth";
    private static final String FILTER = "dwr:filter";
    private static final String LATENCY_FILTER = "dwr:latencyfilter";

}
