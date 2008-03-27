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
package org.directwebremoting.bayeux;

import java.util.List;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.dwrp.PlainCallMarshaller;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.impl.ContainerUtil;

import dojox.cometd.Bayeux;

/**
 * @author Greg Wilkins [gregw at webtide dot com]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BayeuxServletContextAttributeListener implements ServletContextAttributeListener
{
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextAttributeListener#attributeAdded(javax.servlet.ServletContextAttributeEvent)
     */
    public void attributeAdded(ServletContextAttributeEvent scab)
    {
        synchronized (this)
        {
            if (bayeuxHandler == null && "dojox.cometd.bayeux".equals(scab.getName()))
            {
                Bayeux bayeux = (Bayeux) scab.getValue();
                bayeuxHandler = new BayeuxClient(bayeux);

                if (remoter != null)
                {
                    bayeuxHandler.setRemoter(remoter);
                }

                if (converterManager != null)
                {
                    bayeuxHandler.setConverterManager(converterManager);
                }

                if (plainCallMarshaller != null)
                {
                    bayeuxHandler.setPlainCallMarshaller(plainCallMarshaller);
                }
            }
            
            if (ContainerUtil.ATTRIBUTE_CONTAINER_LIST.equals(scab.getName()))
            {
                @SuppressWarnings("unchecked")
                List<Container> containers = (List<Container>) scab.getValue();
                
                log.debug("containers="+containers);
                
                for (Container container: containers)
                {
                    log.debug("container=" + container);
                    log.debug("beans=" + container.getBeanNames());
                    Remoter r = (Remoter) container.getBean("org.directwebremoting.extend.Remoter");
                    if (r != null)
                    {
                        remoter = r;
                        if (bayeuxHandler != null)
                        {
                            bayeuxHandler.setRemoter(remoter);
                        }
                        log.debug("remoter=" + remoter);
                    }

                    ConverterManager c = (ConverterManager) container.getBean("org.directwebremoting.extend.ConverterManager");
                    if (c != null)
                    {
                        converterManager = c;
                        if (bayeuxHandler != null)
                        {
                            bayeuxHandler.setConverterManager(converterManager);
                        }
                        log.debug("converterManager=" + converterManager);
                    }

                    PlainCallMarshaller p = (PlainCallMarshaller) container.getBean("org.directwebremoting.dwrp.PlainCallMarshaller");
                    if (p != null)
                    {
                        this.plainCallMarshaller = p;
                        if (bayeuxHandler != null)
                        {
                            bayeuxHandler.setPlainCallMarshaller(plainCallMarshaller);
                        }

                        log.debug("plainCallMarshaller=" + plainCallMarshaller);
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextAttributeListener#attributeRemoved(javax.servlet.ServletContextAttributeEvent)
     */
    public void attributeRemoved(ServletContextAttributeEvent scab)
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextAttributeListener#attributeReplaced(javax.servlet.ServletContextAttributeEvent)
     */
    public void attributeReplaced(ServletContextAttributeEvent scab)
    {
    }

    private BayeuxClient bayeuxHandler;
    private Remoter remoter;
    private ConverterManager converterManager;
    private PlainCallMarshaller plainCallMarshaller;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BayeuxServletContextAttributeListener.class);
}
