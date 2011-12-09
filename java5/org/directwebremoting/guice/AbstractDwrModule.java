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

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Arrays.asList;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.Converter;

import static org.directwebremoting.guice.ParamName.CLASSES;

/**
 * An extension of {@link AbstractModule} that adds DWR configuration methods,
 * in conjunction with {@link DwrGuiceServlet}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractDwrModule extends AbstractModule
{
    /**
     * Implement this method to configure Guice bindings for a DWR-based
     * web application.
     */
    protected abstract void configure();

    
    /** 
     * Configure DWR scopes and bindings for servlet-related types;
     * incompatible with Guice's {@link ServletModule} because their
     * bindings for request, response, and session conflict.
     */
    protected void bindDwrScopes() 
    {
        install(new DwrGuiceServletModule(true));
    }

    
    /** 
     * Configure DWR scopes and bindings for servlet-related types,
     * specifying whether to include bindings that conflict with those
     * provided by Guice's {@link ServletModule}.
     * @param bindPotentiallyConflictingTypes whether to bind request, response,
     *     and session types (risking conflict with Guice)
     */
    protected void bindDwrScopes(boolean bindPotentiallyConflictingTypes) 
    {
        install(new DwrGuiceServletModule(bindPotentiallyConflictingTypes));
    }
    
    
    /**
     * Creates a binding for a conversion for types with names matching 
     * {@code match}.
     * @param match the string describing which types to convert
     */
    protected LinkedBindingBuilder<Converter> bindConversion(String match)
    {
        return bind(Converter.class)
            .annotatedWith(new ConvertingImpl(match));
    }
    
    
    /**
     * Creates a binding for a conversion for {@code type}.
     * @param type the type to be converted
     */
    protected LinkedBindingBuilder<Converter> bindConversion(Class<?> type)
    {
        return bind(Converter.class)
            .annotatedWith(new ConvertingImpl(type));
    }
    
    
    /**
     * Creates a binding for a conversion for {@code type} using an existing
     * conversion for {@code impl}, which must be assignable to {@code type}. 
     * The check for an existing conversion happens at run-time.
     * @param type the type to be converted
     */
    protected <T> void bindConversion(Class<T> type, Class<? extends T> impl)
    {
        bind(Converter.class)
            .annotatedWith(new ConvertingImpl(type, impl))
            .to(InternalConverter.class); // never used, subverted by InternalConverterManager
    }
    
    
    /**
     * Creates a binding to {@code type} that is used as the target of a
     * remote method call with the class's unqualified name as the script name.
     * 
     * <p>Note: if you are scoping the result, don't rely on implicit binding.
     * Instead, link the type to itself explicitly. For example,
     * <pre>
     *   bindRemoted(ConcreteService.class)
     *       .to(ConcreteService.class) // this line is required
     *       .in(DwrScopes.SESSION);
     * </pre>
     * This could be considered a bug.
     * @param type the type to bind as a target for remote method calls
     */
    protected <T> LinkedBindingBuilder<T> bindRemoted(Class<T> type)
    {
        return bind(type)
            .annotatedWith(new RemotedImpl());
    }
    
    
    /**
     * Creates a binding to a type that is used as the target of a
     * remote method call with the given {@code scriptName}.
     * 
     * <p>Note: if you are scoping the result, don't rely on implicit binding.
     * Instead, link the type to itself explicitly. For example,
     * <pre>
     *   bindRemotedAs("Mixer", ConcreteService.class)
     *       .to(ConcreteService.class) // this line is required
     *       .in(DwrScopes.SESSION);
     * </pre>
     * This could be considered a bug.
     * @param type the type to bind as a target for remote method calls
     * @param scriptName the name by which the target type will be known to script callers
     */
    protected <T> LinkedBindingBuilder<T> bindRemotedAs(String scriptName, Class<T> type)
    {
        return bind(type)
            .annotatedWith(new RemotedImpl(scriptName));
    }
    
    
    /**
     * Creates a binding for an Ajax filter for the script named
     * {@code scriptName}.
     * @param scriptName the script to filter
     */
    protected LinkedBindingBuilder<AjaxFilter> bindFilter(String scriptName)
    {
        return bind(AjaxFilter.class)
            .annotatedWith(new FilteringImpl(scriptName));
    }
    
    
    /**
     * Creates a binding for a global Ajax filter.
     * @param scriptName the script to filter
     */
    protected LinkedBindingBuilder<AjaxFilter> bindGlobalFilter()
    {
        return bind(AjaxFilter.class)
            .annotatedWith(new FilteringImpl());
    }

    
    /**
     * Call this method in 
     * {@link org.directwebremoting.guice.AbstractDwrModule#configure configure}
     * to create a binding for a DWR parameter.
     * @param paramName a parameter name supported by DWR
     */
    protected ConstantBindingBuilder bindParameter(ParamName paramName)
    {
        return bindConstant()
            .annotatedWith(new InitParamImpl(paramName));
    }


    /**
     * Call this method in 
     * {@link org.directwebremoting.guice.AbstractDwrModule#configure configure}
     * to specify classes that DWR should scan for annotations.
     * @param classes the classes to be scanned for DWR-specific annotations
     */
    protected void bindAnnotatedClasses(Class... classes)
    {
        bind(new TypeLiteral<List<Class>>(){})
            .annotatedWith(new InitParamImpl(CLASSES))
            .toInstance(asList(classes));

    }
}
