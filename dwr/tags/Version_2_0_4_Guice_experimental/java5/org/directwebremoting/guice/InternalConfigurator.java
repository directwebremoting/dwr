/*
 * Copyright 2007 Tim Peierls
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
package org.directwebremoting.guice;

import org.directwebremoting.Container;
import org.directwebremoting.extend.Configurator;

import com.google.inject.Inject;

import static org.directwebremoting.guice.DwrGuiceUtil.getInjector;

/**
 * Delegates to an injected configurator. This class only exists to provide an
 * publicly accessible named class with a parameterless constructor.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class InternalConfigurator implements Configurator
{
    public InternalConfigurator()
    {
        getInjector().injectMembers(this);
    }

    public void configure(Container container)
    {
        configurator.configure(container);
    }

    @Inject private volatile Configurator configurator;
}
