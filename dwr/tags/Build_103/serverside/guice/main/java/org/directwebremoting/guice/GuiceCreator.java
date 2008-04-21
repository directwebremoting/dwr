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

import com.google.inject.Injector;

import org.directwebremoting.extend.Creator;
import org.directwebremoting.create.NewCreator;

import static org.directwebremoting.guice.DwrGuiceUtil.getInjector;

/**
 * A creator that uses Guice dependency injection to create remoted objects.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class GuiceCreator extends NewCreator implements Creator
{

    public GuiceCreator()
    {
        this.injector = getInjector();
    }

    /**
     * Specified via {@link org.directwebremoting.annotations.RemoteProxy @RemoteProxy}
     * or via a parameter in XML configuration.
     */
    @Override
    public void setClass(String classname)
    {
        try
        {
            // Don't use LocalUtil.classForName because it insists
            // on a default constructor, and we want to be able to
            // use an @Inject constructor.
            this.type = Class.forName(classname);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(String.format("GuiceCreator: class %s not found", classname));
        }
    }

    /**
     * The class named through {@link GuiceCreator#setClass setClass}.
     */
    @Override
    public Class<?> getType()
    {
        return type;
    }

    /**
     * Looks up an instance of this creator's type with an
     * {@link com.google.inject.Injector Injector}.
     */
    @Override
    public Object getInstance()
    {
        return injector.getInstance(type);
    }

    /**
     * The type of object being created.
     */
    private volatile Class<?> type;

    /**
     * The Injector with which objects are created.
     */
    private final Injector injector;
}
