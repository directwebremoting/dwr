package org.directwebremoting.guice;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.directwebremoting.impl.DefaultConverterManager;
import org.directwebremoting.io.RawData;
import org.directwebremoting.util.LocalUtil;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;

import static org.directwebremoting.guice.DwrGuiceUtil.*;

/**
 * Extends an existing converter manager with an injected list of converters
 * specified at Guice bind-time. Only to be used in conjunction with
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

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#addConverterType(java.lang.String, java.lang.String)
     */
    public void addConverterType(String id, String className)
    {
        converterManager.addConverterType(id, className);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#addConverter(java.lang.String, java.lang.String, java.util.Map)
     */
    public void addConverter(String match, String type, Map<String, String> params) throws IllegalArgumentException, InstantiationException, IllegalAccessException
    {
        converterManager.addConverter(match, type, params);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#addConverter(java.lang.String, org.directwebremoting.extend.Converter)
     */
    public void addConverter(String match, Converter converter) throws IllegalArgumentException
    {
        converterManager.addConverter(match, converter);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#addConverter(java.lang.Class, org.directwebremoting.extend.Converter)
     */
    public void addConverter(Class<?> clazz, Converter converter)
    {
        converterManager.addConverter(clazz, converter);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#getConverterMatchStrings()
     */
    public Collection<String> getConverterMatchStrings()
    {
        return converterManager.getConverterMatchStrings();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#getConverterByMatchString(java.lang.String)
     */
    public Converter getConverterByMatchString(String match)
    {
        return converterManager.getConverterByMatchString(match);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#getNamedConverterJavaScriptNames()
     */
    public Collection<String> getNamedConverterJavaScriptNames()
    {
        return converterManager.getNamedConverterJavaScriptNames();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#getNamedConverter(java.lang.String)
     */
    public NamedConverter getNamedConverter(String javascriptClassName)
    {
        return converterManager.getNamedConverter(javascriptClassName);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#isConvertable(java.lang.Class)
     */
    public boolean isConvertable(Class<?> paramType)
    {
        return converterManager.isConvertable(paramType);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#getClientDeclaredType(org.directwebremoting.extend.InboundVariable)
     */
    public Class<?> getClientDeclaredType(InboundVariable iv)
    {
        return converterManager.getClientDeclaredType(iv);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext, org.directwebremoting.extend.TypeHintContext)
     */
    public <T> T convertInbound(Class<T> paramType, InboundVariable iv, Property incc) throws ConversionException
    {
        return converterManager.convertInbound(paramType, iv, incc);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#convertInbound(java.lang.Class, org.directwebremoting.io.RawData)
     */
    public <T> T convertInbound(Class<T> paramType, RawData data) throws ConversionException
    {
        return converterManager.convertInbound(paramType, data);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object object, OutboundContext outctx) throws ConversionException
    {
        return converterManager.convertOutbound(object, outctx);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#checkOverride(org.directwebremoting.extend.Property)
     */
    public Property checkOverride(Property property)
    {
        return converterManager.checkOverride(property);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#setOverrideProperty(org.directwebremoting.extend.Property, org.directwebremoting.extend.Property)
     */
    public void setOverrideProperty(Property original, Property replacement)
    {
        converterManager.setOverrideProperty(original, replacement);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ConverterManager#setConverters(java.util.Map)
     */
    public void setConverters(Map<String, Converter> converters)
    {
        converterManager.setConverters(converters);
    }

    /**
     *
     */
    private final ConverterManager converterManager;

    @SuppressWarnings("unchecked")
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
                Class<?> type = ann.type();
                Class<?> impl = ann.impl();

                if ("".equals(match))
                {
                    // Use the type name as a match string
                    match = type.getName();
                }

                Provider<Converter> provider = null;
                Class<?> cvtType;

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

    /**
     *
     */
    private static ConverterManager getConverterManager()
    {
        String name = typeName.get();
        try
        {
            @SuppressWarnings("unchecked")
            Class<? extends ConverterManager> cls = (Class<? extends ConverterManager>) LocalUtil.classForName(name);
            return cls.newInstance();
        }
        catch (Exception e)
        {
            if (name != null && !"".equals(name))
            {
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
    private static final Log log = LogFactory.getLog(InternalConverterManager.class);
}
