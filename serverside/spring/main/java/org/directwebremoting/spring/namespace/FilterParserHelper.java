package org.directwebremoting.spring.namespace;

import java.util.List;

import org.directwebremoting.annotations.Filter;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.filter.ExtraLatencyAjaxFilter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Convenience methods to help parse a <code>&lt;filter&gt;</code> tag.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public abstract class FilterParserHelper extends NamespaceParserHelper
{

    @SuppressWarnings("unchecked")
    protected void processLatencyFilter(BeanDefinitionRegistry registry, BeanDefinitionBuilder creatorConfig, Element child, String name)
    {
        BeanDefinitionBuilder beanFilter = BeanDefinitionBuilder.rootBeanDefinition(ExtraLatencyAjaxFilter.class);
        beanFilter.addPropertyValue("delay", child.getAttribute("delay"));
        registerFilter(registry, beanFilter.getBeanDefinition(), "__latencyFilter_" + name);
        ManagedList filterList = new ManagedList();
        filterList.add(new RuntimeBeanReference("__latencyFilter_" + name));
        creatorConfig.addPropertyValue("filters", filterList);
    }

    /**
     * Processes filters configured via xml.
     *
     * @param registry
     * @param element
     * @param javascript
     */
    @SuppressWarnings("unchecked")
    protected void processFilter(BeanDefinitionRegistry registry, Element element, String javascript)
    {
        String filterClass = element.getAttribute("class");
        BeanDefinitionBuilder beanFilter = BeanDefinitionBuilder.rootBeanDefinition(filterClass);
        List<Element> filterParamElements = DomUtils.getChildElementsByTagName(element, "param");
        for (Element filterParamElement : filterParamElements)
        {
            beanFilter.addPropertyValue(filterParamElement.getAttribute("name"), filterParamElement.getAttribute("value"));
        }
        registerFilter(registry, beanFilter.getBeanDefinition(), "__filter_" + filterClass + "_" + javascript);
    }

    /**
     * Processes Filters configured via annotations.
     *
     * @param registry
     * @param filter
     * @param javascript
     * @param filters
     */
    @SuppressWarnings("unchecked")
    protected static void processFilter(BeanDefinitionRegistry registry, Filter filter, String javascript, ManagedList filters)
    {
        BeanDefinitionBuilder beanFilter = BeanDefinitionBuilder.rootBeanDefinition(filter.type());
        Param[] filterParams = filter.params();
        if (filterParams != null)
        {
            for (Param filterParam : filterParams) {
                beanFilter.addPropertyValue(filterParam.name(), filterParam.value());
            }
        }
        registerFilter(registry, beanFilter.getBeanDefinition(), "__filter_" + javascript);
        filters.add(new RuntimeBeanReference("__filter_" + javascript));
    }

    private static void registerFilter(BeanDefinitionRegistry registry, BeanDefinition bean, String name)
    {
        BeanDefinitionHolder holder = new BeanDefinitionHolder(bean, name);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    @SuppressWarnings("unchecked")
    protected void addGlobalFilter(BeanDefinitionRegistry registry, Element element, String javascript)
    {
        String filterClass = element.getAttribute("class");
        BeanDefinition springConfigurator = ConfigurationParser.registerConfigurationIfNecessary(registry);
        ManagedList filters = (ManagedList) springConfigurator.getPropertyValues().getPropertyValue("filters").getValue();
        filters.add(new RuntimeBeanReference("__filter_" + filterClass + "_" + javascript));
    }

    protected ManagedList createManagedFilterList(Element element, String javascript)
    {
        return createManagedFilterList(element.getAttribute("class"), javascript);
    }

    @SuppressWarnings("unchecked")
    protected ManagedList createManagedFilterList(String filterClass, String javascript)
    {
        ManagedList filterList = new ManagedList();
        filterList.add(new RuntimeBeanReference("__filter_" + filterClass + "_" + javascript));
        return filterList;
    }

}
