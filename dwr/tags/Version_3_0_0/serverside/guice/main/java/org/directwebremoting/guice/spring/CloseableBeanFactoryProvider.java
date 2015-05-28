/*
 * Copyright 2007 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.guice.spring;

import com.google.inject.Provider;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;

/**
 * Lazily creates a singleton BeanFactory and, when {@code close()} is 
 * called, destroys it if it exists and is a {@code DisposableBean}.
 */
class CloseableBeanFactoryProvider implements Closeable, Provider<BeanFactory> 
{    
    CloseableBeanFactoryProvider(BeanFactoryLoader loader)
    {
        this.loader = loader;
    }
    
    /* (non-Javadoc)
     * @see com.google.inject.Provider#get()
     */
    public synchronized BeanFactory get()
    {
        if (beanFactory == null)
        {
            beanFactory = loader.loadBeanFactory();
        }
        return beanFactory;
    }
    
    /* (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    public synchronized void close() throws IOException 
    {
        if (beanFactory != null && beanFactory instanceof DisposableBean)
        {
            try 
            {
                ((DisposableBean) beanFactory).destroy();
                log.info("Destroyed BeanFactory from Guice provider.");
            } 
            catch (IOException e) 
            {
                log.info("Caught IO exception destroying BeanFactory: " + e);
                throw e;
            } 
            catch (Exception e) 
            {
                log.info("Unexpected exception while destroying BeanFactory: " + e);
                throw new RuntimeException(e);
            }
        }
    }
    
    private final BeanFactoryLoader loader;
    
    /* @GuardedBy("this") */ private BeanFactory beanFactory;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(CloseableBeanFactoryProvider.class);
}
