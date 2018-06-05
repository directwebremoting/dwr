package org.directwebremoting.guice.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.matcher.Matcher;

import static org.directwebremoting.guice.util.DeclaredBy.*;
import static org.directwebremoting.guice.util.Numbers.*;
import static org.directwebremoting.guice.util.OutermostCallInterceptor.*;

import static com.google.inject.matcher.Matchers.*;

/**
 * An extension of Guice's {@link com.google.inject.AbstractModule AbstractModule}
 * that provides support for member injection of objects created before injector creation,
 * including {@link com.google.inject.AbstractModule AbstractModule} itself and
 * {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor} instances
 * constructed at bind-time.
 * <p>In addition, there are {@link #bindInterceptor(Class, Class...) bindInterceptor} variants
 * that arrange for the interceptors to be constructor-injected, that can wrap each interceptor
 * with extra common behavior, and that intercept all calls to methods declared by a given type.
 * These variants all call {@link #registerForInjection() registerForInjection}, so there is
 * no need to call it explicitly when using these methods (though it's harmless to do so).</p>
 * <p>The {@code Class<?>} and {@code Key<?>} arguments in the
 * {@link #bindInterceptor(Class, Class...) bindInterceptor} variants really should
 * be {@code Class<? extends MethodInterceptor>} and {@code Key<? extends MethodInterceptor>},
 * but this would cause compile-time warnings on every use.</p>
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractModule extends com.google.inject.AbstractModule
{
    /**
     * No-arg constructor for subclasses.
     */
    protected AbstractModule()
    {
        super();
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
     * to be field and method injected when the
     * {@link com.google.inject.Injector Injector} is created.
     * It is safe to call this method more than once, and it is safe
     * to call it more than once on the same object(s).
     * @param objects the objects whose members are to be injected
     *   when the {@link com.google.inject.Injector Injector} is created
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
     * Overridden version of
     * {@link com.google.inject.AbstractModule#bindInterceptor bindInterceptor}
     * that, in addition to the standard behavior, arranges for field and method injection
     * of each MethodInterceptor in {@code interceptors}.
     * @param classMatcher matches classes the interception should apply to.
     *   For example: {@code only(Runnable.class)}.
     * @param methodMatcher matches methods the interception should apply to.
     *   For example: {@code annotatedWith(Transactional.class)}.
     * @param methodInterceptors chain of
     *   {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}s
     *   used to intercept calls.
     */
    @Override
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                MethodInterceptor... methodInterceptors)
    {
        registerForInjection(methodInterceptors);
        super.bindInterceptor(classMatcher, methodMatcher, methodInterceptors);
    }


    /**
     * Variant of {@link #bindInterceptor(Class, Class...)} that allows
     * constructor-injection of interceptors described by class.
     * @param classMatcher matches classes the interception should apply to.
     *   For example: {@code only(Runnable.class)}.
     * @param methodMatcher matches methods the interception should apply to.
     *   For example: {@code annotatedWith(Transactional.class)}.
     * @param methodInterceptorClasses chain of
     *   {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}s
     *   used to intercept calls, specified by class.
     */
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                Class<?>... methodInterceptorClasses)
    {
        bindInterceptor(classMatcher, methodMatcher, NULL_WRAPPER, methodInterceptorClasses);
    }

    /**
     * Variant of {@link #bindInterceptor(Class, Class...) bindInterceptor} that
     * allows constructor-injection of interceptors described by
     * {@link com.google.inject.Key Key}.
     * @param classMatcher matches classes the interception should apply to.
     *   For example: {@code only(Runnable.class)}.
     * @param methodMatcher matches methods the interception should apply to.
     *   For example: {@code annotatedWith(Transactional.class)}.
     * @param methodInterceptorKeys chain of
     *   {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}s
     *   used to intercept calls, specified by {@link com.google.inject.Key Key}.
     */
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                Key<?>... methodInterceptorKeys)
    {
        bindInterceptor(classMatcher, methodMatcher, NULL_WRAPPER, methodInterceptorKeys);
    }


    /**
     * Variant of {@link #bindInterceptor(Class, Class...) bindInterceptor} that
     * allows constructor-injection of interceptors described by class, each
     * wrapped by a method interceptor wrapper.
     * @param classMatcher matches classes the interception should apply to.
     *   For example: {@code only(Runnable.class)}.
     * @param methodMatcher matches methods the interception should apply to.
     *   For example: {@code annotatedWith(Transactional.class)}.
     * @param methodInterceptorWrapper a wrapper applied to each of the specified interceptors.
     * @param methodInterceptorClasses chain of
     *   {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}s
     *   used to intercept calls, specified by class.
     */
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                MethodInterceptorWrapper methodInterceptorWrapper,
                                Class<?>... methodInterceptorClasses)
    {
        if (methodInterceptorClasses != null)
        {
            MethodInterceptor[] interceptors = new MethodInterceptor[methodInterceptorClasses.length];
            int i = 0;
            for (Class<?> cls : methodInterceptorClasses)
            {
                if (!MethodInterceptor.class.isAssignableFrom(cls))
                {
                    addError("bindInterceptor: %s does not implement MethodInterceptor", cls.getName());
                }
                else
                {
                    @SuppressWarnings("unchecked")
                    Class<? extends MethodInterceptor> c = (Class<? extends MethodInterceptor>) cls;
                    interceptors[i++] = wrap(methodInterceptorWrapper, c);
                }
            }
            bindInterceptor(classMatcher, methodMatcher, interceptors);
        }
    }

    /**
     * Variant of {@link #bindInterceptor(Class, Class...) bindInterceptor} that
     * allows constructor-injection of interceptors described by Key, each
     * wrapped by a method interceptor wrapper.
     * @param classMatcher matches classes the interception should apply to.
     *   For example: {@code only(Runnable.class)}.
     * @param methodMatcher matches methods the interception should apply to.
     *   For example: {@code annotatedWith(Transactional.class)}.
     * @param methodInterceptorWrapper a wrapper applied to each of the specified interceptors.
     * @param methodInterceptorKeys chain of
     *   {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}s
     *   used to intercept calls, specified by {@link com.google.inject.Key Key}.
     */
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher,
                                Matcher<? super Method> methodMatcher,
                                MethodInterceptorWrapper methodInterceptorWrapper,
                                Key<?>... methodInterceptorKeys)
    {
        if (methodInterceptorKeys != null)
        {
            MethodInterceptor[] interceptors = new MethodInterceptor[methodInterceptorKeys.length];
            int i = 0;
            for (Key<?> key : methodInterceptorKeys)
            {
                Type type = key.getTypeLiteral().getType();
                if (!(type instanceof Class))
                {
                    addError("bindInterceptor: %s is not a Key for a MethodInterceptor subtype", key);
                }
                else // type instanceof Class
                {
                    Class<?> cls = (Class<?>) type;
                    if (!MethodInterceptor.class.isAssignableFrom(cls))
                    {
                        addError("bindInterceptor: %s does not implement MethodInterceptor", cls.getName());
                    }
                    else
                    {
                        @SuppressWarnings("unchecked")
                        Key<? extends MethodInterceptor> k = (Key<? extends MethodInterceptor>) key;
                        interceptors[i++] = wrap(methodInterceptorWrapper, k);
                    }
                }
            }
            bindInterceptor(classMatcher, methodMatcher, interceptors);
        }
    }


    /**
     * Variant of {@link #bindInterceptor(Class, Class...) bindInterceptor} intercepting
     * non-nested calls to instances of a given type (or of a subclass of that type),
     * allowing constructor-injection of interceptors described by class.
     * @param targetType the type for which method calls are to be intercepted
     * @param methodInterceptorClasses chain of
     *   {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}s
     *   used to intercept calls, specified by class.
     */
    public final void bindInterceptor(Class<?> targetType,
                                      Class<?>... methodInterceptorClasses)
    {
        bindInterceptor(
            subclassesOf(targetType),
            declaredBy(targetType),
            outermostCallWrapper(),
            methodInterceptorClasses);
    }

    /**
     * Variant of {@link #bindInterceptor(Class, Class...) bindInterceptor} intercepting
     * non-nested calls to instances of a given type (or of a subclass of that type),
     * allowing constructor-injection of interceptors described by
     * {@link com.google.inject.Key Key}.
     * @param targetType the type for which method calls are to be intercepted
     * @param methodInterceptorKeys chain of
     *   {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}s
     *   used to intercept calls, specified by {@link com.google.inject.Key Key}.
     */
    public final void bindInterceptor(Class<?> targetType,
                                      Key<?>... methodInterceptorKeys)
    {
        bindInterceptor(
            subclassesOf(targetType),
            declaredBy(targetType),
            outermostCallWrapper(),
            methodInterceptorKeys);
    }


    /**
     * Overrides {@link com.google.inject.AbstractModule#install AbstractModule.install}
     * to automatically register {@code module} for injection if it is an
     * {@link AbstractModule}, in addition to the standard behavior of
     * arranging for {@code module} to be configured during injector creation.
     * @param module the module to be installed and (if it is an
     *   {@link AbstractModule}) registered for injection
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

    @SuppressWarnings("unused") /* called by reflection */
    @Inject
    private void injectRegisteredObjects(Injector injector)
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
