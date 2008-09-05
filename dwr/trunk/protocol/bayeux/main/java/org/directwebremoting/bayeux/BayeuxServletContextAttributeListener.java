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

import org.directwebremoting.Container;
import org.directwebremoting.dwrp.PlainCallHandler;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.impl.StartupUtil;

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

                if (plainCallHandler != null)
                {
                    bayeuxHandler.setPlainCallHandler(plainCallHandler);
                }
            }

            if (StartupUtil.ATTRIBUTE_CONTAINER_LIST.equals(scab.getName()))
            {
                @SuppressWarnings("unchecked")
                List<Container> containers = (List<Container>) scab.getValue();

                for (Container container: containers)
                {
                    Remoter r = container.getBean(Remoter.class);
                    if (r != null)
                    {
                        remoter = r;
                        if (bayeuxHandler != null)
                        {
                            bayeuxHandler.setRemoter(remoter);
                        }
                    }

                    ConverterManager c = container.getBean(ConverterManager.class);
                    if (c != null)
                    {
                        converterManager = c;
                        if (bayeuxHandler != null)
                        {
                            bayeuxHandler.setConverterManager(converterManager);
                        }
                    }

                    PlainCallHandler p = container.getBean(PlainCallHandler.class);
                    if (p != null)
                    {
                        this.plainCallHandler = p;
                        if (bayeuxHandler != null)
                        {
                            bayeuxHandler.setPlainCallHandler(plainCallHandler);
                        }
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

    private PlainCallHandler plainCallHandler;
}
