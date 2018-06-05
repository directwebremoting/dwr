package org.directwebremoting.guice;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.guice.util.AbstractModule;
import org.directwebremoting.util.LocalUtil;

import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;

import static java.util.Arrays.*;

import static org.directwebremoting.guice.ParamName.*;

/**
 * An extension of the enhanced {@link AbstractModule} from the util
 * subpackage that adds DWR configuration methods when used in conjunction
 * with {@link DwrGuiceServlet}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public abstract class AbstractDwrModule extends AbstractModule
{
    /**
     * Implement this method to configure Guice bindings for a DWR-based
     * web application.
     */
    @Override
    protected abstract void configure();


    /**
     * Call this method before configuration to explicitly determine the behavior
     * of {@link #bindDwrScopes()}. If not called, the default behavior is to bind
     * the potentially conflicting types only if the Guice ServletModule is not found
     * in the classloader. That is usually the right behavior; it should not often be
     * necessary to call this method.
     * @param newBindPotentiallyConflictingTypes whether to bind request, response, and
     *   session types to DWR scopes without qualifying with an annotation
     */
    protected final void bindPotentiallyConflictingTypes(boolean newBindPotentiallyConflictingTypes)
    {
        this.bindPotentiallyConflictingTypes = newBindPotentiallyConflictingTypes;
    }

    /**
     * Configure DWR scopes and bindings for servlet-related types.
     * If {@link #bindPotentiallyConflictingTypes} has been
     * called previously for this module, this method is equivalent
     * to calling {@link #bindDwrScopes(boolean) bindDwrScopes}
     * with the value passed to {@link #bindPotentiallyConflictingTypes}.
     * Otherwise this module will include bindings that might conflict
     * with those provided by Guice's ServletModule <strong>only</strong>
     * if ServletModule is not found in the current class loader.
     * <p>Idempotent within current thread.</p>
     */
    protected void bindDwrScopes()
    {
        if (this.bindPotentiallyConflictingTypes == null)
        {
            bindDwrScopes(!guiceServletModuleExists());
        }
        else
        {
            bindDwrScopes(this.bindPotentiallyConflictingTypes);
        }
    }

    /**
     * Configure DWR scopes and bindings for servlet-related types,
     * specifying explicitly whether to include bindings that might
     * conflict with those provided by Guice's ServletModule.
     * The {@link #bindDwrScopes() variant} of this method that takes
     * no arguments usually does the right; it should not often be
     * necessary to call this method.
     * <p>Idempotent within current thread.</p>
     * @param newBindPotentiallyConflictingTypes whether to bind request, response,
     *     and session types (risking conflict with Guice)
     */
    protected void bindDwrScopes(boolean newBindPotentiallyConflictingTypes)
    {
        if (!boundDwrScopes.get())
        {
            boundDwrScopes.set(true);
            install(new DwrGuiceServletModule(newBindPotentiallyConflictingTypes));
        }
    }


    /**
     * Call this method in
     * {@link org.directwebremoting.guice.AbstractDwrModule#configure configure}
     * to specify classes that DWR should scan for annotations.
     * @param classes the classes to be scanned for DWR-specific annotations
     */
    protected void bindAnnotatedClasses(Class<?>... classes)
    {
        bind(List.class)
            .annotatedWith(new InitParamImpl(CLASSES, unique.incrementAndGet()))
            .toInstance(asList(classes));
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
     * @param scriptName the name by which the target type will be known to script callers
     * @param type the type to bind as a target for remote method calls
     */
    protected <T> LinkedBindingBuilder<T> bindRemotedAs(String scriptName, Class<T> type)
    {
        return bind(type)
            .annotatedWith(new RemotedImpl(scriptName));
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
     * @param impl a type for which a conversion is already defined
     */
    protected <T> void bindConversion(Class<T> type, Class<? extends T> impl)
    {
        bind(Converter.class)
            .annotatedWith(new ConvertingImpl(type, impl))
            .to(InternalConverter.class); // never used, subverted by InternalConverterManager
    }

    /**
     * Creates a binding for an Ajax filter for the script named
     * {@code scriptName}.
     * @param scriptName the script to filter
     */
    protected LinkedBindingBuilder<AjaxFilter> bindFilter(String scriptName)
    {
        return bind(AjaxFilter.class)
            .annotatedWith(new FilteringImpl(scriptName, unique.incrementAndGet()));
    }

    /**
     * Creates a binding for a global Ajax filter.
     */
    protected LinkedBindingBuilder<AjaxFilter> bindGlobalFilter()
    {
        return bind(AjaxFilter.class)
            .annotatedWith(new FilteringImpl("", unique.incrementAndGet()));
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
     * Used to determine what value {@link #bindDwrScopes() bindDwrScopes()}
     * passes to {@link #bindDwrScopes(boolean) bindDwrScopes(boolean)}.
     * If null, the result of calling {@code !guiceServletModuleExists()}
     * is used.
     */
    volatile Boolean bindPotentiallyConflictingTypes = null;


    private static final AtomicLong unique = new AtomicLong();

    private static final ThreadLocal<Boolean> boundDwrScopes = new ThreadLocal<Boolean>()
    {
        @Override
        protected Boolean initialValue()
        {
            return false;
        }
    };


    private static boolean guiceServletModuleExists()
    {
        try
        {
            LocalUtil.classForName("com.google.inject.servlet.ServletModule");
            return true;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }
}
