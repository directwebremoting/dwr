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

import com.google.inject.Key;
import com.google.inject.Injector;
import com.google.inject.Provider;

import java.util.Collection;
import java.util.Map;

import org.directwebremoting.dwrp.DefaultConverterManager;
import org.directwebremoting.extend.*;
import org.directwebremoting.util.Logger;

import static org.directwebremoting.guice.DwrGuiceUtil.getInjector;
import static org.directwebremoting.guice.DwrGuiceUtil.getServletContext;

/**
 * Extends an existing converter manager with an injected list of converters
 * specified at Guice bind-time. Only to be used in conjection with
 * {@link DwrGuiceServlet}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class InternalConverterManager implements ConverterManager 
{
    /**
     * Retrieves an underlying converter manager from thread-local state
     * to which this class delegates {@link ConverterManager} calls.
     */
    public InternalConverterManager()
    {
        this.converterManager = getConverterManager();
        addConverters();
    }


    public void addConverterType(String id, String className)
    {
        converterManager.addConverterType(id, className);
    }

    public void addConverter(String match, String type, Map params) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        converterManager.addConverter(match, type, params);
    }

    public void addConverter(String match, Converter converter) throws IllegalArgumentException
    {
        converterManager.addConverter(match, converter);
    }

    public Collection getConverterMatchStrings()
    {
        return converterManager.getConverterMatchStrings();
    }

    public Converter getConverterByMatchString(String match)
    {
        return converterManager.getConverterByMatchString(match);
    }

    public boolean isConvertable(Class paramType)
    {
        return converterManager.isConvertable(paramType);
    }

    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx, TypeHintContext incc) throws MarshallException
    {
        return converterManager.convertInbound(paramType, iv, inctx, incc);
    }

    public OutboundVariable convertOutbound(Object object, OutboundContext outctx) throws MarshallException
    {
        return converterManager.convertOutbound(object, outctx);
    }

    public void setExtraTypeInfo(TypeHintContext thc, Class type)
    {
        converterManager.setExtraTypeInfo(thc, type);
    }

    public Class getExtraTypeInfo(TypeHintContext thc)
    {
        return converterManager.getExtraTypeInfo(thc);
    }

    public void setConverters(Map converters)
    {
        converterManager.setConverters(converters);
    }

    
    private final ConverterManager converterManager;

    private void addConverters()
    {
        Injector injector = getInjector();
        for (Key<?> key : injector.getBindings().keySet())
        {
            Class<?> atype = key.getAnnotationType();
            if (atype != null && Converting.class.isAssignableFrom(atype))
            {
                Converting ann = Converting.class.cast(key.getAnnotation());
                
                String match = ann.match();
                Class type = ann.type();
                Class impl = ann.impl();
                
                if ("".equals(match))
                {
                    // Use the type name as a match string
                    match = type.getName();
                }

                Provider<Converter> provider = null;
                Class cvtType;
                
                if (impl.equals(Void.class))
                {
                    // No impl specified, so there should be a Converter
                    // for this key.
                    
                    provider = injector.getProvider((Key<Converter>) key);
                    cvtType = type;
                }
                else
                {
                    // Impl class specified, so the Converter for key is
                    // bogus (the injected constructor InternalConverter
                    // is just to keep Guice happy); see the two-arg 
                    // bindConversion method in AbstractDwrModule.
                    
                    try 
                    {
                        // First try looking for a Converter for impl in the bindings.
                     
                        Key<Converter> ikey = Key.get(Converter.class, new ConvertingImpl(impl));
                        provider = injector.getProvider(ikey);
                    }
                    catch (RuntimeException e)
                    {
                        // Ignore any trouble we have looking things up.
                    }
                    
                    if (provider == null)
                    {
                        // It wasn't in the bindings, so use a Provider that
                        // looks in the underlying ConverterManager.
                    
                        final String implMatch = impl.getName();
                        provider = new Provider<Converter>()
                        {
                            public Converter get()
                            {
                                return getConverterByMatchString(implMatch);
                            }
                        };
                    }
                    
                    cvtType = impl;
                }
                addConverter(match, new InternalConverter(cvtType, provider));
            }
        }
    }
    
    /**
     * Stores a type name in a thread-local variable for later retrieval by
     * {@code getConverterManager}.
     */
    static void setTypeName(String name)
    {
        typeName.set(name);
    }
    
    private static ConverterManager getConverterManager()
    {
        String name = typeName.get();
        try
        {
            Class<? extends ConverterManager> cls = 
                (Class<? extends ConverterManager>) Class.forName(name);
            return cls.newInstance();
        }
        catch (Exception e)
        {
            if (name != null && !"".equals(name)) {
                log.warn("Couldn't make ConverterManager from type: " + name);
            }
            return new DefaultConverterManager();
        }
    }


    /**
     * Place to stash a type name for retrieval in same thread.
     */
    private static final ThreadLocal<String> typeName = new ThreadLocal<String>();


    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(InternalConverterManager.class);
}
