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
package org.directwebremoting.impl;

import java.util.Collection;
import java.util.Iterator;

import org.directwebremoting.Container;
import org.directwebremoting.extend.InitializingBean;

/**
 * An implementation of some of the simpler methods from {@link Container}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractContainer implements Container
{
    
    /**
     * Used after setup to call {@link InitializingBean#afterContainerSetup(Container)}
     * on all the contained beans.
     */
    protected void callInitializingBeans()
    {
        callInitializingBeans(getBeanNames());
    }

    /**
     * Call {@link InitializingBean#afterContainerSetup(Container)} on the named
     * beans
     * @param beanNames The beans to setup.
     */
    protected void callInitializingBeans(Collection beanNames)
    {
        for (Iterator it = beanNames.iterator(); it.hasNext();)
        {
            String name = (String) it.next();
            Object bean = getBean(name);

            if (bean instanceof InitializingBean)
            {
                InitializingBean startMeUp = (InitializingBean) bean;
                startMeUp.afterContainerSetup(this);
            }
        }
    }

}
