/*
 * Copyright 2005 Joe Walker
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.directwebremoting.Container;
import org.directwebremoting.impl.DefaultContainer;
import org.directwebremoting.util.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * A <code>Container</code> implementation that looks up all beans from the
 * configuration specified in a Spring context.
 * It loads the configuration from a Spring web application context.
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringContainer extends DefaultContainer implements Container, BeanFactoryAware
{
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.DefaultContainer#getBean(java.lang.String)
     */
    public Object getBean(String id)
    {
        Object reply = super.getBean(id);
        if (reply == null)
        {
            reply = beanFactory.getBean(id);
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.DefaultContainer#getBeanNames()
     */
    public Collection getBeanNames()
    {
        List names = new ArrayList();

        // Snarf the beans from Spring
        if (beanFactory instanceof ListableBeanFactory)
        {
            ListableBeanFactory listable = (ListableBeanFactory) beanFactory;
            names.addAll(Arrays.asList(listable.getBeanDefinitionNames()));
        }
        else
        {
            log.warn("List of beanNames does not include Spring beans since your BeanFactory is not a ListableBeanFactory."); //$NON-NLS-1$
        }

        // And append the DWR ones
        names.addAll(super.getBeanNames());

        return Collections.unmodifiableCollection(names);
    }

    /**
     * The Spring BeanFactory that we read from
     */
    protected BeanFactory beanFactory;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(SpringContainer.class);
}
