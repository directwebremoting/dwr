/*
 * Copyright 2008 Tim Peierls
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
package org.directwebremoting.guice.util;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matcher;
import static com.google.inject.matcher.Matchers.subclassesOf;
import static com.google.inject.name.Names.named;

import static org.directwebremoting.guice.util.DeclaredBy.declaredBy;
import static org.directwebremoting.guice.util.Numbers.numbered;
import static org.directwebremoting.guice.util.OutermostCallInterceptor.outermostCallWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.aopalliance.intercept.MethodInterceptor;


/**
 * An extension of Guice's {@link com.google.inject.AbstractModule AbstractModule}
 * that provides support for member injection of instances constructed at bind-time:
 * the module itself and
 * {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor} instances
 * constructed before injection.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractModule extends com.google.inject.AbstractModule
{
    /**
     * Variant of {@link AbstractModule#bindInterceptor bindInterceptor} intercepting non-nested calls
     * to instances of a given type (or of a subclass of that type), allowing
     * constructor-injection of interceptors described by class.
     */
    public final void bindInterceptor(Class<?> type,
                                      Class<?>... classes)
    {
        bindInterceptor(
            subclassesOf(type),
            declaredBy(type),
            outermostCallWrapper(),
            classes);
    }

    /**
     * Variant of {@link AbstractModule#bindInterceptor bindInterceptor} intercepting non-nested calls
     * to instances of a given type (or of a subclass of that type), allowing
     * constructor-injection of interceptors described by {@link com.google.inject.Key Key}.
     */
    public final void bindInterceptor(Class<?> type,
                                      Key<?>... keys)
    {
        bindInterceptor(
            subclassesOf(type),
            declaredBy(type),
            outermostCallWrapper(),
            keys);
    }

    /**
     * Variant of {@link #bindInterceptor bindInterceptor} that allows constructor-injection of
     * interceptors described by class.
     */
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                Class<?>... classes)
    {
        bindInterceptor(classMatcher, methodMatcher, NULL_WRAPPER, classes);
    }

    /**
     * Variant of {@link #bindInterceptor bindInterceptor} that allows constructor-injection of
     * interceptors described by {@link com.google.inject.Key Key}.
     */
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                Key<?>... keys)
    {
        bindInterceptor(classMatcher, methodMatcher, NULL_WRAPPER, keys);
    }

    /**
     * Variant of {@link #bindInterceptor bindInterceptor} that allows constructor-injection of
     * interceptors described by class, each wrapped by a method interceptor wrapper.
     */
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                MethodInterceptorWrapper wrapper,
                                Class<?>... classes)
    {
        if (classes != null)
        {
            MethodInterceptor[] interceptors = new MethodInterceptor[classes.length];
            int i = 0;
            for (Class<?> cls : classes)
            {
                if (!MethodInterceptor.class.isAssignableFrom(cls))
                {
                    addError("bindInterceptor: %s does not implement MethodInterceptor", cls.getName());
                }
                else
                {
                    @SuppressWarnings("unchecked")
                    Class<? extends MethodInterceptor> c = (Class<? extends MethodInterceptor>) cls;
                    interceptors[i++] = wrap(wrapper, c);
                }
            }
            bindInterceptor(classMatcher, methodMatcher, interceptors);
        }
    }

    /**
     * Variant of {@link #bindInterceptor bindInterceptor} that allows constructor-injection of
     * interceptors described by Key, each wrapped by a method interceptor wrapper.
     */
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                MethodInterceptorWrapper wrapper,
                                Key<?>... keys)
    {
        if (keys != null)
        {
            MethodInterceptor[] interceptors = new MethodInterceptor[keys.length];
            int i = 0;
            for (Key<?> key : keys)
            {
                Type type = key.getTypeLiteral().getType();
                if (!(type instanceof Class))
                {
                    addError("bindInterceptor: %s is not a Key for a MethodInterceptor subtype", key);
                }
                else // type instanceof Class
                {
                    Class cls = (Class) type;
                    if (!MethodInterceptor.class.isAssignableFrom(cls))
                    {
                        addError("bindInterceptor: %s does not implement MethodInterceptor", cls.getName());
                    }
                    else
                    {
                        @SuppressWarnings("unchecked")
                        Key<? extends MethodInterceptor> k = (Key<? extends MethodInterceptor>) key;
                        interceptors[i++] = wrap(wrapper, k);
                    }
                }
            }
            bindInterceptor(classMatcher, methodMatcher, interceptors);
        }
    }

    /**
     * Overridden version of
     * {@link com.google.inject.AbstractModule#bindInterceptor bindInterceptor}
     * that, in addition to the standard behavior, arranges for field and method injection
     * of each MethodInterceptor in {@code interceptors}.
     */
    @Override
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                MethodInterceptor... interceptors)
    {
        registerForInjection(interceptors);
        super.bindInterceptor(classMatcher, methodMatcher, interceptors);
    }

    /**
     * Arranges for this module to be field and method injected when
     * the Injector is created. It is safe to call this method more than once.
     */
    protected void registerForInjection()
    {
        registerForInjection((Object[]) null);
    }

    /**
     * Arranges for this module and each of the given objects
     * to be field and method injected when the Injector is created.
     * It is safe to call this method more than once, and it is safe
     * to call it more than once on the same object(s).
     */
    protected <T> void registerForInjection(T... objects)
    {
        ensureSelfInjection(binder());

        if (objects != null)
        {
            for (T object : objects)
            {
                if (object != null)
                {
                    registeredForInjection.putIfAbsent(object, true);
                }
            }
        }
    }

    /**
     * Overrides {@link com.google.inject.AbstractModule#install AbstractModule.install}
     * to automatically register {@code module} for injection if it is an
     * {@link AbstractModule AbstractModule}, in addition to the standard behavior of
     * arranging for {@code module} to be configured during injector creation.
     */
    @Override
    protected void install(Module module)
    {
        if (module instanceof AbstractModule)
        {
            final AbstractModule abstractModule = (AbstractModule) module;
            Module wrapper = new Module()
            {
                public void configure(Binder binder)
                {
                    binder.install(abstractModule);
                    abstractModule.ensureSelfInjection(binder);
                }
            };
            super.install(wrapper);
        }
        else
        {
            super.install(module);
        }
    }


    @Inject private void injectRegisteredObjects(Injector injector)
    {
        for (Object injectee : registeredForInjection.keySet())
        {
            injector.injectMembers(injectee);
        }
    }

    private void ensureSelfInjection(Binder binder)
    {
        if (ensuredSelfInjection.compareAndSet(false, true))
        {
            binder
                .bind(AbstractModule.class)
                .annotatedWith(numbered(unique.incrementAndGet()))
                .toInstance(this);
        }
    }


    private final ConcurrentMap<Object, Boolean> registeredForInjection =
        new ConcurrentHashMap<Object, Boolean>();

    private final AtomicBoolean ensuredSelfInjection = new AtomicBoolean(false);


    private static MethodInterceptor wrap(MethodInterceptorWrapper wrapper, Class<? extends MethodInterceptor> cls)
    {
        return wrapper.wrap(new InjectingMethodInterceptor(cls));
    }


    private static MethodInterceptor wrap(MethodInterceptorWrapper wrapper, Key<? extends MethodInterceptor> key)
    {
        return wrapper.wrap(new InjectingMethodInterceptor(key));
    }


    private static MethodInterceptorWrapper NULL_WRAPPER = new MethodInterceptorWrapper()
    {
        public MethodInterceptor wrap(MethodInterceptor methodInterceptor)
        {
            return methodInterceptor;
        }
    };

    private static final AtomicLong unique = new AtomicLong();
}
