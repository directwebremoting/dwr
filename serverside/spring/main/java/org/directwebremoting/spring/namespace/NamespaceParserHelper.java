package org.directwebremoting.spring.namespace;

import java.lang.reflect.Method;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.StringUtils;

/**
 * Convenience methods to find things in context while parsing DWR namespace.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public abstract class NamespaceParserHelper
{
    /**
     *  Tries to obtain the beanClassName from the definition and if that fails tries to get it from
     *  the parent (and even parent BeanFactory if we have to).
     *
     *  @param definition a non null instance
     *  @param registry a non null instance
     *  @return class name or null if not found
     */
    protected String resolveBeanClassname(BeanDefinition definition, BeanDefinitionRegistry registry)
    {
        String beanClassName = definition.getBeanClassName();
        while(!StringUtils.hasText(beanClassName))
        {
            try
            {
                Method m = definition.getClass().getMethod("getParentName", new Class[0]);
                String parentName = (String) m.invoke(definition, new Object[0]);
                BeanDefinition parentDefinition = findParentDefinition(parentName, registry);
                beanClassName = parentDefinition.getBeanClassName();
                definition = parentDefinition;
            } catch (Exception e)
            {
                throw new FatalBeanException("No parent bean could be found for " + definition, e);
            }
        }
        return beanClassName;
    }

    /**
     * Finds a parent bean definition in the hierarchy of contexts.
     *
     * @param parentName any
     * @param registry any
     * @return any
     */
    protected BeanDefinition findParentDefinition(String parentName, BeanDefinitionRegistry registry)
    {
        if (registry != null)
        {
            if (registry.containsBeanDefinition(parentName))
            {
                return registry.getBeanDefinition(parentName);
            }
            else if (registry instanceof HierarchicalBeanFactory)
            {
                // Try to get parent definition from the parent BeanFactory. This could return null
                BeanFactory parentBeanFactory = ((HierarchicalBeanFactory) registry).getParentBeanFactory();
                return findParentDefinition(parentName, (BeanDefinitionRegistry) parentBeanFactory);
            }
        }
        return null;
    }

}
