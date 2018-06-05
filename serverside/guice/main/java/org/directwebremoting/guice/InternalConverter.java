package org.directwebremoting.guice;

import java.util.concurrent.atomic.AtomicBoolean;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Specialized converter implementation that uses a Provider to
 * look up instances to delegate to. This class is used by
 * {@link InternalConverterManager}.
 * @author Tim Peierls [tim at peierls dot net]
 */
class InternalConverter extends AbstractConverter implements Converter
{
    /**
     * Only used to satisfy bindings for the two-arg {@code bindConversion}
     * method of {@link AbstractDwrModule}.
     */
    @Inject
    InternalConverter()
    {
        this.type = null;
        this.provider = null;
    }

    /**
     * Adapts a Provider into a Converter.
     */
    InternalConverter(Class<?> type, Provider<Converter> provider)
    {
        this.type = type;
        this.provider = provider;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext)
     */
    public Object convertInbound(Class<?> typeInfo, InboundVariable data) throws ConversionException
    {
        try
        {
            return getConverter().convertInbound(typeInfo, data);
        }
        catch (ClassCastException e)
        {
            throw new ConversionException(type, e);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        try
        {
            return getConverter().convertOutbound(type.cast(data), outctx);
        }
        catch (ClassCastException e)
        {
            throw new ConversionException(type, e);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BaseV20Converter#setConverterManager(org.directwebremoting.extend.ConverterManager)
     */
    @Override
    public void setConverterManager(ConverterManager mgr)
    {
        this.converterManager = mgr;
    }


    private Converter getConverter()
    {
        Converter converter = provider.get();
        if (calledSetConverterManager.compareAndSet(false, true))
        {
            converter.setConverterManager(converterManager);
        }
        return converter;
    }


    private final Class<?> type;

    private final Provider<Converter> provider;

    private final AtomicBoolean calledSetConverterManager = new AtomicBoolean();

    private volatile ConverterManager converterManager;
}
