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

    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

}
